package com.aalexandrakis;

public class Customer implements java.io.Serializable{
	private Integer customerid;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String password;
    private String email;
    private Integer admin;
    private Integer insertdate;
    
    public Customer() { }
    
    public Customer(Integer customerid, String name, String address, String city,
    				String phone, String password, String email, Integer admin, Integer insertdate){
    	this.customerid = customerid;
    	this.name = name;
    	this.address = address;
    	this.city = city;
    	this.phone = phone;
    	this.password = password;
    	this.email = email;
    	this.admin = admin;
    	this.insertdate = insertdate;
    	
    }
    
    public Integer getCustomerId(){
    	return this.customerid;
    }
    public void setCustomerid(Integer customerid){
    	this.customerid = customerid;
    }
    public String getName(){
    	return this.name;
    }
    public void setName(String name){
    	this.name = name;
    }
	public String getAddress(){
		return this.address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public String getCity(){
    	return this.city;
    }
    public void setCity(String city){
    	this.city = city;
    }
    public String getPhone(){
    	return this.phone;
    }
    public void setPhone(String phone){
    	this.phone = phone;
    }
	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getPassword(){
		return this.password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public Integer getAdmin(){
    	return this.admin;
    }
    public void setAdmin(Integer admin){
    	this.admin = admin;
    }
    public Integer getInsertdate(){
    	return this.insertdate;
    }
    public void setInsertdate(Integer insertdate){
    	this.insertdate = insertdate;
    }
}
