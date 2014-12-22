package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class CategoriesPage extends BasePage {
	private static final long serialVersionUID = 1L;
	public Integer CategoryId=0;
	Items_Category ItmCat = new Items_Category();
	
	public CategoriesPage(final PageParameters parameters) {
		super(parameters);
		if (FruitShopSession.get().getIsAdmin()==null ||
			!FruitShopSession.get().getIsAdmin()){
			setResponsePage(HomePage.class);
		}
		// TODO Add your page's components here
		if (parameters.getNamedKeys().contains("CategoryId")){
			CategoryId = parameters.get("CategoryId").toInteger();
		} else {
			CategoryId=0;
		}
		CategoriesPanel CatPanel = new CategoriesPanel("CategoriesPanel", getClass(), CategoryId);
		add (CatPanel);
		Button AddNewButton = new Button("AddNewButton", new Model("Νέα Κατηγορία")){
			@Override
			public void onSubmit(){
				ItmCat=null;
				setResponsePage(CategoriesPage.class);
			}
		};
		add (AddNewButton);
		
		ItmCat = getCategory(CategoryId);
		Form CategoryForm = new Form("CategoryForm", new CompoundPropertyModel<Items_Category>(ItmCat));
		add (CategoryForm);
		CategoryForm.add (new TextField("categoryid"));
		CategoryForm.add (new TextField("category").setRequired(true));
        Button AddOrUpdate =new Button("AddOrUpdate"){
        	@Override
        	public void onSubmit(){
        		if(!AddOrUpdateCategory(ItmCat)){
        			error("Η εγγραφή δεν ολοκληρώθηκε. Παρακαλώ προσπαθήστε αργότερα.");
        		} else {
        			setResponsePage(CategoriesPage.class);
        		}
        	}
        };
        if(!parameters.getNamedKeys().contains("CategoryId")){
        	AddOrUpdate.add(AttributeModifier.append("value", "Προσθήκη"));
        } else {
        	AddOrUpdate.add(AttributeModifier.append("value", "Διόρθωση"));
        }
        CategoryForm.add(AddOrUpdate);

    }
}
