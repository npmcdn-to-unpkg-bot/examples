package com.cement.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.Sample;

@Repository
public class SampleJpa extends EntityWithIdJpa<Sample> {
	public SampleJpa() {
		super(Sample.class);
	}
	
	private static void addMap(Integer sieveId, Object sieveLabel, Map<Integer, Map> map) {
		Map m = new HashMap();
		m.put("id", sieveId);
		m.put("label", sieveLabel);
		m.put("sieve", "sieve" + m.get("id"));
		map.put(sieveId, m);
	}
	
	private Map<Integer, Map> makeSievesMap() {
		Query q = em.createQuery("select s.id, s.name from Sieve s", Object[].class);
		List<Object[]> items = q.getResultList();
		Map<Integer, Map> map = new HashMap<Integer, Map>();
		for (Object[] i : items) {
			addMap((Integer) i[0], i[1], map);
		}
		addMap(-2, "Value -2", map);
		addMap(-1, "Value -1", map);
		
		return map;
	}
	
	private static List<Map> sieveMapToList(Map<Integer, Map> map) {
		List<Map> r = new ArrayList(map.values());
		Collections.sort(r, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {
				return Integer.compare((Integer) o1.get("id"), (Integer) o2.get("id"));
			}
		});
		return r;
	}
	
	@Transactional(readOnly=true)
	public List<Map> getSampleData(int sampleId) {
		Map<Integer, Map> map = makeSievesMap();
		Query q = em.createQuery("select p.sieve.id, p.value, p.passId from SievePass p where p.sample.id=:id", Object[].class);
		q.setParameter("id", sampleId);
		List<Object[]> items = q.getResultList();
		for (Object[] i : items) {
			Map m = map.get(i[0]);
			if (m == null)
				continue;
			m.put(i[2], i[1]);
		}
		return sieveMapToList(map);
	}
}
