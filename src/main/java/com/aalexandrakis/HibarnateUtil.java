package com.aalexandrakis;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibarnateUtil {
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory getSessionFactory() throws HibernateException {
		System.out.println("DBUSERNAME : " + System.getenv("FRUITSHOP_DB_USERNAME"));
		System.out.println("DBPASSWORD : " + System.getenv("FRUITSHOP_DB_PASSWORD"));
		System.out.println("DBIP : " + System.getenv("FRUITSHOP_DB_IP"));
		System.out.println("DBPASSWORD : " + System.getenv("FRUITSHOP_DB_PASSWORD"));
		Configuration configuration = new Configuration();
		String fullDbUrl = "jdbc:mysql://" + System.getenv("FRUITSHOP_DB_IP")
				+ ":" + System.getenv("FRUITSHOP_DB_PORT") + "/"
				+ System.getenv("FRUITSHOP_DB_NAME");
		configuration.setProperty("hibernate.connection.url", fullDbUrl);
		configuration.setProperty("hibernate.connection.username",
				System.getenv("FRUITSHOP_DB_USERNAME"));
		configuration.setProperty("hibernate.connection.password",
				System.getenv("FRUITSHOP_DB_PASSWORD"));
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}

}
