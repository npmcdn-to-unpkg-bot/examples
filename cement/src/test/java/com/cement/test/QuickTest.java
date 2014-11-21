package com.cement.test;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.model.Sieve;

public class QuickTest {

	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

//		Query q = em.createQuery("select new com.cement.model.SieveValue(p.sieve.id, p.sieve.name, p.value) " +
//				"from SievePass p where p.sample.id=:sampleId and p.passId=:passId and p.sieve.id <= 1 order by p.sieve.id", SieveValue.class);

/*		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
		Root<Sieve> root = criteria.from(Sieve.class);
		criteria.multiselect(root.get("id"), root.get("name"));
		Query q = em.createQuery(criteria);
*/
/*		Query q = em.createQuery("select p.sieve, p.value from SievePass p left join fetch p.sieve where p.sample.id=:sampleId and p.passId=:passId");
		q.setParameter("sampleId", 23);
		q.setParameter("passId", 0);
*/		
		Query q = em.createNativeQuery("select s.sieve_id id, s.sieve_d name from sieve s order by s.sieve_id", "SieveName_list2"); 
		//Query q = em.createNamedQuery("SieveName_list2");
/*		List<Object> items = q.getResultList();
		for (Object item : items) {
			System.out.println(item);
		}
*/
		List<Object[]> items = q.getResultList();
		for (Object[] item : items) {
			System.out.println(Arrays.toString(item) + " " + item[0].getClass() + ", " + item[1].getClass());
		}
		
/*		Query q = em.createNamedQuery("SieveValue_loadPass");
		q.setParameter(1, 23);
		q.setParameter(2, 0);
		System.out.println("--------");
		List<SieveValue> items = q.getResultList();
		for (SieveValue item : items) {
			System.out.println(item);
		}
		System.out.println("--------");
*/
/*		SievePassJpa jpa = appContext.getBean(SievePassJpa.class);
		Collection<Integer> passes = jpa.listPassIds(3);
		for (Integer i : passes) {
			System.out.println(i);
		}
		*/
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
