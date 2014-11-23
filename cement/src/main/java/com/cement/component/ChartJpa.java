package com.cement.component;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.Chart;
import com.cement.model.Sieve;

@Repository
public class ChartJpa extends IdNamePairJpaBase<Chart> {
	public ChartJpa() {
		super(Chart.class);
	}
	
	@Transactional(readOnly=true)
	public List loadChartItemValues(int chartItemId) {
		Query q = em.createNativeQuery("select cv.value v from chart_item_value cv join sieve s on cv.sieve_id=s.sieve_id where cv.item_id=?1 order by s.sieve_d");
				// "select v from ChartItemValue v join Sieve s where v.chartItem.id=:chartItemId order by s.name");
		q.setParameter(1, chartItemId);
		return q.getResultList();
	}

	@Transactional(readOnly=true)
	public List<Object[]> loadCurve2(int curveId) {
		Query q = em.createNativeQuery("select s.sieve_d, low_value, upper_value, value " +
				"from sieve s join curve_sieve cs on s.sieve_id=cs.sieve_id where curve_id=?1 order by s.sieve_d");
		q.setParameter(1, curveId);
		List<Object[]> list = q.getResultList();
		return list;
	}

	@Transactional(readOnly=true)
	public String loadSieveLabels() {
		Query q = em.createQuery("select s from Sieve s order by s.name");
		List<Sieve> sieves = q.getResultList();
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
