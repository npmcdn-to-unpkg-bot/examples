package com.cement.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.CurveSieve;
import com.cement.model.Material;
import com.cement.model.ReceiptMaterial;
import com.cement.model.ReceiptSet;
import com.cement.model.Sieve;

@Repository
public class ReceiptSetJpa extends IdNamePairJpaBase<ReceiptSet> {
	public ReceiptSetJpa() {
		super(ReceiptSet.class);
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

	@Transactional(readOnly=true)
	public Map<Integer, Double> loadMaterialSieveData(int materialId) {
		Query q = em.createQuery("select p.sieve sieve, sum(p.value)/count(p) val from SievePass p join Sample s on p.sample=s.id where s.material.id=:materialId and s.status=2 and p.sieve > 0 group by p.sieve");
		q.setParameter("materialId", materialId);
		List<Object[]> items = q.getResultList();
		Map<Integer, Double> r = new HashMap<>();
		for (Object[] item : items) {
			r.put((Integer) item[0], (Double) item[1]);
		}
		return r;
	}

	@Transactional(readOnly=true)
	public List<CurveSieve> loadCurveData(int curveId) {
		Query q = em.createQuery("select cs from CurveSieve cs where cs.curve=:curveId order by cs.sieve");
		q.setParameter("curveId", curveId);
		List<CurveSieve> items = q.getResultList();
		return items;
	}
	
	@Transactional(readOnly=true)
	public List<ReceiptMaterial> loadReceiptSet(int receiptSetId) {
		Query q = em.createQuery("select r.materials from ReceiptSet r where r.id=:receiptSetId");
		q.setParameter("receiptSetId", receiptSetId);
		List<Material> items = q.getResultList();
		List<ReceiptMaterial> r = new ArrayList<>();
		for (Material m : items) {
			ReceiptMaterial rm = new ReceiptMaterial();
			rm.setMaterial(m);
			r.add(rm);
		}
		return r;
	}
}
