package com.aalexandrakis;

import java.util.Date;

public class Orders implements java.io.Serializable{
	private Integer orderid;
	private Date date;
    private Float ammount;
    private String username;
    private Integer status;
    private String txn_id;
    
    public Orders() { }
    
    public Orders(Integer orderid, Date date, Float ammount, String username,
    			Integer status, String txn_id) {
    	this.orderid = orderid;
    	this.date = date;
    	this.ammount = ammount;
    	this.username = username;
    	this.status = status;
    	this.txn_id = txn_id;
    	
    }
    
    public Integer getOrderid(){
    	return this.orderid;
    }
    public void setOrderid(Integer orderid){
    	this.orderid = orderid;
    }
    public Date getDate(){
    	return this.date;
    }
    public void setDate(Date date){
    	this.date = date;
    }
    public Float getAmmount(){
    	return this.ammount;
    }
    public void setAmmount(Float ammount){
    	this.ammount = ammount;
    }
    public String getUsername(){
    	return this.username;
    }
    public void setUsername(String username){
    	this.username = username;
    }
    public Integer getStatus(){
    	return this.status;
    }
    public void setStatus(Integer status){
    	this.status = status;
    }
    public String getTxn_id(){
    	return this.txn_id;
    }
    public void setTxn_id(String txn_id){
    	this.txn_id = txn_id;
    }
}
