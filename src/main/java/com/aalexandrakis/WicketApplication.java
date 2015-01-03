package com.aalexandrakis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.resource.FileResourceStream;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.aalexandrakis.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{   
	private String bussiness_email = System.getenv("HOTMAIL");

	//public ServletContext context=null; 
	public String ImagesPath;
	public String ProductImgPath;
	@Override
	public Session newSession(Request request, Response response) {
		return new FruitShopSession(request);
	}
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
		//return ImageResourcesPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard(); 
	    if (packageResourceGuard instanceof SecurePackageResourceGuard) { 
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard; 
		    guard.addPattern("+*.GIF"); 
		} 
	    if (packageResourceGuard instanceof SecurePackageResourceGuard) { 
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard; 
		    guard.addPattern("+*.JPG"); 
		}
	    if (packageResourceGuard instanceof SecurePackageResourceGuard) { 
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard; 
		    guard.addPattern("+*.png"); 
		}
	    
	    final ServletContext context = this.getServletContext();
	    ImagesPath = context.getRealPath("images");
	    ProductImgPath = context.getRealPath("productimg");
	    /**************************** working only for home page
        getSharedResources().add("productimg", new FolderContentResource(new File("C:\\Fruit-e-shop-wicket-photos\\productimg")));
        getSharedResources().add("images", new FolderContentResource(new File("C:\\Fruit-e-shop-wicket-photos\\images")));
        
        mountResource("productimg", new SharedResourceReference("productimg"));
        mountResource("images", new SharedResourceReference("images"));
        *********************************************************/
	    
        // add your configuration here
	    mountPage("login", 	LoginPage.class);
	    mountPage("home", HomePage.class);
	    mountPage("aboutUs", AboutUsPage.class);
	    mountPage("contactUs", ContactUsPage.class);
	    mountPage("where", WherePage.class);
	    mountPage("addUpdateUser", AddUpdateUserPage.class);
	    mountPage("resetPassword", ResetPasswordPage.class);
	    mountPage("myCart", MyCartPage.class);
	    mountPage("myOrders", OrdersPage.class);	   
	    mountPage("orderDetails", OrderDetailsPage.class);
	    mountPage("itemDetails", ItemDetailsPage.class);
	    mountPage("addUpdateItem", AddUpdateItemPage.class);
	    mountPage("orderCompleted", OrderCompleted.class);
	}


	public static WicketApplication get() {
		return (WicketApplication) Application.get();
	}
	
	
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		// TODO Auto-generated method stub
		return RuntimeConfigurationType.DEPLOYMENT;
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	public List<Items_Category> getCategories() {
		List<Items_Category> Categories = new ArrayList<Items_Category>();
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		try{ 
			String hql = "From Items_Category";
			Query q = session.createQuery(hql);
			//		  .setString("email", Email.getInput().toString())
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			Categories = q.list();
			return Categories;
		    } catch (HibernateException e) { 
				e.printStackTrace(); 
			}finally {
				session.close();
			}
			return Categories;
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	public List<Item> getItems(Integer CategoryId) {
		List<Item> Items = null;
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Item where categoryid=:categoryid order by descr";
			Query q = session.createQuery(hql)
					  .setParameter("categoryid", CategoryId);
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			Items = q.list();
			tx.commit();
			
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				session.close();
				return Items;
			}
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	public List<Item> getActiveItems(Integer CategoryId) {
		List<Item> Items = null;
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Item where categoryid=:categoryid and display = 1 and price > 0 order by descr";
			Query q = session.createQuery(hql)
					  .setParameter("categoryid", CategoryId);
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			Items = q.list();
			tx.commit();
			
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				session.close();
				return Items;
			}

		
	}
	
	@SuppressWarnings({ "finally", "unchecked" })
	public List<Item> getAllItems(Integer CategoryId) {
		List<Item> Items = null;
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Item where categoryid=:categoryid order by descr";
			Query q = session.createQuery(hql)
					  .setParameter("categoryid", CategoryId);
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			Items = q.list();
			tx.commit();
			
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				session.close();
				return Items;
			}

		
	}

	@SuppressWarnings("finally")
	public Item getItem(Integer ItemId) {
		Item SelectedItem = new Item();
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Item where itemid=:itemid";
			Query q = session.createQuery(hql)
					  .setParameter("itemid", ItemId);
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			SelectedItem = (Item) q.list().get(0);
			tx.commit();
			
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				session.close();
				return SelectedItem;
			}
	    }

		@SuppressWarnings("finally")
		public Items_Category getCategory(Integer CategoryId) {
			Items_Category SelectedCategory = new Items_Category();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Items_Category where categoryid=:categoryid";
				Query q = session.createQuery(hql)
						  .setParameter("categoryid", CategoryId);
				//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
				//info(Sha1.getHash(Password.getInput().toString()));
				SelectedCategory = (Items_Category) q.list().get(0);
				tx.commit();
				
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace(); 
				}finally {
					session.close();
					return SelectedCategory;
				}

	}
		
		public Boolean addNewOrder(Order OrderHeader, ArrayList<OrderedItem> OrderDetails) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.save(OrderHeader);
