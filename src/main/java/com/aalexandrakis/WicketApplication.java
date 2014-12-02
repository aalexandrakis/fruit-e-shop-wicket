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
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.SharedResourceReference;
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
	private String bussiness_email = "aalexandrakis@hotmail.com";
	
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
	}


	public static WicketApplication get() {
		return (WicketApplication) Application.get();
	}
	
	public List<Items_Category> getCategories() {
		List<Items_Category> Categories = null;
		SessionFactory sf = HibarnateUtil.getSessionFactory(); 
		org.hibernate.Session session = sf.openSession(); 
		Transaction tx = null;
		try{ 
			tx = session.beginTransaction();
			String hql = "From Items_Category";
			Query q = session.createQuery(hql);
			//		  .setString("email", Email.getInput().toString())
			//		  .setString("password", Sha1.getHash(Password.getInput().toString()));
			//info(Sha1.getHash(Password.getInput().toString()));
			Categories = q.list();
			tx.commit();
			
		    } catch (HibernateException e) { 
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				((org.hibernate.Session) session).close();
				return Collections.unmodifiableList(Categories);
			}

		
	}
	
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
				((org.hibernate.Session) session).close();
				return Collections.unmodifiableList(Items);
			}

		
	}
	
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
				((org.hibernate.Session) session).close();
				return Collections.unmodifiableList(Items);
			}

		
	}
	
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
				((org.hibernate.Session) session).close();
				return Collections.unmodifiableList(Items);
			}

		
	}

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
				((org.hibernate.Session) session).close();
				return SelectedItem;
			}
	    }

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
					((org.hibernate.Session) session).close();
					return SelectedCategory;
				}

	}
		
		public Boolean AddNewOrder(Orders OrderHeader, ArrayList<Ordered_Items> OrderDetails) {
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
			    session.save(OrderHeader);
			    System.out.print(OrderHeader.getOrderid());
			    Iterator ItemsIterator =  OrderDetails.iterator();
			    while(ItemsIterator.hasNext()){
			    	//System.out.print("On  call " + OrderDetails.size() + "\n");
			    	Ordered_Items NewItem = (Ordered_Items) ItemsIterator.next();
			    	NewItem.setOrderid(OrderHeader.getOrderid());
			    	session.save(NewItem);
			    }
			    tx.commit();
			    return true;
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
					return false;
				}finally {
					((org.hibernate.Session) session).close();
				}

	}

		public List<Orders> getOrdersFromUsername() {
			List<Orders> ReturnList = new ArrayList<Orders>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Orders where username=:username";
				Query q = session.createQuery(hql)
						  .setParameter("username", 
								  FruitShopSession.get().getUsername());
				tx.commit();
				ReturnList = q.list();
				
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					((org.hibernate.Session) session).close();
					return ReturnList;
				}
	}
		
		public List<Ordered_Items> getOrderDetails(Integer OrderId) {
			List<Ordered_Items> ReturnList = new ArrayList<Ordered_Items>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Ordered_Items where orderid=:orderid";
				Query q = session.createQuery(hql)
						  .setParameter("orderid", OrderId); 
				tx.commit();
				ReturnList = q.list();
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					((org.hibernate.Session) session).close();
					return ReturnList;
				}
	}

		public Orders getOrderFromId(Integer OrderId) {
			Orders ReturnOrder = new Orders();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Orders where orderid=:orderid";
				Query q = session.createQuery(hql)
						  .setParameter("orderid", OrderId); 
				tx.commit();
				ReturnOrder = (Orders) q.list().get(0);
				
			    } catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					((org.hibernate.Session) session).close();
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
					((org.hibernate.Session) session).close();
				}
		}
		
		public Boolean SendMail(String eml, String sbj, String msg){
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
		    session.setDebug(true);
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
					((org.hibernate.Session) session).close();
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
					((org.hibernate.Session) session).close();
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
					((org.hibernate.Session) session).close();
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
					((org.hibernate.Session) session).close();
				}
		}

		public List<Orders> getAllActiveOrders() {
			List<Orders> ReturnList = new ArrayList<Orders>();
			SessionFactory sf = HibarnateUtil.getSessionFactory(); 
			org.hibernate.Session session = sf.openSession(); 
			Transaction tx = null;
			try{ 
				tx = session.beginTransaction();
				String hql = "From Orders where status < 4 order by date";
				Query q = session.createQuery(hql);
				tx.commit();
				ReturnList = q.list();
				} catch (HibernateException e) { 
					if (tx!=null) tx.rollback();
					e.printStackTrace();
				}finally {
					((org.hibernate.Session) session).close();
					return ReturnList;
				}
	}
		public Boolean UpdateOrder(Orders Order) {
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
					((org.hibernate.Session) session).close();
				}

		}
		
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
					((org.hibernate.Session) session).close();
					return ReturnList;
				}
	}

	

}


class FolderContentResource implements IResource {
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
