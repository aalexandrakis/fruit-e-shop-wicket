package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class MyCartPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private List<CartItem> CartList = new ArrayList<CartItem>(FruitShopSession.get().getCart()); 
    public MyCartPage(final PageParameters parameters) {
		super(parameters);
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		this.construct();
		// TODO Add your page's components here

	} 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void construct(){
		//removeAll();
		add(new ListView("cart", new PropertyModel(this, "CartList")) {
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@Override
		protected void populateItem(ListItem item) {
			CartItem cartitem = (CartItem) item.getModelObject();
			item.add(new Label("itemid", cartitem.getItem().getItemid()));
			item.add(new Label("descr", cartitem.getItem().getDescr()));
			item.add(new Label("mm", cartitem.getItem().getMm()));
//			item.add(new Label("category", getCategory(cartitem.getCategoryid()).getCategory()));
			item.add(new Label("price",  cartitem.getItem().getPrice()));
			item.add(new Label("quantity", cartitem.getQuantity()));
			item.add(new Label("summary",  cartitem.getMcGross()));
			//item.add(removeLink("remove", item));
			Link removeLink = new Link("remove", item.getModel()){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					CartItem selected =  (CartItem) getModelObject();
					if(FruitShopSession.get().getCart().contains(selected)){
						FruitShopSession.get().getCart().remove(selected);
						CartList.remove(selected);
						//CartSummary = FruitShopSession.get().CalcCart();
					}
					
				}
			};
			item.add(removeLink);
			
			}
		});
		add(new Label("TotalHeader", "Σύνολο"));
		add(new Label("total", new Model(FruitShopSession.get().calcCart())));
		
		Link emptyLink = new Link("emptyLink"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
					FruitShopSession.get().getCart().clear();
					CartList.clear();
			}
			
			@Override
			protected void onBeforeRender(){
				super.onBeforeRender();
				setVisible(!FruitShopSession.get().getCart().isEmpty());
			}
		};
		add(emptyLink);
		
		Link payLink = new Link("payLink"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				    if(writeOrder()){
				    	FruitShopSession.get().getCart().clear();
				    	CartList.clear();
				    }
			}
			@Override
			protected void onBeforeRender(){
				super.onBeforeRender();
				if (FruitShopSession.get().getCart().isEmpty() ||
					FruitShopSession.get().getUsername().isEmpty()){
						setVisible(false);
					} else {
						setVisible(true);
					}
				
			}
		};
		add(payLink.setVisible(!FruitShopSession.get().getCart().isEmpty()));
		
		HiddenField paypal_custom = new HiddenField("paypal_custom", Model.of(FruitShopSession.get().getCurrentUser().getCustomerId()));
		paypal_custom.add(new AttributeModifier("name", "custom"));
		add(paypal_custom);
		
//		Paypal button setup
		ListView paypalCart = new ListView("paypalCart", new PropertyModel(this, "CartList")){
			int number = 1; 
			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				CartItem cartitem = (CartItem) item.getModelObject();
				HiddenField item_number = new HiddenField("item_number", Model.of(cartitem.getItem().getItemid()));
				item_number.add(new AttributeModifier("name", "item_number_" + number));
				HiddenField item_name = new HiddenField("item_name", Model.of(cartitem.getItem().getDescr()));
				item_name.add(new AttributeModifier("name", "item_name_" + number));
				HiddenField quantity = new HiddenField("quantity", Model.of(cartitem.getQuantity()));
				quantity.add(new AttributeModifier("name", "quantity_" + number));
				HiddenField amount = new HiddenField("amount", Model.of(cartitem.getItem().getPrice()));
				amount.add(new AttributeModifier("name", "amount_" + number));
				item.add(item_number);
				item.add(item_name);
				item.add(quantity);
				item.add(amount);
				number++;
			}
		};
		add(paypalCart);
		SubmitLink paypalBtn = new SubmitLink("paypalBtn"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				// TODO Auto-generated method stub
//				    if(writeOrder()){
//				    	FruitShopSession.get().getCart().clear();
//				    	CartList.clear();
//				    }
			}
			@Override
			protected void onBeforeRender(){
				super.onBeforeRender();
				if (FruitShopSession.get().getCart().isEmpty() ||
					FruitShopSession.get().getUsername().isEmpty()){
						setVisible(false);
					} else {
						setVisible(true);
					}
				
			}

		};
		add(paypalBtn.setVisible(!FruitShopSession.get().getCart().isEmpty()));
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void onBeforeRender(){
		super.onBeforeRender();
		addOrReplace(new Label("total", new Model(FruitShopSession.get().calcCart())));
		if (!FruitShopSession.get().getUsername().isEmpty()
				&& FruitShopSession.get().calcCart()>0){ 
			this.get("payLink").setVisible(true);
			this.get("paypalBtn").setVisible(true);
		} else {
			this.get("payLink").setVisible(false);
			this.get("paypalBtn").setVisible(false);
		}
	}
	
	private Boolean writeOrder(){
		Order NewOrder = new Order();
		ArrayList<OrderedItem> NewOrderedItems = new ArrayList<OrderedItem>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		/* Create detail records */
		Iterator CartIterator = FruitShopSession.get().getCart().iterator();
		while (CartIterator.hasNext()){
			OrderedItem OrderedItem = new OrderedItem();
			CartItem CartItem = new CartItem();
			CartItem = (CartItem) CartIterator.next();
			OrderedItem.setItemid(CartItem.getItem().getItemid());
			OrderedItem.setOrderid(0);
			OrderedItem.setPrice(CartItem.getItem().getPrice());
			OrderedItem.setQuantity(CartItem.getQuantity());
			NewOrderedItems.add(OrderedItem);
			System.out.print("Before call " + NewOrderedItems.size() + "\n");
		}
		/* Create Header records */
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		NewOrder.setAmmount(FruitShopSession.get().calcCart());
		NewOrder.setDate(date);
		NewOrder.setStatus(0);
		NewOrder.setTxn_id("PAY ON DELIVERY");
//		NewOrder.setUsername(FruitShopSession.get().getUsername());
		NewOrder.setUsername("");
		NewOrder.setCustid(FruitShopSession.get().getCurrentUser().getCustomerId());
		
		if (addNewOrder(NewOrder, NewOrderedItems)){
			String sbj = "WEB - New order from " + FruitShopSession.get().getUsername();
	    	StringBuilder msg = new StringBuilder();
	    	msg.append(NewOrder.toString() + "\n");
	    	for (OrderedItem item : NewOrderedItems){
	    		msg.append(item.toString() + "\n");
	    	}
	    	SendMail(System.getenv("HOTMAIL"), sbj, msg.toString());
	    	SendMail(FruitShopSession.get().getUsername(), "Your order have been placed", msg.toString());
			return true;
		} else {
			return false;
		}
	}
}
