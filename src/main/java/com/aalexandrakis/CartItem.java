package com.aalexandrakis;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cartitem")
public class CartItem implements java.io.Serializable{
	private Item item;
    private float quantity;
    private float mcGross;
    
    public CartItem() { }
    
    public CartItem(Item item, float quantity, float mcGross){
    	this.item = item;
    	this.quantity = quantity;
    	this.mcGross = mcGross;
    }

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getMcGross() {
		return mcGross;
	}

	public void setMcGross(float mcGross) {
		this.mcGross = mcGross;
	}
    

}