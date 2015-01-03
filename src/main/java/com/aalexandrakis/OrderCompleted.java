package com.aalexandrakis;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class OrderCompleted extends BasePage {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OrderCompleted(PageParameters parms) {
		super(parms);
		FruitShopSession.get().emptyCart();
	}
}