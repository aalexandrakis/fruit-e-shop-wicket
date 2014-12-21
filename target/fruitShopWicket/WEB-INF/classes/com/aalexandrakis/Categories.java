package com.aalexandrakis;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="categories")
public class Categories {
	@XmlElement(name = "item_category")
	private List<Items_Category> categories;
	public Categories(){
		
	}
	public Categories(List<Items_Category> categories){
		this.categories = categories;
	}

	public void setCategories(List<Items_Category> categories){
		this.categories = categories;
	}
	
	public List<Items_Category> getCategorites(){
		return this.categories;
	}
}
