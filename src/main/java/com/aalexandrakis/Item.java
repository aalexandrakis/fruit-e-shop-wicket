package com.aalexandrakis;

public class Item implements java.io.Serializable{
	private Integer itemid;
    private String descr;
    private String mm;
    private Float price;
    private Integer categoryid;
    private Integer display;
    private String photo;
    private Integer lastupdate;
    
    public Item() { }
    
    public Item(Integer itemid, String descr, String mm, Float price,
    				Integer categoryid, Integer display, String photo, Integer lastupdate){
    	this.itemid = itemid;
    	this.descr = descr;
    	this.mm = mm;
    	this.price = price;
    	this.categoryid = categoryid;
    	this.display = display;
    	this.photo = photo;
    	this.lastupdate = lastupdate;
    	
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
	public String getPhoto(){
		return this.photo;
	}
	public void setPhoto(String photo){
		this.photo = photo;
	}
	public Integer getDisplay(){
		return this.display;
	}
	public void setDisplay(Integer display){
		this.display = display;
	}
	
    public Integer getLastupdate(){
    	return this.lastupdate;
    }
    public void setLastupdate(Integer lastupdate){
    	this.lastupdate = lastupdate;
    }
}
