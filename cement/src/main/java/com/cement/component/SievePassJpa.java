package com.cement.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.misc.SieveListForm;
import com.cement.misc.SieveMapForm;
import com.cement.model.IdName;
import com.cement.model.Sample;
import com.cement.model.Sieve;
import com.cement.model.SievePass;
import com.cement.model.SievePassJustIds;
import com.cement.model.SieveValue;

@Repository
public class SievePassJpa {
	@PersistenceContext
	protected EntityManager em;

	@Transactional(readOnly=true)
	public Collection<Integer> listPassIds(int sampleId) {
		Query q = em.createQuery("select p.passId from SievePass p where p.sample.id=:sampleId group by p.passId order by p.passId", Integer.class);
		q.setParameter("sampleId", sampleId);
		return q.getResultList();
	}

	@Transactional(readOnly=true)
	public Collection<IdName> listSieves() {
		Query q = em.createNamedQuery("SieveName_list");
		List<IdName> result = new ArrayList<>();
		result.add(new IdName(-1, "Value -1"));
		result.add(new IdName(0, "Value 0"));
		result.addAll(q.getResultList());
		
		return result;
	}

	@Transactional(readOnly=true)
	public SieveListForm makeNew() {
		List<SieveValue> list = new ArrayList<>();
		Collection<IdName> sieves = listSieves();
		for (IdName sieve : sieves) {
			list.add(new SieveValue(sieve.getId(), 0));
		}
		SieveListForm result = new SieveListForm();
		result.setSieve(list);
		return result;
	}

	@Transactional(readOnly=true)
	public SieveListForm load(int sampleId, int passId) {
		Query q = em.createNamedQuery("SieveValue_loadPass");
		q.setParameter(1, sampleId);
		q.setParameter(2, passId);
		SieveListForm r = new SieveListForm();
		r.setSieve(q.getResultList());
		return r;
	}

	@Transactional(readOnly=true)
	public SieveMapForm load2(int sampleId, int passId) {
		//Query q = em.createNativeQuery("select p.sieve_id, p.value from material_sample_sieve_pass p where p.sample_id=23 and p.pass_id=0");
		Query q = em.createNamedQuery("SieveValue_loadPass");
		q.setParameter(1, sampleId);
		q.setParameter(2, passId);
		List<SieveValue> list = q.getResultList();
		Map<Integer, SieveValue> map = new HashMap<>();
		for (SieveValue i : list) {
			map.put(i.getSieveId(), i);
		}
		SieveMapForm r = new SieveMapForm();
		r.setSieve(map);
		return r;
	}

	/**
	 * passId = -1 => create new
	 */
	@Transactional
	public void save(int sampleId, int passId, SieveListForm data) {
		if (passId == -1) {
			Collection<Integer> passes = listPassIds(sampleId);
			passId = 0;
			for (Integer i : passes) {
				if (i == passId)
					passId++;
			}
		}
		Query q = em.createQuery("delete from SievePass p where p.sample.id=:sampleId and p.passId=:passId");
		q.setParameter("sampleId", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();

		for (SieveValue item : data.getSieve()) {
			SievePassJustIds pass = new SievePassJustIds();
			pass.setSample(sampleId);
			pass.setSieve(item.getSieveId());
			pass.setPassId(passId);
			pass.setValue(item.getVal());
			em.persist(pass);
		}
	}

	
	@Transactional
	public void delete(int sampleId, int passId) throws Exception {
		Query q = em.createQuery("delete from SievePass s where s.sample.id=:id and s.passId=:passId");
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();
	}

	
}
