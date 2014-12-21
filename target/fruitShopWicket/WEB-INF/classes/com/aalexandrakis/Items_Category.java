package com.aalexandrakis;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item_category")
public class Items_Category implements java.io.Serializable{
	private Integer categoryid;
    private String category;
    
    public Items_Category() { }
    
    public Items_Category(Integer categoryid, String category) {
    	this.categoryid = categoryid;
    	this.category = category;
    	
    }
    
    public Integer getCategoryid(){
    	return this.categoryid;
    }
    public void setCategoryid(Integer categoryid){
    	this.categoryid = categoryid;
    }
    public String getCategory(){
    	return this.category;
    }
    public void setCategory(String category){
    	this.category = category;
    }
	}
