package com.aalexandrakis;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory; 
import org.hibernate.cfg.Configuration;    
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
public class HibarnateUtil {        
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory getSessionFactory() throws HibernateException {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    return sessionFactory;
	}	
	 public static void shutdown() {
	    	// Close caches and connection pools
	    	getSessionFactory().close();
	    }

	}


