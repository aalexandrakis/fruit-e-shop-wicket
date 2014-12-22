package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class MyCartPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private List<Cart_Item> CartList = new ArrayList<Cart_Item>(FruitShopSession.get().getCart()); 
    public MyCartPage(final PageParameters parameters) {
		super(parameters);
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		this.construct();
		// TODO Add your page's components here

	} 
	
	private void construct(){
		//removeAll();
		add(new Label("PageHeader", "Το καλάθι μου"));
		add(new Label("itemidHeader", "Κωδικός"));
		add(new Label("descrHeader", "Περιγραφή"));
		add(new Label("mmHeader", "Μον.Μέτρησης"));
		add(new Label("categoryHeader", "Κατηγορία"));
		add(new Label("priceHeader", "Τιμή"));
		add(new Label("quantityHeader", "Ποσότητα"));
		add(new Label("summaryHeader", "Αξία"));
		add(new Label("removeHeader", "Διαγραφή"));
		
		add(new ListView("cart", new PropertyModel(this, "CartList")) {
		@Override
		protected void populateItem(ListItem item) {
			Cart_Item cartitem = (Cart_Item) item.getModelObject();
			item.add(new Label("itemid", cartitem.getItemid()));
			item.add(new Label("descr", cartitem.getDescr()));
			item.add(new Label("mm", cartitem.getMm()));
			item.add(new Label("category", getCategory(cartitem.getCategoryid()).getCategory()));
			item.add(new Label("price",  cartitem.getPrice()));
			item.add(new Label("quantity", cartitem.getQuantity()));
			item.add(new Label("summary",  cartitem.getSummary()));
			//item.add(removeLink("remove", item));
			Link RemoveLink = new Link("remove", item.getModel()){
				@Override
				public void onClick() {
					// TODO Auto-generated method stub
					Cart_Item selected =  (Cart_Item) getModelObject();
					if(FruitShopSession.get().getCart().contains(selected)){
						FruitShopSession.get().getCart().remove(selected);
						CartList.remove(selected);
						//CartSummary = FruitShopSession.get().CalcCart();
					}
					
				}
			};
			item.add(RemoveLink);
			RemoveLink.add(new Image("removebutton", "buttons/deletefromcart.GIF"));
			}
		});
		add(new Label("TotalHeader", "Σύνολο"));
		add(new Label("total", new Model(FruitShopSession.get().CalcCart())));
		
		Link EmptyLink = new Link("EmptyLink"){
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
		add(EmptyLink);
		EmptyLink.add(new Image("EmptyButton", "buttons/emptycart.GIF"));
		
		
		Link PayLink = new Link("PayLink"){
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				    if(WriteOrder()){
				    	String sbj = "Νέα παραγγελία";
				    	String msg = "Νέα παραγγελία από τον χρήστη " + FruitShopSession.get().getUsername();
				    	SendMail(bussiness_email, sbj, msg);
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
		add(PayLink.setVisible(!FruitShopSession.get().getCart().isEmpty()));
		PayLink.add(new Image("PayButton", "buttons/paybutton.GIF"));
		
	}
	
	
	
	public void onBeforeRender(){
		super.onBeforeRender();
		addOrReplace(new Label("total", new Model(FruitShopSession.get().CalcCart())));
		if (!FruitShopSession.get().getUsername().isEmpty()
				&& FruitShopSession.get().CalcCart()>0){ 
			this.get("PayLink").setVisible(true);
		} else {
			this.get("PayLink").setVisible(false);
		}
	}
	
	private Boolean WriteOrder(){
		Orders NewOrder = new Orders();
		ArrayList<Ordered_Items> NewOrderedItems = new ArrayList<Ordered_Items>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		/* Create detail records */
		Iterator CartIterator = FruitShopSession.get().getCart().iterator();
		while (CartIterator.hasNext()){
			Ordered_Items OrderedItem = new Ordered_Items();
			Cart_Item CartItem = new Cart_Item();
			CartItem = (Cart_Item) CartIterator.next();
			OrderedItem.setItemid(CartItem.getItemid());
			OrderedItem.setOrderid(0);
			OrderedItem.setPrice(CartItem.getPrice());
			OrderedItem.setQuantity(CartItem.getQuantity());
			NewOrderedItems.add(OrderedItem);
			System.out.print("Before call " + NewOrderedItems.size() + "\n");
		}
		/* Create Header records */
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		NewOrder.setAmmount(FruitShopSession.get().CalcCart());
		NewOrder.setDate(date);
		NewOrder.setStatus(0);
		NewOrder.setTxn_id("");
		NewOrder.setUsername(FruitShopSession.get().getUsername());
		
		return AddNewOrder(NewOrder, NewOrderedItems);
	}
}
