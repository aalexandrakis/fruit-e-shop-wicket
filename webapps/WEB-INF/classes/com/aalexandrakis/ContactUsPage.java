package com.aalexandrakis;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

public class ContactUsPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private TextField YoursMailText = new TextField("YoursEmail", new Model(FruitShopSession.get().getUsername()));
	private TextField SubjectText = new TextField("Subject", new Model(""));
	private TextArea MessageText = new TextArea("Message", new Model(""));    


	public ContactUsPage(final PageParameters parameters) {
		super(parameters);
		// TODO Add your page's components here
		Form ContactUsForm = new Form("ContactUsForm");
		add(ContactUsForm);
		ContactUsForm.add(new Label("HeaderPage", "Επικοινωνήστε μαζί μας"));
		ContactUsForm.add(new Label("EmailHeader", "Email"));
		ContactUsForm.add(new Label("BussinessEmail", bussiness_email));
		ContactUsForm.add(new Label("PhonesHeader", "Επικοινωνήστε μαζί μας"));
		ContactUsForm.add(new Label("Phones", "Σταθερό:210-7472461 Κινητό:6948-211181"));
		ContactUsForm.add(new Label("SendUsAMessage", "Στείλτε μας ένα μύνημα"));
		ContactUsForm.add(new Label("YoursEmailHeader", "Το Email σας"));
		ContactUsForm.add(YoursMailText.setRequired(true));
		ContactUsForm.add(new Label("SubjectHeader", "Θέμα"));
		ContactUsForm.add(SubjectText.setRequired(true));
		ContactUsForm.add(new Label("MessageHeader", "Μήνυμα"));
		ContactUsForm.add(MessageText.setRequired(true));
		Button SendIt = new Button("SendIt"){
			@Override
			public void onSubmit(){
				String sbj = SubjectText.getInput().toString();
				String msg = MessageText.getInput().toString();
				String eml = YoursMailText.getInput().toString();
				msg = "Message From " + eml + "<br>" + msg;
				if(!SendMail(bussiness_email, sbj, msg)){
					error("Το μήνυμα δεν έχει αποσταλεί. Παρακαλώ προσπαθήστε αργότερα");
				} else {
					setResponsePage(HomePage.class);
				}
			}
		};
		SendIt.add(AttributeModifier.append("value", "Αποστολή"));
		ContactUsForm.add(SendIt);

    }
}
