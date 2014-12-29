package com.aalexandrakis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	protected DateFormat dateFormatIso = new SimpleDateFormat("yyyy-MM-dd");
	public final String bussiness_email = System.getenv("HOTMAIL");
	public boolean isLoggedIn;
	int cartItems = 0;
	Label cartItemsCount = new Label("cartItemsCount", Model.of(cartItems));
	ListView<CartItem> cartList;
	Label emptyCart = new Label("emptyCart", "Your cart is empty");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	BookmarkablePageLink myCartLink = new BookmarkablePageLink("myCartLink",
			MyCartPage.class, new PageParameters());
	@SuppressWarnings({ "rawtypes", "unchecked" })
	BookmarkablePageLink myOrdersLink = new BookmarkablePageLink("myOrdersLink",
			OrdersPage.class, new PageParameters());
	
	public BasePage(){
		super();
		pageInitialization();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BasePage(final PageParameters parameters) {
		super(parameters);
		pageInitialization();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void pageInitialization(){
		isLoggedIn = !FruitShopSession.get().getUsername().isEmpty();
		BookmarkablePageLink aboutUsLink = new BookmarkablePageLink("aboutUsLink",
				AboutUsPage.class, new PageParameters());
		aboutUsLink.setOutputMarkupId(true);
		add(aboutUsLink);
//		
		BookmarkablePageLink whereWeAreLink = new BookmarkablePageLink("whereWeAreLink",
				WherePage.class, new PageParameters());
		whereWeAreLink.setOutputMarkupId(true);
		add(whereWeAreLink);
//		
		BookmarkablePageLink contactUsLink = new BookmarkablePageLink("contactUsLink",
				ContactUsPage.class, new PageParameters());
		contactUsLink.setOutputMarkupId(true);
		add(contactUsLink);
//		
		myOrdersLink.setOutputMarkupId(true);
		myOrdersLink.setVisible(!FruitShopSession.get().getUsername().isEmpty());
		add(myOrdersLink);
//		
		LoadableDetachableModel<ArrayList<CartItem>> cartModel = new LoadableDetachableModel<ArrayList<CartItem>>() {

			@Override
			protected ArrayList<CartItem> load() {
				// TODO Auto-generated method stub
				return FruitShopSession.get().getCart();
			}
			
		};
		
		myCartLink.setOutputMarkupId(true);
		add(myCartLink);
		myCartLink.setVisible(cartItems > 0);
		
		refreshCartItems();
		cartItemsCount.setVisible(cartItems > 0);
		add(cartItemsCount);
		cartList = new ListView<CartItem>("cartList", cartModel){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<CartItem> item) {
				// TODO Auto-generated method stub
				CartItem cartItem = (CartItem) item.getModelObject();
				Label itemDescr = new Label("itemDescr", Model.of(cartItem.getItem().getDescr()));
				Label itemQuantity = new Label("itemQuantity", Model.of(cartItem.getQuantity()));
				item.add(itemDescr);
				item.add(itemQuantity);
			}			
		};
		add(cartList);
		cartList.setVisible(cartItems > 0);
		add(emptyCart);
		emptyCart.setVisible(cartItems == 0);
//		
		AjaxFallbackLink logOutLink = new AjaxFallbackLink("logOutLink"){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				FruitShopSession.get().setCurrentUser(new Customer());
				FruitShopSession.get().setUsername("");
				setResponsePage(new HomePage(new PageParameters()));
			}
			
		};
		logOutLink.setVisible(isLoggedIn);
		logOutLink.setOutputMarkupId(true);
		add(logOutLink);
		
		BookmarkablePageLink logInLink = new BookmarkablePageLink("logInLink",
				LoginPage.class, new PageParameters());
		logInLink.setVisible(!isLoggedIn);
		logInLink.setOutputMarkupId(true);
		add(logInLink);
		
		BookmarkablePageLink myAccountLink = new BookmarkablePageLink("myAccountLink",
				AddUpdateUserPage.class, new PageParameters());
		myAccountLink.setVisible(isLoggedIn);
		myAccountLink.setOutputMarkupId(true);
		add(myAccountLink);
		
		BookmarkablePageLink resetPasswordLink = new BookmarkablePageLink("resetPasswordLink",
				ResetPasswordPage.class, new PageParameters());
		resetPasswordLink.setVisible(!isLoggedIn);
		resetPasswordLink.setOutputMarkupId(true);
		add(resetPasswordLink);
		
		BookmarkablePageLink signUpLink = new BookmarkablePageLink("signUpLink",
				AddUpdateUserPage.class, new PageParameters());
		signUpLink.setVisible(!isLoggedIn);
		signUpLink.setOutputMarkupId(true);
		add(signUpLink);
		
		List<Items_Category> categories = getCategories();
		ListView categoriesList = new ListView("categoriesList", new PropertyModel(this, "categories")){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				// TODO Auto-generated method stub
				Items_Category itemCategory = (Items_Category) item.getModelObject();

				BookmarkablePageLink categoryLink = new BookmarkablePageLink("categoryLink",
						HomePage.class, new PageParameters().add("categoryId", itemCategory.getCategoryid()));
				categoryLink.add(new Label("categoryLabel", itemCategory.getCategory()));
				item.add(categoryLink);
			}
		};
		add(categoriesList);
	}
	
	
	public List<Items_Category> getCategories() {
		return WicketApplication.get().getCategories();
	}
	
	public List<Item> getItems(Integer CategoryId) {
		return WicketApplication.get().getItems(CategoryId);
	}
	
	public List<CartItem> getActiveItems(Integer CategoryId) {
		List<CartItem> cartList = new ArrayList<CartItem>();
		for (Item item : WicketApplication.get().getActiveItems(CategoryId)){
			cartList.add(new CartItem(item, 0f, 0f));
		}
		return cartList;
	}
	
	public Item getItem(Integer ItemId) {
		return WicketApplication.get().getItem(ItemId);
	}
	
	public Items_Category getCategory(Integer CategoryId) {
		return WicketApplication.get().getCategory(CategoryId);
	}
	public FruitShopSession getSession(){
		return FruitShopSession.get();
	}
   
	public Boolean addNewOrder(Order OrderHeader, ArrayList<OrderedItem> OrderDetails){
		    return WicketApplication.get().addNewOrder(OrderHeader, OrderDetails);
	}
	
	public List<Order> getOrdersFromUsername() {
		return WicketApplication.get().getOrdersFromUser();
	}
	
	public List<OrderedItem> getOrderDetails(Integer OrderId) {
		return WicketApplication.get().getOrderDetails(OrderId);
	}
	
	public Order getOrderFromId(Integer OrderId) {
		return WicketApplication.get().getOrderFromId(OrderId);
	}
	
	public Boolean resetPassword(String Username, String newPassword) {
		return WicketApplication.get().resetPassword(Username, newPassword);
	}
    
	public Boolean AddOrUpdateUser(Customer NewUser) {
		return WicketApplication.get().AddOrUpdateUser(NewUser);
	}
	
	public Boolean MailExists(String Username) {
		return WicketApplication.get().MailExists(Username);
	}
    
	public Boolean AddOrUpdateCategory(Items_Category ItmCat) {
		return WicketApplication.get().AddOrUpdateCategory(ItmCat);
	}
	
	public Boolean AddOrUpdateItem(Item CurItem) {
		return WicketApplication.get().AddOrUpdateItem(CurItem);
	}
	
	public List<CartItem> getAllItems(Integer CategoryId) {
		List<CartItem> cartList = new ArrayList<CartItem>();
		for (Item item : WicketApplication.get().getAllItems(CategoryId)){
			cartList.add(new CartItem(item, 0f, 0f));
		}
		return cartList;
	}
	
	public String getStatusDesc(Integer status){
		switch (status){
			case 0: return "Υπο επεξεργασία";
			case 1: return "Ετοιμάζεται";
			case 2: return "Έτοιμη για αποστολή";
			case 3: return "Έχει αποσταλεί";
			case 4: return "Παραδόθηκε";
			default: return "";
		}
		
	}
	
	public Boolean SendMail(String eml, String sbj, String msg){
		return WicketApplication.get().SendMail(eml, sbj, msg);
	}
	
	public List<Order> getAllActiveOrders() {
		return WicketApplication.get().getAllActiveOrders();
	}
	
	public Boolean UpdateOrder(Order Order) {
		return WicketApplication.get().UpdateOrder(Order);
	}
	
	public List<Customer> getCustomers() {
		return WicketApplication.get().getCustomers();
	}

	@Override
	protected void onBeforeRender() {
		// TODO Auto-generated method stub
		super.onBeforeRender();
		refreshCartItems();
		cartItemsCount.setVisible(cartItems > 0);
		myOrdersLink.setVisible(!FruitShopSession.get().getUsername().isEmpty());
	}
	
	private void refreshCartItems(){
		cartItems = FruitShopSession.get().getCart() == null || FruitShopSession.get().getCart().size() == 0 ? 0 : FruitShopSession.get().getCart().size();
		cartItemsCount.setDefaultModelObject(cartItems);
		if (cartList != null){
			cartList.setVisible(cartItems > 0);
		}
		emptyCart.setVisible(cartItems == 0);
		
		myCartLink.setVisible(cartItems > 0);
	}
	
}
