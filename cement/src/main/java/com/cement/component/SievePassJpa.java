package com.cement.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SievePassJpa {
	@PersistenceContext
	protected EntityManager em;

	@Transactional(readOnly=true)
	public Collection<Integer> listPassIds(int sampleId) {
		Query q = em.createQuery("select s.passId from SievePass s where s.sample.id=:id group by s.passId", Integer.class);
		q.setParameter("id", sampleId);
		return q.getResultList();
	}
	
/*	@Transactional(readOnly=true)
	public List<Object[]> getSievePassValues(int sampleId, int passId) {
		Query q = em.createQuery("select s.sieve_id, s.value from SievePass s where s.sample.id=:id and s.pass_id=:passId order by s.sieve_id", Object[].class);
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		return q.getResultList();
	}
*/
	private static void addMap(Integer sieveId, String sieveLabel, Map<Integer, Map> map) {
		Map m = new HashMap();
		m.put("id", sieveId);
		m.put("label", sieveLabel);
		map.put(sieveId, m);
	}
	
	@Transactional(readOnly=true)
	public Collection<Map> loadData(int sampleId) {
		Query q = em.createQuery("select s.id, s.name from Sieve s", Object[].class);
		List<Object[]> items = q.getResultList();
		Map<Integer, Map> map = new HashMap<Integer, Map>();
		for (Object[] i : items) {
			addMap((Integer) i[0], (String) i[1], map);
		}
		addMap(-2, "Value -2", map);
		addMap(-1, "Value -1", map);
		
		q = em.createQuery("select s.sieveId, s.passId, s.value from SievePass s where s.sample.id=:id", Object[].class);
		q.setParameter("id", sampleId);
		items = q.getResultList();
		for (Object[] i : items) {
			Map m = map.get(i[0]);
			if (m == null)
				continue;
			m.put(i[1], i[2]);
		}
		
		List<Map> r = new ArrayList(map.values());
		Collections.sort(r, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {
				return Integer.compare((Integer) o1.get("id"), (Integer) o2.get("id"));
			}
		});

		return r;
	}
	
	@Transactional
	public void delete(int sampleId, int passId) throws Exception {
		Query q = em.createQuery("delete from SievePass s where s.sample.id=:id and s.passId=:passId");
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();
	}

	
}
