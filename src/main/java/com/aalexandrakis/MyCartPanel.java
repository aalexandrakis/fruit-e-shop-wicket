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
public class MyCartPanel extends Panel {
    
	public MyCartPanel(String id) {
		super(id);
		this.Construct();
	}

private void Construct(){
	add(new Image("carticon", "buttons/cart.GIF"));
    add(new Label("CartSummaryText", "Το σύνολο του καλαθιού είναι ")); 
	add(new Label("CartSummary", new Model(FruitShopSession.get().CalcCart())));			     

}

@Override
protected void onBeforeRender(){
	removeAll();
	super.onBeforeRender();
	this.Construct();
}
}