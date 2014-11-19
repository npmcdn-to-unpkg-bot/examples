package com.cement.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.component.SampleJpa;
import com.cement.component.SievePassJpa;
import com.cement.model.SieveValue;

public class QuickTest {

	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

		Query q = em.createQuery("select p.sieve.id sieveId, s.name sieve, p.value from SievePass p left join Sieve s where p.sample.id=:id and p.passId=:passId order by p.sieve.id", SieveValue.class);	
		q.setParameter("id", 23);
		q.setParameter("passId", 0);
		List items = q.getResultList();
		for (Object item : items) {
			System.out.println(item);
		}

		SievePassJpa jpa = appContext.getBean(SievePassJpa.class);
		Collection<Integer> passes = jpa.listPassIds(3);
		for (Integer i : passes) {
			System.out.println(i);
		}
		
/*		List<Object[]> vals = jpa.getSievePassValues(3, 2);
		for (Object[] i : vals) {
			System.out.println(Arrays.toString(i));
		}
*/		// ... more
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
