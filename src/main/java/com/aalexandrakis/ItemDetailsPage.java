package com.aalexandrakis;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class ItemDetailsPage extends BasePage {
	private static final long serialVersionUID = 1L;
    private Integer itemid;
    private Item selectedItem;
    CartItem itemToAdd = new CartItem();
    FeedbackPanel errorMessages = new FeedbackPanel("errorMessages");

	public ItemDetailsPage(final PageParameters parameters) {
		super(parameters);
		itemid = parameters.get("itemId").toInteger();
		selectedItem = getItem(itemid);
		itemToAdd.getItem().setCategoryid(selectedItem.getCategoryid());
	    itemToAdd.getItem().setItemid(selectedItem.getItemid());
	    itemToAdd.getItem().setDescr(selectedItem.getDescr());
	    itemToAdd.getItem().setMm(selectedItem.getMm());
	    itemToAdd.getItem().setPrice(selectedItem.getPrice());
	    
		String photo = "productimg/";
		if (selectedItem.getPhoto().isEmpty()){
			photo += "nophoto.jpg";
		} else {
			photo += selectedItem.getPhoto();
		}
        final Form buyNow = new Form("BuyNow", new CompoundPropertyModel<CartItem>(itemToAdd));
        add(errorMessages);
        errorMessages.setVisible(errorMessages.anyMessage());
        Image itemPhoto = new Image("itemPhoto"){};
		itemPhoto.add(AttributeModifier.replace("src", photo));
        add(itemPhoto);
        add(buyNow);
        buyNow.add(new Label("ItemIdHeader", "Item Code"));
		buyNow.add(new TextField("item.itemid"));
		buyNow.add(new Label("ItemDescrHeader", "Description"));
		buyNow.add(new TextField("item.descr"));
		buyNow.add(new Label("ItemMmHeader", "Metric unit"));
		buyNow.add(new TextField("item.mm"));
		buyNow.add(new Label("ItemCatHeader", "Category"));
		buyNow.add(new TextField("ItemCat", new Model(getCategory(selectedItem.getCategoryid()).getCategory())));
		buyNow.add(new Label("ItemPriceHeader", "Price"));
		buyNow.add(new NumberTextField("item.price"));
		buyNow.add(new Label("QuantityHeader", "Quantity"));
		buyNow.add(new NumberTextField("quantity").setRequired(true));
		SubmitLink AddToCart = new SubmitLink("AddToCart"){
			@Override
			public void onSubmit(){
				//System.out.print(SelectedItem.getItemid());
				//ItemToAdd.setQuantity(1.0f);
				FruitShopSession.get().addToCart(itemToAdd);
				System.out.print(FruitShopSession.get().calcCart());
				PageParameters parameters = new PageParameters();
				parameters.set("CategoryId", itemToAdd.getItem().getCategoryid());
				setResponsePage(HomePage.class, parameters);
			}
		};
        buyNow.add(AddToCart);
		// TODO Add your page's components here

    }


	@Override
	protected void onBeforeRender() {
		// TODO Auto-generated method stub
		super.onBeforeRender();
 		errorMessages.setVisible(errorMessages.anyMessage());
	}
	
	
	}
