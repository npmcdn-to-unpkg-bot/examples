package com.cement.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.model.Chart;
import com.cement.model.ChartItem;
import com.cement.model.ChartItemValue;

public class QuickTest {

	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

//		Query q = em.createQuery("select new com.cement.model.SieveValue(p.sieve.id, p.sieve.name, p.value) " +
//				"from SievePass p where p.sample.id=:sampleId and p.passId=:passId and p.sieve.id <= 1 order by p.sieve.id", SieveValue.class);

/*		Query q = em.createNamedQuery("SieveName_list2");
		List<Object> items = q.getResultList();
		for (Object item : items) {
			System.out.println(item);
		}
*/
		Chart c = em.find(Chart.class, 104);
		for (ChartItem ci : c.getItems()) {
			System.out.println("1");
		}
/*		List<Object[]> items = q.getResultList();
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

	static int byteArray2Int(byte[] b) {
		return 
			((((int) b[0]) & 0x0ff) << 24) |
			((((int) b[1]) & 0x0ff) << 16) |
			((((int) b[2]) & 0x0ff) << 8) |
			((((int) b[3]) & 0x0ff) << 0);
	}
	
	void doIt() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("QuickTest.xml", getClass());
		DataSource dataSource = appContext.getBean("dataSource", DataSource.class);
/*
		Connection conn = dataSource.getConnection();
		PreparedStatement st = conn.prepareStatement("select recp_data from recp where recp_id = 10");
		ResultSet rs = st.executeQuery();
		rs.next();
		InputStream is = rs.getBinaryStream(1);
		byte[] buf = new byte[4];
		int counter = 0;
		while (true) {
			int read = is.read(buf);
			if (read != 4)
				break;
			System.out.println(counter + "\t" + byteArray2Int(buf));
			counter++;
		}
*/		
//		FileOutputStream fou = new FileOutputStream("out.txt");
//		IOUtils.copy(is, fou);
//		fou.close();
		
		
		createORMs(appContext);
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new QuickTest().doIt();
		System.out.println("Done.");
	}
}
