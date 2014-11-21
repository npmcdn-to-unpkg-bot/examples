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
import com.cement.model.IdName;
import com.cement.model.SievePassJustIds;

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
	public List<IdName> listSieves() {
		Query q = em.createNamedQuery("SieveName_list");
		List<IdName> result = new ArrayList<>();
		result.add(new IdName(-1, "Value -1"));
		result.add(new IdName(0, "Value 0"));
		result.addAll(q.getResultList());
		
		return result;
	}

	@Transactional(readOnly=true)
	public SievePassForm makeNew() {
		Collection<IdName> sieves = listSieves();
		SievePassForm result = new SievePassForm();
		for (IdName sieve : sieves) {
			result.getValues().add(0.0);
		}
		return result;
	}

	@Transactional(readOnly=true)
	public SievePassForm load(int sampleId, int passId) {
		SievePassForm r = new SievePassForm();
		Query q = em.createQuery("select p.value val from SievePassJustIds p where p.sample=:sampleId and p.sieve=:sieveId and p.passId=:passId");
		q.setParameter("sampleId", sampleId);
		q.setParameter("passId", passId);
		for (IdName sieve : listSieves()) {
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
		Query q = em.createQuery("delete from SievePass p where p.sample.id=:sampleId and p.passId=:passId");
		q.setParameter("sampleId", sampleId);
		q.setParameter("passId", passId);
		q.executeUpdate();

		List<IdName> sieves = listSieves();
		for (int i = 0; i < sieves.size(); i++) {
			IdName sieve = sieves.get(i);
			SievePassJustIds pass = new SievePassJustIds();
			pass.setSample(sampleId);
			pass.setSieve(sieve.getId());
			pass.setPassId(passId);
			pass.setValue(data.getValues().get(i));
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
