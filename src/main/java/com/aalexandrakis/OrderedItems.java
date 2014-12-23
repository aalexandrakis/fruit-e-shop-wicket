package com.aalexandrakis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="ordereditems")
@XmlSeeAlso({OrderedItem.class})
public class OrderedItems {
	@XmlElement(name="ordereditem")
	List<OrderedItem> list = new ArrayList<OrderedItem>();
	
	public OrderedItems(){
		
	}
	public OrderedItems(List<OrderedItem> orderedItems){
		this.list = orderedItems;
	}
	public List<OrderedItem> getOrderedItems() {
		return list;
	}
	public void setOrderedItems(List<OrderedItem> orderedItems) {
		this.list = orderedItems;
	}
	
	
}