//			    System.out.print(OrderHeader.getOrderid());
			    Iterator<?> ItemsIterator =  OrderDetails.iterator();
			    while(ItemsIterator.hasNext()){
			    	//System.out.print("On  call " + OrderDetails.size() + "\n");
			    	OrderedItem NewItem = (OrderedItem) ItemsIterator.next();
			    	NewItem.setOrderid(OrderHeader.getOrderid());
			    	NewItem.setAmount(NewItem.getQuantity() * NewItem.getPrice());
			    	session.save(NewItem);
			    }
			    tx.commit();
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					session.close();
				}

	}

		@SuppressWarnings({ "finally", "unchecked" })
		public List<Order> getOrdersFromUser() {
			List<Order> ReturnList = new ArrayList<Order>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Order where custid=:custid";
				Query q = session.createQuery(hql)
						  .setParameter("custid", 
								  FruitShopSession.get().getCurrentUser().getCustomerId());
				tx.commit();
				ReturnList = q.list();
				
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					session.close();
					return ReturnList;
				}
	}
		
		@SuppressWarnings({ "finally", "unchecked" })
		public List<OrderedItem> getOrderDetails(Integer OrderId) {
			List<OrderedItem> ReturnList = new ArrayList<OrderedItem>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From OrderedItem where orderid=:orderid";
				Query q = session.createQuery(hql)
						  .setParameter("orderid", OrderId); 
				tx.commit();
				ReturnList = q.list();
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					session.close();
					return ReturnList;
				}
	}

		@SuppressWarnings("finally")
		public Order getOrderFromId(Integer OrderId) {
			Order ReturnOrder = new Order();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Order where orderid=:orderid";
				Query q = session.createQuery(hql)
						  .setParameter("orderid", OrderId); 
				tx.commit();
				ReturnOrder = (Order) q.list().get(0);
				
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					session.close();
					return ReturnOrder;
				}
	}
		
		public Boolean resetPassword(String Username, String newPassword) {
			Customer user = new Customer();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Customer where email=:username";
				Query q = session.createQuery(hql)
						  .setParameter("username", Username);
				if (q.list().size() == 0){
					return false;
				}
				user = (Customer) q.list().get(0);
				user.setPassword(Sha1.getHash(newPassword));
				session.update(user);
				String sbj = "Αλλαγή κωδικού";
				String msg = "Ο νέος κωδικός είναι " + newPassword;
				if (SendMail(Username, sbj, msg)){
					tx.commit();
					return true;
				} else {
					tx.rollback();
					return false;
				}
				
				} catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				} finally {
					session.close();
				}
		}
		
		public static Boolean SendMail(String eml, String sbj, String msg){
			Properties props = new Properties();
	    	props.setProperty("mail.transport.protocol", "smtp");
		    props.setProperty("mail.host", "smtp.live.com");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.smtp.auth", "true");
			
		    javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator()
	        { protected PasswordAuthentication getPasswordAuthentication()
	            {  return new PasswordAuthentication(System.getenv("HOTMAIL"), System.getenv("HOTMAIL_PASSWORD"));
	             }
	         });
		    session.setDebug(false);
		    Transport trans = null;
			try {
				trans = session.getTransport("smtp");
			} catch (NoSuchProviderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        Message message = new MimeMessage(session);
	        try {
	    	    trans.connect("smtp.live.com", 25, System.getenv("HOTMAIL"), System.getenv("HOTMAIL_PASSWORD"));
				message.setFrom(new InternetAddress(System.getenv("HOTMAIL")));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(eml));
	        	//String msg="Thank's to Behind Java Scene to teach the core aspect of Email sending ";
				message.setSubject(sbj);
				message.setContent(msg, "text/html; charset=utf-8");
				Transport.send(message);
	            return true;
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		public Boolean AddOrUpdateUser(Customer NewUser) {
			Integer CustId = NewUser.getCustomerId();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.saveOrUpdate(NewUser);
			    tx.commit();
			    if (CustId==null){
			    	String sbj="New User";
			    	String msg="New User " + NewUser.getName() + " " + NewUser.getEmail();
			    	SendMail(bussiness_email, sbj, msg);
			    }
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					session.close();
				}

		}

		public Boolean MailExists(String Username) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Customer where email=:username";
				Query q = session.createQuery(hql)
						  .setParameter("username", Username);
				if (q.list().size()>0){
					return true;
				} else {
					return false;
				}
				  //tx.commit();
				} catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return true;
				} finally {
					session.close();
				}
		}
		
		public Boolean AddOrUpdateCategory(Items_Category ItmCat) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.saveOrUpdate(ItmCat);
			    tx.commit();
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					session.close();
				}

		}
		
		public Boolean AddOrUpdateItem(Item CurItem) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.saveOrUpdate(CurItem);
			    tx.commit();
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					session.close();
				}
		}

		@SuppressWarnings({ "finally", "unchecked" })
		public List<Order> getAllActiveOrders() {
			List<Order> ReturnList = new ArrayList<Order>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Order where status < 4 order by date";
				Query q = session.createQuery(hql);
				tx.commit();
				ReturnList = q.list();
				} catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					session.close();
					return ReturnList;
				}
	}
		public Boolean UpdateOrder(Order Order) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.update(Order);
			    tx.commit();
			    
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					session.close();
				}

		}
		
		@SuppressWarnings({ "unchecked", "finally" })
		public List<Customer> getCustomers() {
			List<Customer> ReturnList = new ArrayList<Customer>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Customer where Admin=0 order by name";
				Query q = session.createQuery(hql);
				tx.commit();
				ReturnList = q.list();
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					session.close();
					return ReturnList;
				}
		}
		

		@SuppressWarnings({ "unchecked", "finally" })
		public String getUserEmailById(int id) {
			String userEmail = "";
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			try{ 
				Customer cust = (Customer) session.get(Customer.class, id);
				userEmail = cust.getEmail();
			    } catch (HibernateException e) { 
					e.printStackTrace();
				}finally {
					session.close();
					return userEmail;
				}
		}

	

}


class FolderContentResource implements IResource {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final File rootFolder;
    
    public FolderContentResource(File rootFolder) {
        this.rootFolder = rootFolder;
    }
    public void respond(Attributes attributes) {
    	PageParameters parameters = attributes.getParameters();
        String fileName = parameters.get(0).toString();
        File file = new File(rootFolder, fileName);
        FileResourceStream fileResourceStream = new FileResourceStream(file);
        ResourceStreamResource resource = new ResourceStreamResource(fileResourceStream);
        resource.respond(attributes);
    }
}
