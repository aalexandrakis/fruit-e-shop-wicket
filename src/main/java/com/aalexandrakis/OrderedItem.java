package com.aalexandrakis;

public class OrderedItem implements java.io.Serializable{
	private Integer orderid;
	private Integer itemid;
    private Float quantity;
    private Float price;
    private Float amount;
    private String descr;
    private String mm;
   
	public OrderedItem() { }
    
    public OrderedItem(Integer orderid, Integer itemid, Float quantity, Float price) {
    	this.orderid = orderid;
    	this.itemid = itemid;
    	this.quantity = quantity;
    	this.price = price;
    	
    }

    @Override
	public String toString() {
		return "Ordered_Items [orderid=" + orderid + ", itemid=" + itemid
				+ ", quantity=" + quantity + ", price=" + price + ", amount="
				+ amount + "]";
	}

	public OrderedItem(Integer orderid, Integer itemid, Float quantity, Float price, Float amount) {
    	this.orderid = orderid;
    	this.itemid = itemid;
    	this.quantity = quantity;
    	this.price = price;
    	this.amount = amount;
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

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
    
	 public String getDescr() {
			return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}	
}
