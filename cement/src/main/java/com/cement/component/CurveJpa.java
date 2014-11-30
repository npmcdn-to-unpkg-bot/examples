package com.cement.component;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.Curve;
import com.cement.model.Sieve;

@Repository
public class CurveJpa extends IdNamePairJpaBase<Curve> {
	public CurveJpa() {
		super(Curve.class);
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> loadCurve(int curveId) {
		Query q = em.createNativeQuery("select s.sieve_d, low_value, upper_value, value " +
				"from sieve s join curve_sieve cs on s.sieve_id=cs.sieve_id where curve_id=?1 order by s.sieve_d");
		q.setParameter(1, curveId);
		List<Object[]> list = q.getResultList();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Sieve> loadSieves() {
		Query q = em.createQuery("select s from Sieve s order by s.name");
		List<Sieve> sieves = q.getResultList();
		return sieves;
	}

	@Transactional(readOnly=true)
	public String loadSieveLabels() {
		List<Sieve> sieves = loadSieves();
		String prefix = "";
		StringBuilder r = new StringBuilder();
		for (Sieve s : sieves) {
			r.append(prefix);
			r.append(s.getName());
			prefix = ",";
		}
		return r.toString();
	}
}
