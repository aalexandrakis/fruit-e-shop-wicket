package com.aalexandrakis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimeType;
import javax.jws.WebService;
import javax.mail.internet.MimeMultipart;
import javax.swing.text.DateFormatter;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;


@WebService
@Path("services")
public class WebServices {


	@Path("/test")
	@Produces("text/plain")
	@GET
	public String test() throws JSONException{
		JSONObject user = new JSONObject();
		user.put("userName", "its me");
		user.put("userPassword", "mypassword");
		return user.toString();
	}

	
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response logIn(@FormParam("email") String email, @FormParam("password") String password ) throws JSONException{
		JSONObject returnObject = new JSONObject();
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Customer C where C.email = :email and C.password = :password";
			Query q = session.createQuery(hql)
					  .setString("email", email)
					  .setString("password", password);
			//info(Sha1.getHash(Password.getInput().toString()));
			List<Customer> resultList = q.list();
			
            //DebugString = q.getQueryString()										
			if (resultList.isEmpty()){
				returnObject.put("message", "Incorrect email or password. Please try again");
				return Response.ok(returnObject.toString()).build();

			} else {
				for (Customer cust : resultList){
					returnObject.put("status", 0);
					returnObject.put("id", cust.getCustomerId());
					returnObject.put("email", cust.getEmail());
					returnObject.put("password", cust.getPassword());
					returnObject.put("name", cust.getName());
					returnObject.put("phone", cust.getPhone());
					returnObject.put("address", cust.getAddress());
					returnObject.put("city", cust.getCity());
					return Response.ok(returnObject.toString()).build();
				}
				
			}	
			tx.commit();
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace();
			}finally { 
				((org.hibernate.Session) session).close(); 
			}
		return Response.status(500).build();
	}
	
	
	@Path("/getCategories")
	@GET
	@Produces(MediaType.APPLICATION_XML + "; charset=UTF-8")
	public Response getCategories() {
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Items_Category";
			Query q = session.createQuery(hql);
			List<Items_Category> resultList = q.list();
			
            //DebugString = q.getQueryString()										
			if (resultList.isEmpty()){
				return Response.ok(null).build();
			} else {
				Categories categories = new Categories(resultList);
				return Response.ok(categories).build();
			}
	    } catch (HibernateException e) { 
			if (tx!=null) tx.rollback();
			e.printStackTrace();
	    } finally { 
			((org.hibernate.Session) session).close(); 
		}
		return Response.status(500).build();
	}
	
	
	@Path("/getItems/{catId}")
	@GET
	@Produces(MediaType.APPLICATION_XML + "; charset=UTF-8")
	public Response getItems(@PathParam("catId") String catId) {
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Item where categoryid = :catId";
			Query q = session.createQuery(hql).setString("catId", catId);
			List<Items> resultList = q.list();
			
            //DebugString = q.getQueryString()										
			if (resultList.isEmpty()){
				return Response.ok(null).build();
			} else {
				Items items = new Items(resultList);
				return Response.ok(items).build();
			}
	    } catch (HibernateException e) { 
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		}finally { 
			((org.hibernate.Session) session).close(); 
		}
		return Response.status(500).build();
	}
	

	@Path("/resetPassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response resetPassword(@FormParam("email") String email) {
		JSONObject jsonResponse = new JSONObject();
		String newPassword = LoginPanel.makeid();
		try {
			if (!WicketApplication.get().resetPassword(email, newPassword)){
				jsonResponse.put("status", "FAILED");
				jsonResponse.put("message", "Reset password failed because of an internal error. Please try later");
			} else {
				jsonResponse.put("status", "SUCCESS");
				jsonResponse.put("message", "Password changed succesfully");	
			}
			return Response.ok(jsonResponse.toString()).build();
			
		} catch (JSONException e){
			e.printStackTrace();
		}
	
		return Response.status(500).build();
	}
	
	@SuppressWarnings("finally")
	@Path("/register")
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response register(@FormParam("name") String name, @FormParam("address") String address, @FormParam("city") String city, @FormParam("phone") String phone, 
							 @FormParam("email") String email, @FormParam("password") String password ) {
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = session.beginTransaction();
		JSONObject jsonResponse = new JSONObject();
		try{
			String hql = "FROM Customer where email = :email";
			Query q = session.createQuery(hql).setString("email", email);
			List<Customer> custList = q.list();
			if (custList.size() > 0){
				jsonResponse.put("status", "FAILED");
				jsonResponse.put("message", "This email already exists.");
			} else {
				DateFormat df = new SimpleDateFormat("yyyyMMdd");
				int date = Integer.valueOf(df.format(new Date())); 
				Customer cust = new Customer(null, name, address, city, phone, password, email, 0, date);
				session.saveOrUpdate(cust);
				tx.commit();
				jsonResponse.put("status", "SUCCESS");
				jsonResponse.put("message", "Your registration is completed.");
				
			}
		} catch (JSONException e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} finally {			
			session.close();
			return Response.ok(jsonResponse.toString()).build();
		}
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	@Path("/updateUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response updateUser(@FormParam("name") String name, @FormParam("address") String address, @FormParam("city") String city, @FormParam("phone") String phone, 
							 @FormParam("email") String email, @FormParam("id") String id) {
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = session.beginTransaction();
		JSONObject jsonResponse = new JSONObject();
		try{
			String hql = "FROM Customer where email = :email and id <> :userId";
			Query q = session.createQuery(hql).setString("email", email).setInteger("userId", Integer.valueOf(id));
			List<Customer> custList = q.list();
			if (custList.size() > 0){
				jsonResponse.put("status", "FAILED");
				jsonResponse.put("message", "This email already exists.");
			} else {
				Customer cust = (Customer) session.get(Customer.class, Integer.valueOf(id));
				cust.setName(name);
				cust.setAddress(address);
				cust.setCity(city);
				cust.setPhone(phone);
				cust.setEmail(email);
				session.saveOrUpdate(cust);
				tx.commit();
				jsonResponse.put("status", "SUCCESS");
				jsonResponse.put("message", "Your account updated successfully.");
				
			}
		} catch (JSONException e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} finally {			
			session.close();
			return Response.ok(jsonResponse.toString()).build();
		}
	}
	
	@SuppressWarnings({ "finally"})
	@Path("/updatePassword")
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response updatePassword(@FormParam("id") String id, @FormParam("password") String password) {
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = session.beginTransaction();
		JSONObject jsonResponse = new JSONObject();
		try{
			Customer cust = (Customer) session.get(Customer.class, Integer.valueOf(id));
			cust.setPassword(password);
			session.saveOrUpdate(cust);
			tx.commit();
			jsonResponse.put("status", "SUCCESS");
			jsonResponse.put("message", "Your account updated successfully.");
		} catch (JSONException e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			jsonResponse.put("status", "FAILED");
			jsonResponse.put("message", e.getMessage());
		} finally {			
			session.close();
			return Response.ok(jsonResponse.toString()).build();
		}
	}
}
	

