package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class AddUpdateUserPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private PasswordTextField ConfPass = new PasswordTextField("confpass", new Model(""));
	private PasswordTextField Password = new PasswordTextField("password");
	Customer NewUser = new Customer();
	public AddUpdateUserPage(final PageParameters parameters) {
		super(parameters);
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		if (FruitShopSession.get().getUsername().isEmpty()){
			add(new Label("PageHeader", "Νέος Χρήστης"));
		} else {
			add(new Label("PageHeader", "Αλλαγή στοιχείων"));
			NewUser = FruitShopSession.get().getCurrentUser();
		}
        Form NewUserForm = new Form("NewUserForm", new CompoundPropertyModel<Customer>(NewUser));
        add(NewUserForm);
        NewUserForm.add(new Label("NameHeader", "Ονοματεπώνυμο"));
        NewUserForm.add(new TextField("name").setRequired(true));
        NewUserForm.add(new Label("AddressHeader", "Διεύθυνση"));
        NewUserForm.add(new TextField("address").setRequired(true));
        NewUserForm.add(new Label("CityHeader", "Πόλη"));
        NewUserForm.add(new TextField("city").setRequired(true));
        NewUserForm.add(new Label("PhoneHeader", "Τηλέφωνο"));
        NewUserForm.add(new TextField("phone").setRequired(true));
        NewUserForm.add(new Label("EmailHeader", "Email"));
        NewUserForm.add(new TextField("email").setRequired(true).
        		    add(EmailAddressValidator.getInstance()));
        NewUserForm.add(new Label("PasswordHeader", "Κωδικός"));
        NewUserForm.add(Password.setRequired(true));
        NewUserForm.add(new Label("ConfirmHeader", "Επιβεβαίωση κωδικού"));
        NewUserForm.add(ConfPass.setRequired(true));
        Button SaveButton =new Button("NewUserButton"){
        	@Override
        	public void onSubmit(){
        		if(!Password.getInput().toString().equals(ConfPass.getInput().toString())){
        			error("Ο κωδικός δεν συμφωνεί με την επιβεβαίωση κωδικού");
        			return;
        		}
        		if(FruitShopSession.get().getUsername().isEmpty()){
        			if(MailExists(NewUser.getEmail())){  
        				error("To Email υπάρχει ήδη.");
        				return;
        			}
        		}
        		
        		NewUser.setPassword(Sha1.getHash(NewUser.getPassword()));
        		Date date = new Date();
        		DateFormat dateFormatIsoInt = new SimpleDateFormat("yyyyMMdd");
        		NewUser.setAdmin(0);
        		NewUser.setInsertdate(Integer.valueOf(dateFormatIsoInt.format(date).toString()));
        		if(!AddOrUpdateUser(NewUser)){
        			error("Η εγγραφή δεν ολοκληρώθηκε. Παρακαλώ προσπαθήστε αργότερα.");
        		} else {
        			setResponsePage(HomePage.class);
        		}
        	}
        };
        if(FruitShopSession.get().getUsername().isEmpty()){
        	SaveButton.add(AttributeModifier.append("value", "Εγγραφή"));
        } else {
        	SaveButton.add(AttributeModifier.append("value", "Διόρθωση"));
        }
        NewUserForm.add(SaveButton);
        
        //SaveButton.add(new Label("ButtonValue", "Εγγραφή"));
        //SaveButton.getMarkupAttributes().add("value", "Εγγραφή");
    }
}
