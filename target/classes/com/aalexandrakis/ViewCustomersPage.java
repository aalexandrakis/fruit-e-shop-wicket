package com.aalexandrakis;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

public class ViewCustomersPage extends BasePage {
	private static final long serialVersionUID = 1L;
    private List<Customer> Users = new ArrayList<Customer>();
	
	public ViewCustomersPage(final PageParameters parameters) {
		super(parameters);
		Users = getCustomers();
		add(new Label("custidHeader", "Κωδικός"));
		add(new Label("nameHeader", "Ονοματεπώνυμο"));
		add(new Label("addressHeader", "Διεύθυνση"));
		add(new Label("cityHeader", "Πόλη/Περιοχή"));
		add(new Label("phoneHeader", "Τηλέφωνο"));
		add(new Label("emailHeader", "Email"));
        //add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        ListView CustList = new ListView("users", new CompoundPropertyModel(Users)){

			@Override
			protected void populateItem(ListItem item) {
				Customer user =  (Customer) item.getModelObject();
				// TODO Auto-generated method stub
				item.add(new Label("customerid", user.getCustomerId()));
				item.add(new Label("name", user.getName()));
				item.add(new Label("address", user.getAddress()));
				item.add(new Label("city", user.getCity()));
				item.add(new Label("phone", user.getPhone()));
				item.add(new Label("email", user.getEmail()));
				
			}
        	
        };
        add(CustList);
		// TODO Add your page's components here

    }
}
