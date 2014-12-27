package com.aalexandrakis;

public class Cart_Item implements java.io.Serializable{
	private Integer itemid;
    private String descr;
    private String mm;
    private Float price;
    private Integer categoryid;
    private Float quantity;
    
    public Cart_Item() { }
    
    public Cart_Item(Integer itemid, String descr, String mm, Float price,
    				Integer categoryid, Float quantity){
    	this.itemid = itemid;
    	this.descr = descr;
    	this.mm = mm;
    	this.price = price;
    	this.categoryid = categoryid;
    	this.quantity = quantity;
    	
    }
    
    public Integer getItemid(){
    	return this.itemid;
    }
    public void setItemid(Integer itemid){
    	this.itemid = itemid;
    }
    public String getDescr(){
    	return this.descr;
    }
    public void setDescr(String descr){
    	this.descr = descr;
    }
	public String getMm(){
		return this.mm;
	}
	public void setMm(String mm){
		this.mm = mm;
	}
	public Float getPrice(){
    	return this.price;
    }
    public void setPrice(Float price){
    	this.price = price;
    }
    public Integer getCategoryid(){
    	return this.categoryid;
    }
    public void setCategoryid(Integer categoryid){
    	this.categoryid = categoryid;
    }
	public Float getQuantity(){
    	return this.quantity;
    }
    public void setQuantity(Float quantity){
    	this.quantity = quantity;
    }

    public Float getSummary(){
    	return this.quantity * this.price;
    }

}
