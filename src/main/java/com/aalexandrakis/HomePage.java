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
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
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
import org.eclipse.jetty.util.resource.FileResource;


public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;
    public Integer categoryId=0;
    public String photo="";
	private Class ItemClass = null;
    private List<Item> ItemsList = new ArrayList<Item>();
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
				ItemsList = getActiveItems(categoryId);
			} else {
				ItemsList = getAllItems(categoryId);
		}
		/*****************************************************/
        //add(new Label("CategoryId", CategoryId));
        ListView products
		= new ListView("products", ItemsList) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				Item CurrentItem = (Item) item.getModelObject();
					ItemClass = getItemClass();
					String photo="productimg/";
					//String photo=WicketApplication.get().ImagesPath + "\\productimg\\";
					if (CurrentItem.getPhoto().isEmpty()){
						photo += "nophoto.GIF";
					} else {
						photo += CurrentItem.getPhoto();
					}
					
					//Image ItemImage = new Image("ItemImage"){};
					//ItemImage.add(AttributeModifier.replace("src", photo));
					//item.add(ItemImage);
					Image itemImage = new Image("itemImage"){};
					itemImage.add(AttributeModifier.replace("src", photo));
									//new SharedResourceReference(HomePage.class, photo));
					item.add(itemImage);
					PageParameters parameters = new PageParameters();
					parameters.set("itemId", CurrentItem.getItemid());
					BookmarkablePageLink itemDetails = new BookmarkablePageLink("itemDetails",
							                           ItemClass, parameters);
					item.add(itemDetails);
					itemDetails.add(new Label("descr", CurrentItem.getDescr()));
					item.add(new Label("price", "€" + CurrentItem.getPrice().toString()));
					Link addToCart = new Link("addToCart", item.getModel()){
						@Override
						public void onClick(){
							Item SelectedItem = (Item) getModelObject();
							Cart_Item ItemToAdd = new Cart_Item();
							ItemToAdd.setCategoryid(SelectedItem.getCategoryid());
						    ItemToAdd.setItemid(SelectedItem.getItemid());
						    ItemToAdd.setDescr(SelectedItem.getDescr());
						    ItemToAdd.setMm(SelectedItem.getMm());
						    ItemToAdd.setPrice(SelectedItem.getPrice());
						    ItemToAdd.setQuantity(1.0f);
						    FruitShopSession.get().addToCart(ItemToAdd);
						    //System.out.print(FruitShopSession.get().CalcCart()+"\n");
						}
					};
					item.add(addToCart);
//					addToCart.add(new Image("AddToCartButton",
//							new SharedResourceReference(HomePage.class, "buttons/addtocart.GIF")));
					
				}
		};
        
	add(products);
//	add(new PagingNavigator("navigator", products));

    }
	@Override
	protected void onBeforeRender(){
		super.onBeforeRender();
		ItemClass = getItemClass();
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
