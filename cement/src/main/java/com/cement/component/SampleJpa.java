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

import com.cement.misc.SieveValue;
import com.cement.model.Sample;

@Repository
public class SampleJpa extends EntityWithIdJpa<Sample> {
	public SampleJpa() {
		super(Sample.class);
	}
	
	@Transactional(readOnly=true)
	public List<Integer> getSievePasses(int sampleId) {
		Query q = em.createQuery("select s.passId from SievePass s where s.sample.id=:id group by s.passId", Integer.class);
		q.setParameter("id", sampleId);
		return q.getResultList();
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getSievePassValues(int sampleId, int passId) {
		Query q = em.createQuery("select s.sieveId, s.value from SievePass s where s.sample.id=:id and s.passId=:passId order by s.sieveId", Object[].class);
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		return q.getResultList();
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
		Query q = em.createQuery("select s.sieveId, s.value, s.passId from SievePass s where s.sample.id=:id", Object[].class);
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
	
	@Transactional(readOnly=true)
	public List<SieveValue> loadPass(int sampleId, int passId) {
		Map<Integer, Map> map = makeSievesMap();
		Query q = em.createQuery("select s.sieveId, s.value from SievePass s where s.sample.id=:id and s.passId=:passId", Object[].class);
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		List<Object[]> items = q.getResultList();
		for (Object[] i : items) {
			Map m = map.get(i[0]);
			if (m == null)
				continue;
			m.put("pass", i[1]);
		}
		List<Map> list = sieveMapToList(map);
		List<SieveValue> r = new ArrayList<>();
		for (Map m : list) {
			SieveValue v = new SieveValue();
			v.setSieve(m.get("label").toString());
			Object o = m.get("pass");
			Double d = 0.0;
			if (o instanceof Number)
				d = ((Number) o).doubleValue();
			v.setValue(d);
			r.add(v);
		}
		return r;
	}
	
	@Transactional(readOnly=true)
	public List<Map> makeNewPass() {
		Map<Integer, Map> map = makeSievesMap();
		List<Map> r = sieveMapToList(map);
		for (Map m : r) {
			m.put("pass", 0.0);
		}
		return r;
	}
/*
	@Transactional(readOnly=true)
	public void savePass(int sampleId, int passId, Map pass) {
		Map<Integer, Map> map = makeSievesMap();
		Query q = em.createQuery("select s.sieveId, s.value from SievePass s where s.sample.id=:id and s.passId=:passId", Object[].class);
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		List<Object[]> items = q.getResultList();
		for (Object[] i : items) {
			Map m = map.get(i[0]);
			if (m == null)
				continue;
			m.put("pass", i[1]);
		}
	}
*/
}
