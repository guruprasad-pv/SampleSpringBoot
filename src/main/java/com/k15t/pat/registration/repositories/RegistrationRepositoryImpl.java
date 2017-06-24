package com.k15t.pat.registration.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.k15t.pat.JinqSource;
import com.k15t.pat.registration.model.Registration;

/**
 * 
 * Implementation of the {@link RegistrationRepository}. This is a JPA
 * repository that handles storage and querying of {@link Registration} model.
 * <br>
 * {@link EntityManager} reference is autowired. Currently the entitymanager
 * will be provided by Hibernate <br>
 * {@link JinqSource} is a helper class to compose the returned data from data
 * store as a Stream @see java.util.stream
 * 
 */

@Repository
@Transactional
public class RegistrationRepositoryImpl implements RegistrationRepository {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private JinqSource source;

	@Override
	public long saveRegistraion(Registration registration) {
		em.persist(registration);
		em.flush();
		return (registration.getId());
	}

	@Override
	public List<Registration> getAllRegistrations() {
		return source.registrations(em).toList();
	}

	@Override
	public List<Registration> getRegistratonsByname(String name) {
		return source.registrations(em).where(reg -> reg.getName().contains(name)).toList();
	}

	@Override
	public Registration getRegistraionByPhone(String phoneNumber) {
		if (source.registrations(em).where(reg -> reg.getPhoneNumber().equals(phoneNumber)).count() == 1)
			return source.registrations(em).where(reg -> reg.getPhoneNumber().equals(phoneNumber)).getOnlyValue();
		else
			return null;
	}

	@Override
	public Registration getRegistrationByEmail(String email) {
		if (source.registrations(em).where(reg -> reg.getEmail().equals(email)).count() == 1)
			return source.registrations(em).where(reg -> reg.getEmail().equals(email)).getOnlyValue();
		else
			return null;
	}

	
	@Override
	public void deleteRegistration(long id) {
		Registration registration = em.find(Registration.class, id);
		em.remove(registration);
		em.flush();
	}

}
