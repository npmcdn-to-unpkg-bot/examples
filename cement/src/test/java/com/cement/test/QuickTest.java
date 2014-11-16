package com.cement.test;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.component.SampleJpa;

public class QuickTest {

	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();
		
		SampleJpa jpa = appContext.getBean(SampleJpa.class);
		List<Integer> passes = jpa.getSievePasses(3);
		for (Integer i : passes) {
			System.out.println(i);
		}
		
		List<Object[]> vals = jpa.getSievePassValues(3, 2);
		for (Object[] i : vals) {
			System.out.println(Arrays.toString(i));
		}
		// ... more
		em.close();
	}
	
	void doIt() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("QuickTest.xml", getClass());
		DataSource dataSource = appContext.getBean("dataSource", DataSource.class);

		createORMs(appContext);
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new QuickTest().doIt();
		System.out.println("Done.");
	}
}
