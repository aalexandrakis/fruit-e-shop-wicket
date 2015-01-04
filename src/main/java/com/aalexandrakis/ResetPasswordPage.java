package com.aalexandrakis;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.eclipse.jetty.util.log.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;

import com.sun.jersey.spi.inject.Errors.ErrorMessagesException;

public class ResetPasswordPage extends BasePage {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailString = "";
	private String passwordString = "";
	private WebMarkupContainer mainDiv = new WebMarkupContainer("mainDiv");
	private TextField email = new TextField("email", new Model<String>(emailString));
	private FeedbackPanel errorMessages = new FeedbackPanel("errorMessages");
	
	public ResetPasswordPage(PageParameters parms) {
		
		super(parms);
		Form form = new Form("form"){
			@Override
			protected void onValidate() {
				// TODO Auto-generated method stub
				super.onValidate();
				SessionFactory sf = HibarnateUtil.getSessionFactory(); 
				org.hibernate.Session session = sf.openSession();
				Transaction tx = session.beginTransaction();
				try{ 
					String hql = "From Customer C where C.email = :email";
					Query q = session.createQuery(hql)
							  .setString("email", email.getInput());
					//info(Sha1.getHash(Password.getInput().toString()));
					List<Customer> resultList = q.list();
	                //DebugString = q.getQueryString()										
					if (resultList.size() == 0 || resultList.size() > 1 ){
						errorMessages.error("Email not found please try again");
					} else {
						Customer currentCustomer = resultList.get(0);
						String newPassword = makeid();
						currentCustomer.setPassword(Sha1.getHash(newPassword));
						session.update(currentCustomer);
						if (SendMail(email.getInput(), "Reset password" , "Your new password is " + newPassword)){
							tx.commit();
						} else {
							tx.rollback();
						}
					}	
			    } catch (HibernateException e) { 
					e.printStackTrace();
					errorMessages.error("An internal error occured. Please try later.");
				}finally {
					session.flush();
					session.close();
					HibarnateUtil.getSessionFactory().close();
				}		
			}

			@Override
			protected void onSubmit() {
				// TODO Auto-generated method stub
				super.onSubmit();
				if (!errorMessages.anyErrorMessage()){
					setResponsePage(new HomePage(new PageParameters()));
				}
			}
			
		};
		
		@SuppressWarnings("rawtypes")
		AjaxSubmitLink loginButton = new AjaxSubmitLink("loginButton"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};
		mainDiv.setOutputMarkupId(true);
		email.setOutputMarkupId(true);
		email.setRequired(true);
		errorMessages.setOutputMarkupId(true);
		errorMessages.setVisible(false);
		loginButton.setOutputMarkupId(true);
		form.add(email);
		form.add(loginButton);
		mainDiv.add(form);
		mainDiv.add(errorMessages);
		add(mainDiv);
		/*Login Form*/
		FruitShopSession.get().bind();
	}

   @Override	
   protected void onBeforeRender(){
	   super.onBeforeRender();
	   errorMessages.setVisible(errorMessages.anyMessage());
   }

   static String makeid(){
       String text = "";
       String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

       for( int i=0; i < 7; i++ )
           text += possible.charAt((int) Math.floor(Math.random() * possible.length()));
       return text;
   }
}