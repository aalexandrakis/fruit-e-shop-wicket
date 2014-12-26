package com.aalexandrakis;


import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
* Panel for displaying the contents of a shopping cart. The cart
* shows the entries and the total value of the cart. Each item
* can be removed by the user.
*/
public class MenuPanel extends Panel {
    private List<Menu_Item> buttons = new ArrayList<Menu_Item>();
    private PageParameters parameters = new PageParameters();
    private String classname;
    

	public MenuPanel (String id, String classname) {
		super(id);
		this.classname=classname;
        this.construct();
        
	}
	
	private void construct(){
		this.FillMenu();
        //parameters.set("PageName", "PageName");
		removeAll();
		RepeatingView rv = new RepeatingView("Menu");
		add(rv);
		Iterator ButtonsIterator = buttons.iterator();
		while(ButtonsIterator.hasNext()){
			Menu_Item mnuItem =  (Menu_Item) ButtonsIterator.next();
			String Destination = mnuItem.getDestination().toString();
			Destination.replace(".class", "");
			WebMarkupContainer parent =
					new WebMarkupContainer(rv.newChildId());
			rv.add(parent);
			BookmarkablePageLink link =
					new BookmarkablePageLink("Link", mnuItem.getDestination());
			parent.add(link);
			link.add(new Label("Caption",  mnuItem.getCaption()));
			link.setVisible(!classname.contains(Destination));
			Label label = new Label("CaptionText", mnuItem.getCaption());
			parent.add(label);
			label.setVisible(classname.contains(Destination));
			
			
			
		}
	}
	
	private void FillMenu() {
		buttons.clear();
		if (FruitShopSession.get().getIsAdmin()!= null && FruitShopSession.get().getIsAdmin()){
	    	buttons.add( new Menu_Item("Προιόντα", HomePage.class));
	    	buttons.add( new Menu_Item("Κατηγορίες", CategoriesPage.class));
	    	buttons.add( new Menu_Item("Νέο Προϊόν", AddUpdateItemPage.class));
	    	buttons.add( new Menu_Item("Ανεβαστε Φωτογταφίες", PhotoUploadPage.class));
	    	buttons.add( new Menu_Item("Παραγγελίες", OrdersPage.class));
	    	buttons.add( new Menu_Item("Πελάτες", ViewCustomersPage.class));
	    	buttons.add( new Menu_Item("Αλλαγή Στοιχείων", AddUpdateUserPage.class));
	    } else if(!FruitShopSession.get().getUsername().isEmpty()){
	    	buttons.add( new Menu_Item("Προιόντα", HomePage.class));
	    	buttons.add( new Menu_Item("Ποιοι είμαστε", AboutUsPage.class));
	    	buttons.add( new Menu_Item("Που είμαστε", WherePage.class));
	    	buttons.add( new Menu_Item("Φωτογραφίες", PhotosPage.class));
	    	buttons.add( new Menu_Item("Επικοινωνία", ContactUsPage.class));
	    	buttons.add( new Menu_Item("Το καλάθι μου", MyCartPage.class));
	    	buttons.add( new Menu_Item("Οι παραγγελίες μου", OrdersPage.class));
	    	buttons.add( new Menu_Item("Αλλάγη Στοιχείων", AddUpdateUserPage.class));
	    } else {
	    	buttons.add( new Menu_Item("Προιόντα", HomePage.class));
	    	buttons.add( new Menu_Item("Ποιοι είμαστε", AboutUsPage.class));
	    	buttons.add( new Menu_Item("Που είμαστε", WherePage.class));
	    	buttons.add( new Menu_Item("Φωτογραφίες", PhotosPage.class));
	    	buttons.add( new Menu_Item("Επικοινωνία", ContactUsPage.class));
	    	buttons.add( new Menu_Item("Το καλάθι μου", MyCartPage.class));
	    	buttons.add( new Menu_Item("Νέος Χρήστης", AddUpdateUserPage.class));
	    }
	}
	
	@Override
	protected void onBeforeRender(){
		super.onBeforeRender();
		this.construct();
	}
}