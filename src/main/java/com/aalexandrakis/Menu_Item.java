package com.aalexandrakis;

public class Menu_Item implements java.io.Serializable{
	private String Caption;
    private Class Destination;
    
    public Menu_Item(String Caption, Class Destination) {
    	this.Caption = Caption;
    	this.Destination = Destination;
    	
    }
    
    public String getCaption(){
    	return this.Caption;
    }
    public void setCaption(String Caption){
    	this.Caption = Caption;
    }
    public Class getDestination(){
    	return this.Destination;
    }
    public void setDestination(Class Destination){
    	this.Destination = Destination;
    }
	}
