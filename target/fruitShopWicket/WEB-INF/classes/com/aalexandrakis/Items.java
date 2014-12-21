package com.aalexandrakis;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="items")
@XmlSeeAlso({Item.class})
public class Items {
	@XmlElement(name = "item")
	private List<Items> items;
	public Items(){
		
	
	}
	public Items(List<Items> items){
		this.items = items;
	}

	public void setCategories(List<Items> items){
		this.items = items;
	}
	
	public List<Items> getCategorites(){
		return this.items;
	}
}
