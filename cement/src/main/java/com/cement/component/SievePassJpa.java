package com.cement.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cement.misc.SievePassForm;
import com.cement.model.IdNamePair;
import com.cement.model.SievePass;

@Repository
public class SievePassJpa {
	@PersistenceContext
	protected EntityManager em;

	@Transactional(readOnly=true)
	public List<Integer> listPassIds(int sampleId) {
		Query q = em.createQuery("select p.passId from SievePass p where p.sample=:sampleId group by p.passId order by p.passId");
		q.setParameter("sampleId", sampleId);
		return q.getResultList();
	}

	@Transactional(readOnly=true)
	public List<IdNamePair> listSieves() {
		Query q = em.createNamedQuery("SieveName_list");
		List<IdNamePair> result = new ArrayList<>();
		result.add(new IdNamePair(-1, "Value -1"));
		result.add(new IdNamePair(0, "Value 0"));
		result.addAll(q.getResultList());
		
		return result;
	}

	@Transactional(readOnly=true)
	public SievePassForm makeNew() {
		Collection<IdNamePair> sieves = listSieves();
		SievePassForm result = new SievePassForm();
		for (IdNamePair sieve : sieves) {
			result.getValues().add(0.0);
		}
		return result;
	}

	@Transactional(readOnly=true)
	public SievePassForm load(int sampleId, int passId) {
		SievePassForm r = new SievePassForm();
		Query q = em.createQuery("select p.value val from SievePass p where p.sample=:sampleId and p.sieve=:sieveId and p.passId=:passId");
		q.setParameter("sampleId", sampleId);
		q.setParameter("passId", passId);
		for (IdNamePair sieve : listSieves()) {
			q.setParameter("sieveId", sieve.getId());
			double d = 0.0;
			try {
				d = ((Number) q.getSingleResult()).doubleValue();
			} catch (NoResultException e) {
			}
			r.getValues().add(d);
		}
		return r;
	}

	/**
	 * passId = -1 => create new
	 */
	@Transactional
	public void save(int sampleId, int passId, SievePassForm data) {
		if (passId == -1) {
			Collection<Integer> passes = listPassIds(sampleId);
			passId = 0;
			for (Integer i : passes) {
				if (i == passId)
					passId++;
			}
		}
		Query q = em.createQuery("delete from SievePass p where p.sample=:sampleId and p.passId=:passId");
		q.setParameter("sampleId", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();

		List<IdNamePair> sieves = listSieves();
		for (int i = 0; i < sieves.size(); i++) {
			IdNamePair sieve = sieves.get(i);
			SievePass pass = new SievePass();
			pass.setSample(sampleId);
			pass.setSieve(sieve.getId());
			pass.setPassId(passId);
			pass.setValue(data.getValues().get(i));
			em.persist(pass);
		}
	}

	
	@Transactional
	public void delete(int sampleId, int passId) throws Exception {
		Query q = em.createQuery("delete from SievePass p where p.sample=:id and p.passId=:passId");
		q.setParameter("id", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();
	}

	@Transactional(readOnly=true)
	public List<List> getSampleData(int sampleId) {
		List<IdNamePair> sieves = listSieves();
		List<Integer> passes = listPassIds(sampleId);
		List<List> r = new ArrayList<>();
		for (IdNamePair sieve : sieves) {
			List row = new ArrayList();
			row.add(sieve);
			for (int index = 0; index < passes.size(); index++) {
				row.add(0.0);
			}
			r.add(row);
		}
		
		Query q = em.createQuery("select p from SievePass p where p.sample=:sampleId");
		q.setParameter("sampleId", sampleId);
		List<SievePass> items = q.getResultList();
		for (SievePass item : items) {
			for (List row : r) {
				IdNamePair sieve = (IdNamePair) row.get(0);
				if (sieve.getId() != item.getSieve())
					continue;
				for (int index = 0; index < passes.size(); index++) {
					Integer passId = passes.get(index);
					if (item.getPassId() != passId)
						continue;
					row.set(index + 1, item.getValue());
				}
			}
		}
		
		return r;
	}
}
