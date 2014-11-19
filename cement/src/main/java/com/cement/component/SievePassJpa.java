package com.cement.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.model.Sample;
import com.cement.model.Sieve;
import com.cement.model.SievePass;
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
	public List<SieveValue> makeNew() {
		Query q = em.createQuery("select s.id sieveId, s.name sieve, 0 value from Sieve s order by s.id", SieveValue.class);
		List<SieveValue> items = q.getResultList();
		List<SieveValue> result = new ArrayList<>();
		result.add(new SieveValue(-2, "Value -2", 0));
		result.add(new SieveValue(-1, "Value -1", 0));
		result.addAll(items);
		return result;
	}

	@Transactional(readOnly=true)
	public List<SieveValue> load(int sampleId, int passId) {
		Query q = em.createQuery("select p.sieve.id as sieveId, s.name as sieve, p.value as value from SievePass p left join Sieve s where p.sample.id=:id and p.passId=:passId order by p.sieve.id", SieveValue.class);
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		List<SieveValue> items = q.getResultList();
		for (SieveValue i : items) {
			switch (i.getSieveId()) {
			case -2:
				i.setSieve("Value -2");
				break;
			case -1:
				i.setSieve("Value -1");
				break;
			}
		}
		return items;
	}

	/**
	 * passId = -1 => create new
	 */
	@Transactional
	public void save(int sampleId, int passId, List<SieveValue> items) {
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

		Sample sample = em.find(Sample.class, sampleId);
		for (SieveValue item : items) {
			SievePass pass = new SievePass();
			pass.setSample(sample);
			pass.setSieve(em.find(Sieve.class, item.getSieveId()));
			pass.setPassId(passId);
			pass.setValue(item.getValue());
			em.merge(pass);
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
