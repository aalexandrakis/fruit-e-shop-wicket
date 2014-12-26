package com.aalexandrakis;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

public class ContactUsPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private TextField yoursMailText = new TextField("yoursEmail", new Model(FruitShopSession.get().getUsername()));
	private TextField subjectText = new TextField("subject", new Model(""));
	private TextArea messageText = new TextArea("message", new Model(""));
	private FeedbackPanel errorMessages = new FeedbackPanel("errorMessages");


	public ContactUsPage(final PageParameters parameters) {
		super(parameters);
		// TODO Add your page's components here
		Form contactUsForm = new Form("contactUsForm");
		add(contactUsForm);
		contactUsForm.add(new Label("bussinessEmail", bussiness_email));
		contactUsForm.add(new Label("phones", "Phone: +306948211181"));
		contactUsForm.add(yoursMailText.setRequired(true));
		contactUsForm.add(subjectText.setRequired(true));
		contactUsForm.add(messageText.setRequired(true));
		add(errorMessages);
		errorMessages.setOutputMarkupId(true);
		errorMessages.setVisible(errorMessages.anyMessage());
		Button sendIt = new Button("sendIt"){
			@Override
			public void onSubmit(){
				String sbj = subjectText.getInput().toString();
				String msg = messageText.getInput().toString();
				String eml = yoursMailText.getInput().toString();
				msg = "Message From " + eml + "<br>" + msg;
				if(!SendMail(bussiness_email, sbj, msg)){
					errorMessages.error("Your message was not send because of an internal error. Please try later");
				} else {
					setResponsePage(HomePage.class, new PageParameters());
				}
			}
		};
		contactUsForm.add(sendIt);
    }

	@Override
	protected void onBeforeRender() {
		// TODO Auto-generated method stub
		super.onBeforeRender();
		errorMessages.setVisible(errorMessages.anyMessage());
	}
	
	
}
