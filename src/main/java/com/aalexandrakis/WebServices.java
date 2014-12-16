package com.aalexandrakis;

import java.util.Iterator;
import java.util.List;

import javax.activation.MimeType;
import javax.jws.WebService;
import javax.mail.internet.MimeMultipart;
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

import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


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
			System.out.println("email "+ email + " password " + password);
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
	
	
	
}
