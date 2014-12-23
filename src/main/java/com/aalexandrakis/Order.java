package com.aalexandrakis;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="order")
public class Order implements java.io.Serializable{
	private Integer orderid;
	private Date date;
    private Float ammount;
    private String username;
    private Integer status;
    private String txn_id;
    private int custid;
    
    public Order() { }
    
    public Order(Integer orderid, Date date, Float ammount, String username,
    			Integer status, String txn_id) {
    	this.orderid = orderid;
    	this.date = date;
    	this.ammount = ammount;
    	this.username = username;
    	this.status = status;
    	this.txn_id = txn_id;
    }

    public Order(Integer orderid, Date date, Float ammount, String username,
    			Integer status, String txn_id, int custid) {
    	this.orderid = orderid;
    	this.date = date;
    	this.ammount = ammount;
    	this.username = username;
    	this.status = status;
    	this.txn_id = txn_id;
    	this.custid = custid;
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

	public int getCustid() {
		return custid;
	}

	public void setCustid(int custid) {
		this.custid = custid;
	}

	@Override
	public String toString() {
		return "Orders [orderid=" + orderid + ", date=" + date + ", ammount="
				+ ammount + ", username=" + username + ", status=" + status
				+ ", txn_id=" + txn_id + ", custid=" + custid + "]";
	}
	
}
