package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class OrdersPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private List<Order> ListOrders = new ArrayList<Order>();
	private Boolean isAdmin;
	
	public OrdersPage(final PageParameters parameters) {
		super(parameters);
		isAdmin = FruitShopSession.get().getIsAdmin();
		
		if (!isAdmin && 
				FruitShopSession.get().getUsername().isEmpty()){
			setResponsePage(HomePage.class);
		}
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		construct();
		// TODO Add your page's components here

    }
	
	
	@SuppressWarnings("unchecked")
	private void construct(){
		if (isAdmin){
				add(new Label("PageHeader", "Παραγγελίες Πελατών"));
				ListOrders = getAllActiveOrders();
		} else {
				add(new Label("PageHeader", "Οι παραγγελίες μου"));
				ListOrders = getOrdersFromUsername();
		}
		add(new Label("orderidHeader", "Item code"));
		add(new Label("orderdateHeader", "Order Date"));
		add(new Label("amountHeader", "Amount"));
		add(new Label("statusHeader", "Status"));
		add(new Label("emailHeader", "User").setVisible(isAdmin));
		add(new Label("paypalHeader", "Paypal Id").setVisible(isAdmin));
		add(new Label("detailsHeader", "Details"));
		
		add(new ListView("orders", new PropertyModel(this, "ListOrders")) {
		/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		@Override
		protected void populateItem(ListItem item) {
			Order order = (Order) item.getModelObject();
			item.add(new Label("orderid", order.getOrderid()));
			item.add(new Label("date", dateFormatIso.format(order.getDate())));
			item.add(new Label("ammount", order.getAmmount()));
			item.add(new Label("statusDesc", getStatusDesc(order.getStatus())));
			item.add(new Label("email", order.getUsername()).setVisible(isAdmin));
			item.add(new Label("txn_id", order.getTxn_id()).setVisible(isAdmin));
			//item.add(removeLink("remove", item));
			PageParameters parameters = new PageParameters();
			parameters.set("orderid", order.getOrderid());
			BookmarkablePageLink DetailsLink = 
								new BookmarkablePageLink("details", OrderDetailsPage.class, parameters);
			item.add(DetailsLink);
//			DetailsLink.add(new Label("detailHeader", "Λεπτομέριες"));
			
			}
		});
				
	}
}
