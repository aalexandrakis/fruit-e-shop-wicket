package com.aalexandrakis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        System.out.println("POST called");
        Map<String, String> parms = new HashMap<String, String>(); 
        while (req.getParameterNames().hasMoreElements()){
        	String parmName = req.getParameterNames().nextElement().toString();
        	parms.put(parmName, req.getParameter(parmName));
        }
        for (String key : parms.keySet()){
        	System.out.println("Parm => " + key + " value => " + parms.get(key));
        }
        resp.setStatus(200);        
     }
}