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
	private ArrayList<Cart_Item> Cart = new ArrayList<Cart_Item>();
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
	
	public ArrayList<Cart_Item> getCart(){
		return this.Cart;
	}
	
	public void emptyCart(){
		this.Cart.clear();
	}
	
	public void addToCart(Cart_Item NewItem){
		Iterator CartIterator = this.Cart.iterator();
		Boolean exists = false;
		while(CartIterator.hasNext()){
			Cart_Item CurrentItem = (Cart_Item) CartIterator.next();
			if (CurrentItem.getItemid().equals(NewItem.getItemid())){
				CurrentItem.setPrice(NewItem.getPrice());
				CurrentItem.setQuantity(NewItem.getQuantity());
				exists = true;
			}
		}
		if (!exists){
			this.Cart.add(NewItem);
		}
		
	}
	
	public void removeFromCart(Cart_Item NewItem){
		this.Cart.remove(NewItem);
	}
	
	public Float CalcCart(){
		Float Summary = 0.0f;
		Iterator CartIterator = this.Cart.iterator();
		while(CartIterator.hasNext()){
			Cart_Item CurrentItem = (Cart_Item) CartIterator.next();
			Summary += CurrentItem.getPrice() * CurrentItem.getQuantity();
		}
		return Summary;
	}
	
	// if you use java >= 1.5 you can make use of covariant return types    
	public static FruitShopSession get() {        
		return (FruitShopSession)Session.get();     
	} 
	
	
}













