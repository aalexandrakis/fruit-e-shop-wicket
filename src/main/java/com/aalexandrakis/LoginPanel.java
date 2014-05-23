package com.aalexandrakis;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.wicket.Session;
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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;

/**
* Panel for displaying the contents of a shopping cart. The cart
* shows the entries and the total value of the cart. Each item
* can be removed by the user.
*/
public class LoginPanel extends Panel {
    
	private WebMarkupContainer LoginDiv = new WebMarkupContainer("LoginDiv");
	private WebMarkupContainer LogoutDiv = new WebMarkupContainer("LogoutDiv");
	private TextField Email = new TextField("Email", new Model());
	private PasswordTextField Password = new PasswordTextField("Password", new Model());
	
	public LoginPanel(String id) {
		super(id);
		add(new FeedbackPanel("feedback"));
		
		/*Login Form*/
		FruitShopSession.get().bind();
		add(LoginDiv);
		Form LoginForm = new Form("LoginForm") {
			@Override
			protected void onSubmit() {
			}
		};
		LoginDiv.add(LoginForm);
		LoginForm.add(new Label("EmailHeader", "Email"));
		LoginForm.add(Email.setRequired(true));
		LoginForm.add(new Label("PasswordHeader", "Password"));
		LoginForm.add(Password.setRequired(false));
		SubmitLink ConnectLink = new SubmitLink("ConnectLink"){
								@Override
								public void onSubmit() {
									if (Password.getInput().isEmpty()){
										error("Password required");
										return;
									}
									SessionFactory sf = HibarnateUtil.getSessionFactory(); 
									org.hibernate.Session session = sf.openSession(); 
									Transaction tx = null;
									try{ 
										tx = session.beginTransaction();
										
										//String hql = "From Customer";
										//Query q = session.createQuery(hql);
										String hql = "From Customer C where C.email = :email and C.password = :password";
										Query q = session.createQuery(hql)
												  .setString("email", Email.getInput().toString())
												  .setString("password", Sha1.getHash(Password.getInput().toString()));
										//info(Sha1.getHash(Password.getInput().toString()));
										List<Customer> ResultList = q.list();
										
                                        //DebugString = q.getQueryString()										
										Iterator<Customer> SelectedCustomer;
										if (ResultList.isEmpty()){
											info("Λάθος email ή κωδικός.Προσπαθήστε ξανά");
										} else {
											SelectedCustomer = ResultList.iterator();
											while(SelectedCustomer.hasNext()){
												Customer CurrentCustomer = SelectedCustomer.next();
												FruitShopSession.get().setUsername(CurrentCustomer.getEmail());
												FruitShopSession.get().setCurrentUser(CurrentCustomer);
												if (CurrentCustomer.getAdmin().equals(1)){
													FruitShopSession.get().setIsAdmin(true);
												} else {
													FruitShopSession.get().setIsAdmin(false);
												}	
											}
										}	
										tx.commit();
									    } catch (HibernateException e) { 
											if (tx!=null) tx.rollback();
											e.printStackTrace(); 
										}finally { 
											((org.hibernate.Session) session).close(); 
										}

								}
        					};
		
		LoginForm.add(ConnectLink);
		ConnectLink.add(new Image("ConnectImg", 
				new SharedResourceReference(LoginPanel.class, "buttons/connect.GIF")));
        
		SubmitLink ForgotLink = new SubmitLink("ForgotLink"){
			
			@Override
			public void onSubmit() {
					String UserMail = Email.getInput().toString();
					String NewPassword = makeid();
					if (!WicketApplication.get().resetPassword(UserMail, NewPassword)){
					   info("Η αλλαγή του κωδικού δεν έγινε. Παρακαλώ προσπαθήστε ξανά αργότερα.");	
					} else {
						info("Η αλλαγή του κωδικού ολοκληρώθηκε με επιτυχεία.");
						info("Ο νέος κωδικός έχει σταλεί στο Email σας.");
					}
				
			}
		};
        
        
		LoginForm.add(ForgotLink);
        ForgotLink.add(new Image("ForgotImg", 
        		new SharedResourceReference(LoginPanel.class, "buttons/forgotpassword.GIF")));	
        
        
        /*End Login Form*/
       
        /*Disconnect button*/
        
        add(LogoutDiv);
        LogoutDiv.add(new Label("LoginEmail", new PropertyModel(FruitShopSession.get(), "username")));
        Link DisconnectLink = 
        new Link("DisconnectLink") {
        	@Override
        	public void onClick() {
        		FruitShopSession.get().setUsername("");
        		FruitShopSession.get().setIsAdmin(false);
        		FruitShopSession.get().setCurrentUser(null);
        		setResponsePage(HomePage.class);
        	}
        };

        LogoutDiv.add(DisconnectLink);
        DisconnectLink.add(new Image("DisconnectImg",
        		new SharedResourceReference(LoginPanel.class, "buttons/disconnect.GIF")));
        LogoutDiv.setVisible(!FruitShopSession.get().getUsername().isEmpty());
		     
	}

   @Override	
   protected void onBeforeRender(){
	   super.onBeforeRender();
	   if(FruitShopSession.get().getUsername().isEmpty()){
		   LoginDiv.setVisible(true);
		   LogoutDiv.setVisible(false);
	   } else {
		   LoginDiv.setVisible(false);
		   LogoutDiv.setVisible(true);
	   }
   }

   private String makeid(){
       String text = "";
       String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

       for( int i=0; i < 7; i++ )
           text += possible.charAt((int) Math.floor(Math.random() * possible.length()));
       return text;
   }
}