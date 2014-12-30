package com.aalexandrakis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CompleteOrder extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	 @Override
     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("GET called");
        resp.setStatus(200);
        resp.getWriter().append("TEST SERVLET CALL");
     }
	 
	 @Override
     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		resp.setStatus(200);
		StringBuilder parms = new StringBuilder();
		parms.append("cmd=_notify-validate");
		for (Object object : req.getParameterMap().keySet()){
        	if (object instanceof String){
	        	String key = (String) object;
	        	parms.append("&" + key + "=" + req.getParameter(key));
	        	System.out.println("Parm => " + key + " value => " + req.getParameter(key));
        	}
        }
        if(!req.getParameter("txn_id").equals("PAY ON DELIVERY")){
        	URL url = new URL("https://www.sandbox.paypal.com/cgi-bin/webscr");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Method", "POST");
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.addRequestProperty("Content-Length", String.valueOf(parms.length()));
			conn.setDoOutput(true);
			conn.getOutputStream().write(parms.toString().getBytes());
			int responseCode = conn.getResponseCode();
			System.out.println("response code : " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				stringBuilder.append(inputLine);
			in.close();
			if (!stringBuilder.toString().equals("VERIFIED")){
				System.out.println("INVALID order by user " + req.getParameter("custom"));
				return;
			}
        }
        if (req.getParameter("payment_status").equals("Completed")){
        	writeOrder(req);
        } else {
        	Session session = HibarnateUtil.getSessionFactory().openSession();
			Customer cust = (Customer) session.get(Customer.class, Integer.valueOf(req.getParameter("custom")));
			WicketApplication.SendMail(cust.getEmail(), "Your is in status " + req.getParameter("payment_status")+ ".", 
					"Complete your payment to start proccess your order.");
			session.close();	
        }
        
     }
	 
	 protected void writeOrder(HttpServletRequest req){
		 for (Object object : req.getParameterMap().keySet()){
        	if (object instanceof String){
	        	String key = (String) object;
	        	System.out.println("Parm => " + key + " value => " + req.getParameter(key));
        	}
        }
		String cartItemsString = req.getParameter("num_cart_items");
		int cartItems = 0;
		if (cartItemsString != null && !cartItemsString.equals("")){
			try {
			 cartItems = Integer.valueOf(cartItemsString);
			} catch (Exception e){
				cartItems = 0;
			}
		}
		List<CartItem> cart = new ArrayList<CartItem>();
		if (cartItems == 0){
			cart.add(new CartItem(new Item(Integer.valueOf(req.getParameter("item_number")), req.getParameter("item_name"), null, null, null, null, null, null)
						, Float.valueOf(req.getParameter("quantity")), Float.valueOf(req.getParameter("mc_gross"))));
		} else {
			for (int i=1; i <= cartItems; i++){
				String num = String.valueOf(i);
				cart.add(new CartItem(new Item(Integer.valueOf(req.getParameter("item_number" + num)), req.getParameter("item_name" + num), null, null, null, null, null, null)
				, Float.valueOf(req.getParameter("quantity" + num)), Float.valueOf(req.getParameter("mc_gross" + num))));
			}
		}
		int custId = Integer.valueOf(req.getParameter("custom"));
		StringBuilder msg = new StringBuilder();
		Order order = new Order(null, new Date(), Float.valueOf(req.getParameter("mc_gross")), "", 0, req.getParameter("txn_id"), custId);
		Session session = HibarnateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(order);
			msg.append(order.toString() + "\n");
			for (CartItem cartItem : cart){
				float price = cartItem.getMcGross() / cartItem.getQuantity();
				OrderedItem orderedItem = new OrderedItem(order.getOrderid(), 
															  cartItem.getItem().getItemid(),
															  cartItem.getQuantity(), 
															  price,
															  cartItem.getMcGross());
				session.save(orderedItem);
				msg.append(orderedItem.toString() + "\n");
			}
			tx.commit();
			Customer cust = (Customer) session.get(Customer.class, custId);
			WicketApplication.SendMail(System.getenv("HOTMAIL"), "New order from " + cust.getEmail(), msg.toString());
			WicketApplication.SendMail(cust.getEmail(), "Your order have been placed successfully.", msg.toString());
		} catch (Exception e){
			tx.rollback();
		} finally {
			session.close();
		}
	 }
}