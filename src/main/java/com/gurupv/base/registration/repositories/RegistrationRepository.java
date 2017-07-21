package com.gurupv.base.registration.repositories;

import java.util.List;

import javax.inject.Named;

import com.gurupv.base.registration.model.AddressComponent;
import com.gurupv.base.registration.model.Registration;
/**
 * 
 * Repository interface to handle the {@link Registration} model.
 * Suitable implementation to be provided using JPA to store the model
 *
 */

@Named
public interface RegistrationRepository {

	public long saveRegistraion(Registration registration);
	
	public List<Registration> getAllRegistrations();
	
	public List<Registration> getRegistratonsByname(String name);
	
	public Registration getRegistraionByPhone(String phoneNumber);
	
	public Registration getRegistrationByEmail(String email);
	
	public void deleteRegistration(long id);
	
    public List<AddressComponent> getAddressComponents(long id);	
}
