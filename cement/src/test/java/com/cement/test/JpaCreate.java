package com.cement.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JpaCreate {

	void doIt() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("JpaCreate-sping.xml", getClass());
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();
		// ... more
		em.close();
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new JpaCreate().doIt();
		System.out.println("Done.");
	}
}
