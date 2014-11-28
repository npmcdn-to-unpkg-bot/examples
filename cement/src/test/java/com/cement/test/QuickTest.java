package com.cement.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.component.ReceiptSetJpa;
import com.cement.misc.Adjust;
import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;

public class QuickTest {

	public void adjust(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

		ReceiptSetJpa jpa = appContext.getBean(ReceiptSetJpa.class);
		//List<ReceiptMaterial> rm = jpa.loadReceiptSet(1);
		List<ReceiptMaterial> rm = new ArrayList<>();
		rm.add(new ReceiptMaterial(em.find(Material.class, 3)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 5)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 13)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 14)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 23)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 33)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 34)));
		rm.add(new ReceiptMaterial(em.find(Material.class, 36)));
		
		Adjust adj = new Adjust(jpa);
		List<CurveSieve> curve = jpa.loadCurveData(1);
		adj.calc(curve, rm, 1, 1000.0);
		
		em.close();
	}	
	
	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

		System.out.println("--------------------------------");
		Query q = em.createQuery("select r.materials from ReceiptSet r where r.id=:receiptSetId");
		//Query q = em.createQuery("select p.sieve sieve, sum(p.value)/count(p) val from SievePass p join Sample s on p.sample=s.id where s.material.id=:materialId and s.status=2 and p.sieve > 0 group by p.sieve");
		//Query q = em.createQuery("select p.sieve sieve, count(p) val from SievePass p where p.sieve > 0 group by p.sample, p.sieve");
		//Query q = em.createQuery("select s from Sample s where s.material.id=:materialId");
		q.setParameter("receiptSetId", 3);
		
		if (false) {
			List<Object[]> items = q.getResultList();
			for (Object[] item : items) {
				System.out.println(Arrays.toString(item) + " " + item[0].getClass() + ", " + item[1].getClass());
			}
		} else {
			List<Object> items = q.getResultList();
			for (Object item : items) {
				System.out.println(String.valueOf(item));
			}
		}

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
		
		
		//createORMs(appContext);
		adjust(appContext);
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new QuickTest().doIt();
		System.out.println("Done.");
	}
}
