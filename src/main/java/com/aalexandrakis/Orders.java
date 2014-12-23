package com.aalexandrakis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="orders")
@XmlSeeAlso({Order.class})
public class Orders {
	@XmlElement(name="order")
	List<Order> list = new ArrayList<Order>();
	
	public Orders(){
		
	}
	public Orders(List<Order> orderList){
		this.list = orderList;
	}
	public List<Order> getOrderList() {
		return list;
	}
	public void setOrderList(List<Order> orderList) {
		this.list = orderList;
	}
	
	
}
