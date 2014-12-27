package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class OrderDetailsPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private List<OrderedItem> OrderedItems = new ArrayList<OrderedItem>();
	private Order Order = new Order();
	private String nextStatus = "";
	private Integer OrderId=0;
	
	
	public OrderDetailsPage(final PageParameters parameters) {
		super(parameters);
		
		if (!FruitShopSession.get().getIsAdmin() &&
				FruitShopSession.get().getUsername().isEmpty()){
			setResponsePage(HomePage.class);
		}
			
		if(parameters.getNamedKeys().contains("orderid")){
			OrderId = parameters.get("orderid").toInteger();
			OrderedItems= getOrderDetails(OrderId);
			Order = getOrderFromId(OrderId);
			nextStatus = getStatusDesc(Order.getStatus() + 1);
			//setDefaultModelObject(Order);
		}
		System.out.print("OrderId" + OrderId + "\n");
		construct();
	}
	
	
	@SuppressWarnings("unchecked")
	private void construct(){
		add(new Label("OrderId", Order.getOrderid()));
		add(new Label("UserName", Order.getUsername()));
		System.out.print("OrderDate" + Order.getDate() + "\n");
		add(new Label("Date", dateFormatIso.format(Order.getDate())));
		add(new Label("StatusDesc", getStatusDesc(Order.getStatus())));
		
		
		add(new Label("itemidHeader", "Κωδικός"));
		add(new Label("descrHeader", "Περιγραφή"));
		add(new Label("mmHeader", "Μον.Μέτρησης"));
		add(new Label("priceHeader", "Τιμή μον."));
		add(new Label("quantityHeader", "Ποσότητα"));
		add(new Label("amountHeader", "Αξία"));
		
		add(new ListView("order", OrderedItems) {
		@Override
		protected void populateItem(ListItem item) {
			Item CurrentItem = new Item();
			OrderedItem OrderedItem = (OrderedItem) item.getModelObject();
			CurrentItem = getItem(OrderedItem.getItemid());
			item.add(new Label("itemid", OrderedItem.getItemid()));
			item.add(new Label("descr", CurrentItem.getDescr()));
			item.add(new Label("mm", CurrentItem.getMm()));
			item.add(new Label("price", OrderedItem.getPrice()));
			item.add(new Label("quantity", OrderedItem.getQuantity()));
			item.add(new Label("amount", OrderedItem.getQuantity() * OrderedItem.getPrice()));
			//item.add(removeLink("remove", item));
		}
		});
		add(new Label("total", "Σύνολο"));
		add(new Label("totalamount", Order.getAmmount()));
		Form formStatus = new Form("formStatus");
		formStatus.add(new Label("changeStatusTo", "Αλλαγή κατάστασης σε").setVisible(FruitShopSession.get().getIsAdmin()));
		Button changeStatusButton = new Button("changeStatusButton"){
			@Override
			public void onSubmit(){
				//Order = (Orders) getDefaultModelObject();
				Integer stat =  Order.getStatus();
				stat += 1;
				Order.setStatus(stat);
				if (!UpdateOrder(Order)){
					info("Something went wrong. Try again");
				} else {
					setResponsePage(OrdersPage.class);
				}
			}
			
			@Override
			public void onError() {
				System.out.print("onError");
			}
		};
		add(formStatus);
		formStatus.add(changeStatusButton);
		changeStatusButton.setVisible(FruitShopSession.get().getIsAdmin());
		changeStatusButton.add(AttributeModifier.replace("value", nextStatus));
		formStatus.add(new NumberTextField("orderid", new Model(OrderId)));
		
		
		

	}

 }
	
	

