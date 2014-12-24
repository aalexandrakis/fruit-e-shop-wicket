package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;










import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.hibernate.mapping.Array;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	protected DateFormat dateFormatIso = new SimpleDateFormat("yyyy-MM-dd");
	public final String bussiness_email = "aalexandrakis@hotmail.com";
	
	public BasePage(final PageParameters parameters) {
		super(parameters);
		//this.FSSession = (FruitShopSession)getSession();
		//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		add (new LoginPanel("Login"));
		add (new Label("Header2", "Μπακάλικο"));
		add (new MyCartPanel("CartPanel"));
        add (new MenuPanel("Menu", getClass().toString()));
        
		// TODO Add your page's components here
    }
	
	public List<Items_Category> getCategories() {
		return WicketApplication.get().getCategories();
	}
	
	public List<Item> getItems(Integer CategoryId) {
		return WicketApplication.get().getItems(CategoryId);
	}
	
	public List<Item> getActiveItems(Integer CategoryId) {
		return WicketApplication.get().getActiveItems(CategoryId);
	}
	
	public Item getItem(Integer ItemId) {
		return WicketApplication.get().getItem(ItemId);
	}
	
	public Items_Category getCategory(Integer CategoryId) {
		return WicketApplication.get().getCategory(CategoryId);
	}
	public FruitShopSession getSession(){
		return FruitShopSession.get();
	}
   
	public Boolean addNewOrder(Order OrderHeader, ArrayList<OrderedItem> OrderDetails){
		    return WicketApplication.get().addNewOrder(OrderHeader, OrderDetails);
	}
	
	public List<Order> getOrdersFromUsername() {
		return WicketApplication.get().getOrdersFromUser();
	}
	
	public List<OrderedItem> getOrderDetails(Integer OrderId) {
		return WicketApplication.get().getOrderDetails(OrderId);
	}
	
	public Order getOrderFromId(Integer OrderId) {
		return WicketApplication.get().getOrderFromId(OrderId);
	}
	
	public Boolean resetPassword(String Username, String newPassword) {
		return WicketApplication.get().resetPassword(Username, newPassword);
	}
    
	public Boolean AddOrUpdateUser(Customer NewUser) {
		return WicketApplication.get().AddOrUpdateUser(NewUser);
	}
	
	public Boolean MailExists(String Username) {
		return WicketApplication.get().MailExists(Username);
	}
    
	public Boolean AddOrUpdateCategory(Items_Category ItmCat) {
		return WicketApplication.get().AddOrUpdateCategory(ItmCat);
	}
	
	public Boolean AddOrUpdateItem(Item CurItem) {
		return WicketApplication.get().AddOrUpdateItem(CurItem);
	}
	
	public List<Item> getAllItems(Integer CategoryId) {
		return WicketApplication.get().getAllItems(CategoryId);
	}
	
	public String getStatusDesc(Integer status){
		switch (status){
			case 0: return "Υπο επεξεργασία";
			case 1: return "Ετοιμάζεται";
			case 2: return "Έτοιμη για αποστολή";
			case 3: return "Έχει αποσταλεί";
			case 4: return "Παραδόθηκε";
			default: return "";
		}
		
	}
	
	public Boolean SendMail(String eml, String sbj, String msg){
		return WicketApplication.get().SendMail(eml, sbj, msg);
	}
	
	public List<Order> getAllActiveOrders() {
		return WicketApplication.get().getAllActiveOrders();
	}
	
	public Boolean UpdateOrder(Order Order) {
		return WicketApplication.get().UpdateOrder(Order);
	}
	
	public List<Customer> getCustomers() {
		return WicketApplication.get().getCustomers();
	}
}
