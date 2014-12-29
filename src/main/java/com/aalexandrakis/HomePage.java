package com.aalexandrakis;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.eclipse.jetty.util.resource.FileResource;


public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;
    public Integer categoryId=0;
    public String photo="";
	private Class itemClass = null;
    private List<CartItem> itemsList = new ArrayList<CartItem>();
    public HomePage(){
    	super();
    	categoryId=WicketApplication.get().getCategories().get(0).getCategoryid();
    	homePageInitialization();
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public HomePage(final PageParameters parameters) {
		super(parameters);
		if (parameters.getNamedKeys().contains("categoryId")){
			categoryId = parameters.get("categoryId").toInteger();
		} else {
			categoryId=WicketApplication.get().getCategories().get(0).getCategoryid();
		}
		homePageInitialization();
    }
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void homePageInitialization(){
		/*****************************************************/
		if(FruitShopSession.get().getIsAdmin()==null
				|| FruitShopSession.get().getIsAdmin()==false){
				itemsList = getActiveItems(categoryId);
			} else {
				itemsList = getAllItems(categoryId);
		}
		/*****************************************************/
        //add(new Label("CategoryId", CategoryId));
        ListView products
		= new ListView("products", itemsList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final CartItem currentItem = (CartItem) item.getModelObject();
					
					itemClass = getItemClass();
					String photo="productimg/";
					//String photo=WicketApplication.get().ImagesPath + "\\productimg\\";
					if (currentItem.getItem().getPhoto().isEmpty()){
						photo += "nophoto.GIF";
					} else {
						photo += currentItem.getItem().getPhoto();
					}
					Image itemImage = new Image("itemImage"){};
					itemImage.add(AttributeModifier.replace("src", photo));
									//new SharedResourceReference(HomePage.class, photo));
					//
					currentItem.setQuantity(FruitShopSession.getItemQuantity(currentItem.getItem().getItemid(), 1.0f));
					//
					final TextField txtQuantity = new TextField("quantity", new PropertyModel(currentItem, "quantity"));
					//
					PageParameters parameters = new PageParameters();
					parameters.set("itemId", currentItem.getItem().getItemid());
					BookmarkablePageLink itemDetails = new BookmarkablePageLink("itemDetails",
							                           itemClass, parameters);
					//
					itemDetails.add(new Label("descr", currentItem.getItem().getDescr()));
					//
					Label labelPrice = new Label("price", "€" + currentItem.getItem().getPrice().toString());
					//
					AjaxSubmitLink addToCart= new AjaxSubmitLink("addToCart"){
						@Override
						public void onSubmit(AjaxRequestTarget target, Form<?> form){	
						}
					};
					
					Form form = new Form("form"){
						@Override
						protected void onValidate() {
							// TODO Auto-generated method stub
							super.onValidate();
							currentItem.setQuantity(Float.valueOf(txtQuantity.getValue()));
							FruitShopSession.get().addToCart(currentItem);
						}
					};
					item.add(itemImage);
					form.add(txtQuantity);
					form.add(itemDetails);
					form.add(labelPrice);
					form.add(addToCart);
					item.add(form);
				}
		};
        
	add(products);
//	add(new PagingNavigator("navigator", products));

    }
	@Override
	protected void onBeforeRender(){
		super.onBeforeRender();
		itemClass = getItemClass();
		//this.CategoryId = CatPanel.getCategoryId();
	
	}
	
	
	protected Class getItemClass(){
		if (FruitShopSession.get().getIsAdmin()!=null && 
				FruitShopSession.get().getIsAdmin()){
					return AddUpdateItemPage.class;
			} else {
					return ItemDetailsPage.class;
			}
	}
	
}
