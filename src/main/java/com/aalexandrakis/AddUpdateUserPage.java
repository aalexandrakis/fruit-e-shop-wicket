package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;

public class AddUpdateUserPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private PasswordTextField ConfPass = new PasswordTextField("confpass", new Model<String>(""));
	private PasswordTextField Password = new PasswordTextField("password");
	private FeedbackPanel errorMessages = new FeedbackPanel("errorMessages");
	private Label panelHeader;
	Customer newUser = new Customer();
	public AddUpdateUserPage(final PageParameters parameters) {
		super(parameters);
		if (!FruitShopSession.get().getUsername().isEmpty()){
			newUser = FruitShopSession.get().getCurrentUser();
			panelHeader = new Label("panelHeader", "My Account");
		} else {
			panelHeader = new Label("panelHeader", "Sign Up");
		}
		errorMessages.setOutputMarkupId(true);
        add(errorMessages);
        add(panelHeader);
        Form<Customer> form = new Form<Customer>("form", new CompoundPropertyModel<Customer>(newUser));
        add(form);
        form.add(new TextField<String>("name").setRequired(true));
        form.add(new TextField<String>("address").setRequired(true));
        form.add(new TextField<String>("city").setRequired(true));
        form.add(new TextField<String>("phone").setRequired(true));
        form.add(new TextField<String>("email").setRequired(true).
        		    add(EmailAddressValidator.getInstance()));
        form.add(Password.setRequired(true));
        form.add(ConfPass.setRequired(true));
        
        Button submitButton =new Button("submitButton"){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
        	public void onSubmit(){
        		if(!Password.getInput().toString().equals(ConfPass.getInput().toString())){
        			errorMessages.error("Password confirmation must be the same with passord.");
        			return;
        		}
        		if(FruitShopSession.get().getUsername().isEmpty()){
        			if(MailExists(newUser.getEmail())){  
        				errorMessages.error("This email already exists.");
        				return;
        			}
        		}
        		
        		newUser.setPassword(Sha1.getHash(newUser.getPassword()));
        		Date date = new Date();
        		DateFormat dateFormatIsoInt = new SimpleDateFormat("yyyyMMdd");
        		newUser.setAdmin(0);
        		newUser.setInsertdate(Integer.valueOf(dateFormatIsoInt.format(date).toString()));
        		if(!AddOrUpdateUser(newUser)){
        			errorMessages.error("An internal error occured. Please try later.");
        		} else {
        			setResponsePage(HomePage.class);
        		}
        	}
        };
        if(FruitShopSession.get().getUsername().isEmpty()){
        	submitButton.add(AttributeModifier.append("value", "Sign Up"));
        } else {
        	submitButton.add(AttributeModifier.append("value", "Update User"));
        }
        form.add(submitButton);
        
        //SaveButton.add(new Label("ButtonValue", "Εγγραφή"));
        //SaveButton.getMarkupAttributes().add("value", "Εγγραφή");
    }
	@Override
	protected void onBeforeRender() {
		// TODO Auto-generated method stub
		super.onBeforeRender();
		errorMessages.setVisible(errorMessages.anyMessage());
	}
	
}
