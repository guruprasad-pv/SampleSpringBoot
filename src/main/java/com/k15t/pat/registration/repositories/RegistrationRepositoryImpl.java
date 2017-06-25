package com.k15t.pat.registration.repositories;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.k15t.pat.JinqSource;
import com.k15t.pat.registration.model.AddressComponent;
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private JinqSource source;

	
	@Override
	public long saveRegistraion(Registration registration) {
		
		em.persist(registration);
		em.flush();
		
		/*
		 * Try to split the address into various components
		 * components matched are currently restricted to only 5.
		 * Matches only simple German address pattern like Werbellin Strasse 69 Neukoeln 12053 Berlin,
		 * that contains, street name followed by house no, 5 digit postal code and city name
		 * The regular expression cannot match address pattern for other countries, or other exceptional 
		 * German addresses 
		 * 
		 */

		String address = registration.getAddress();
		
		String regexp ="^([^0-9]+) ([0-9]+.*?) ([0-9]{5}) (.*)$";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(address);
		boolean matchFound = matcher.find();

		if (matchFound) {
			for (int i = 1; i <= matcher.groupCount() && i<=6; i++) {
				String addressComponent = matcher.group(i);
				logger.debug("Address component matched: "+addressComponent);
				AddressComponent component = new AddressComponent();
				component.setAddresscomponent(addressComponent);
				component.setRegistration(registration);
				em.persist(component);
			}
		}

		
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

	@Override
	public List<AddressComponent> getAddressComponents(long id) {
		Registration registration = em.find(Registration.class,id);
		if(registration!=null){
			Hibernate.initialize(registration.getAdressComponents());
			return registration.getAdressComponents();
		}
		return null;
	}

}
