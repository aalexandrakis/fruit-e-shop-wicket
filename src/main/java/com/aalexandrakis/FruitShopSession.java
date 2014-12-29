package com.aalexandrakis;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class FruitShopSession extends WebSession {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username="";
	private Boolean isAdmin=false;
	private static ArrayList<CartItem> Cart = new ArrayList<CartItem>();
	private Customer CurrentUser = new Customer();
	
	
	public FruitShopSession(Request request) {
		super(request);
	}
	public String getUsername(){
		return this.username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	
	public Boolean getIsAdmin(){
		return this.isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin){
		this.isAdmin = isAdmin;
	}
	
	public Customer  getCurrentUser(){
		return this.CurrentUser;
	}
	
	public void  setCurrentUser(Customer User){
		this.CurrentUser = User;
	}
	
	public ArrayList<CartItem> getCart(){
		return this.Cart;
	}
	
	public void emptyCart(){
		this.Cart.clear();
	}
	
	public void addToCart(CartItem newItem){
		Boolean exists = false;
		for (CartItem item : Cart){
			if (item.getItem().getItemid().equals(newItem.getItem().getItemid())){
				item.setQuantity(newItem.getQuantity());
				exists = true;
			}
		}
		
		if (!exists){
			Cart.add(newItem);
		}
		
	}
	
	public void removeFromCart(CartItem newItem){
		this.Cart.remove(newItem);
	}
	
	public Float calcCart(){
		Float Summary = 0.0f;
		Iterator CartIterator = this.Cart.iterator();
		while(CartIterator.hasNext()){
			CartItem CurrentItem = (CartItem) CartIterator.next();
			Summary += CurrentItem.getMcGross();
		}
		return Summary;
	}
	
	// if you use java >= 1.5 you can make use of covariant return types    
	public static FruitShopSession get() {        
		return (FruitShopSession)Session.get();     
	} 
	
	public static Float getItemQuantity(int itemId, Float valueIfNotExists){
		for (CartItem item : Cart){
			if (item.getItem().getItemid().equals(itemId)){
				return item.getQuantity();
			}
		}
		return valueIfNotExists;
	}
}













