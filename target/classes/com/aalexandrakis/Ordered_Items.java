package com.aalexandrakis;

public class Ordered_Items implements java.io.Serializable{
	private Integer orderid;
	private Integer itemid;
    private Float quantity;
    private Float price;
    
    public Ordered_Items() { }
    
    public Ordered_Items(Integer orderid, Integer itemid, Float quantity, Float price) {
    	this.orderid = orderid;
    	this.itemid = itemid;
    	this.quantity = quantity;
    	this.price = price;
    	
    }
    
    public Integer getOrderid(){
    	return this.orderid;
    }
    public void setOrderid(Integer orderid){
    	this.orderid = orderid;
    }
    public Integer getItemid(){
    	return this.itemid;
    }
    public void setItemid(Integer itemid){
    	this.itemid = itemid;
    }
    public Float getQuantity(){
    	return this.quantity;
    }
    public void setQuantity(Float quantity){
    	this.quantity = quantity;
    }
    public Float getPrice(){
    	return this.price;
    }
    public void setPrice(Float price){
    	this.price = price;
    }
}
