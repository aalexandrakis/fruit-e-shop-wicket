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
    private Item SelectedItem;
    Cart_Item ItemToAdd = new Cart_Item();
    

	public ItemDetailsPage(final PageParameters parameters) {
		super(parameters);
		//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		itemid = parameters.get("ItemId").toInteger();
		SelectedItem = getItem(itemid);
		
		ItemToAdd.setCategoryid(SelectedItem.getCategoryid());
	    ItemToAdd.setItemid(SelectedItem.getItemid());
	    ItemToAdd.setDescr(SelectedItem.getDescr());
	    ItemToAdd.setMm(SelectedItem.getMm());
	    ItemToAdd.setPrice(SelectedItem.getPrice());
	    
	    
		String photo = "/productimg/";
		if (SelectedItem.getPhoto().isEmpty()){
			photo += "nophoto.GIF";
		} else {
			photo += SelectedItem.getPhoto();
		}
        final Form BuyNow = new Form("BuyNow", new CompoundPropertyModel<Cart_Item>(ItemToAdd));
        
        add(BuyNow);
        //setDefaultModel(new CompoundPropertyModel(ItemToAdd));
        BuyNow.add(new FeedbackPanel("feedback"));
        BuyNow.add(new Image("ItemPhoto"){}.add(AttributeModifier.replace("src", photo)));
		BuyNow.add(new Label("ItemIdHeader", "Κωδικός"));
		BuyNow.add(new TextField("itemid"));
		BuyNow.add(new Label("ItemDescrHeader", "Περιγραφή"));
		BuyNow.add(new TextField("descr"));
		BuyNow.add(new Label("ItemMmHeader", "Μον.Μέτρησης"));
		BuyNow.add(new TextField("mm"));
		BuyNow.add(new Label("ItemCatHeader", "Κατηγορία"));
		BuyNow.add(new TextField("ItemCat", new Model(getCategory(SelectedItem.getCategoryid()).getCategory())));
		BuyNow.add(new Label("ItemPriceHeader", "Τιμή"));
		BuyNow.add(new NumberTextField("price"));
		BuyNow.add(new Label("QuantityHeader", "Ποσότητα"));
		BuyNow.add(new NumberTextField("quantity").setRequired(true));
		SubmitLink AddToCart = new SubmitLink("AddToCart"){
			@Override
			public void onSubmit(){
				//System.out.print(SelectedItem.getItemid());
				//ItemToAdd.setQuantity(1.0f);
				FruitShopSession.get().addToCart(ItemToAdd);
				System.out.print(FruitShopSession.get().CalcCart());
				PageParameters parameters = new PageParameters();
				parameters.set("CategoryId", ItemToAdd.getCategoryid());
				setResponsePage(HomePage.class, parameters);
			}
		};
        AddToCart.add(new Image("AddToCartButton", "buttons/addtocart.GIF"));
        BuyNow.add(AddToCart);
		// TODO Add your page's components here

    }
	
	}
