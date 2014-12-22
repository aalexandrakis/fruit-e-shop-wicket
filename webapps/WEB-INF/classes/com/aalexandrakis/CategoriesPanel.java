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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CategoriesPanel extends Panel {
    private Integer CategoryId; 
    //private PageParameters parameters = new PageParameters();
    private Class classname;
    

    public CategoriesPanel (String id, Class classname, Integer CategoryId) {
		super(id);
		this.classname=classname;
		this.CategoryId = CategoryId;
		this.construct();
        
	}
	
	private void construct(){
		//parameters.set("PageName", "PageName");
		removeAll();
		add(new Label("CategoryHeader", "Κατηγορίες"));
        ListView<Items_Category> Categories = new ListView("Categories", WicketApplication.get().getCategories()){
			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
			   Items_Category category = (Items_Category) item.getModelObject();
               PageParameters parameters = new PageParameters();
               parameters.set("CategoryId", category.getCategoryid());
               BookmarkablePageLink link = new BookmarkablePageLink("CategoryLink",  classname , parameters);
			   item.add(link);
               link.add(new Label("CategoryName", category.getCategory()));
               Label label = new  Label("CategoryNameText", category.getCategory()); 
               item.add(label);
               link.setVisible(!category.getCategoryid().equals(CategoryId));
               label.setVisible(category.getCategoryid().equals(CategoryId));	
			}
        	
        };
        //if (parameters.getNamedKeys().contains("CategoryId")) {
		// 	   this.CategoryId=parameters.get("CategoryId").toInteger();
        //}

        add(Categories);

	}
	
	public Integer  getCategoryId(){
		return this.CategoryId;
	}
	
	@Override
	protected void onBeforeRender(){
		super.onBeforeRender();
		this.construct();
	}
}