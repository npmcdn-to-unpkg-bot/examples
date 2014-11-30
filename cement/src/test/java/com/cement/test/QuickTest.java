package com.cement.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cement.model.ReceiptSet;
import com.slavi.math.MathUtil;

public class QuickTest {

	public void adjust(ApplicationContext appContext) throws Exception {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();

		ReceiptSetJpa jpa = appContext.getBean(ReceiptSetJpa.class);
		int wizardId = 8;
		
		ReceiptSet wizard = jpa.load(wizardId);
		List<CurveSieve> curve = jpa.loadCurveData(1);
		
		List<ReceiptMaterial> materials = new ArrayList<>();
		for (Material m : wizard.getMaterials()) {
			ReceiptMaterial rm = new ReceiptMaterial();
			rm.setMaterial(m);
			materials.add(rm);
		}

		Adjust adj = new Adjust(jpa);

		Map<Integer, Map<Integer, Double>> mmap = new HashMap<>();
		for (ReceiptMaterial rm : materials) {
			Material m = rm.getMaterial();
			Integer materialId = m.getId();
			Map<Integer, Double> mdata = jpa.loadMaterialSieveData(materialId);
			adj.processMaterialData(curve, mdata);
			mmap.put(materialId, mdata);
		}

		List<Double> extResult = new ArrayList<>();
		for (ReceiptMaterial m : materials) {
			int materialId = m.getMaterial().getId();
			double k = 0;
			switch (materialId) {
			case 5: // естествен пясък 0 - 5; Садово; Ескана
				k = 4.6;
				break;
			case 6: // речен пясък 0 - 5; Силистра; Поларис 8
				k = 0;
				break;
			case 7: // речен пясък фракциониран 0 - 5; Пожарево; Инерт ООД
				k = 9.4;
				break;
			case 166: // трошен камък 4 - 20; Сини вир; Ескана
				k = 49.1;
				break;
			case 196: // трошен пясък 0 - 5; Ветрино; Ескана
				k = 36.8;
				break;
			default:
				throw new Error();
			}
			extResult.add(k);
		}
		
		List<Double> r = adj.calc(curve, materials, 1, 1.0);
		
		for (int index = 0; index < curve.size(); index++) {
			CurveSieve cs = curve.get(index);
			
			double sum = 0;
			for (int indexM = 0; indexM < materials.size(); indexM++) {
				ReceiptMaterial m = materials.get(indexM);
				int materialId = m.getMaterial().getId();
				Map<Integer, Double> mdata = mmap.get(materialId);
				double k = extResult.get(indexM);
				double v = mdata.get(cs.getSieve());
				sum += k*v;
			}
			System.out.println("Sieve ID:" + cs.getSieve() + " = " + MathUtil.d4(sum) + ", " + MathUtil.d4(r.get(index)));
		}

		for (int indexM = 0; indexM < materials.size(); indexM++) {
			ReceiptMaterial m = materials.get(indexM);
			double k1 = extResult.get(indexM) / 100;
			double k2 = m.getQuantity();
			System.out.println("mateId:" + MathUtil.l10(m.getMaterial().getId()) + " = " + 
					MathUtil.d4(k1) + " | " + 
					MathUtil.d4(k2) + "    | " + m.getMaterial().toString());
		}
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
