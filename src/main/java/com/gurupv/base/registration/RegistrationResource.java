package com.gurupv.base.registration;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gurupv.base.registration.model.AddressComponent;
import com.gurupv.base.registration.model.Registration;
import com.gurupv.base.registration.repositories.RegistrationRepository;

/**
 * 
 * The REST end point to save and retrieve {@link Registration}
 *
 */

@Path("/registration")
@Component
public class RegistrationResource {

 	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RegistrationRepository repository;
    
	/**
	 * The End point method creates a new registration hence accepts HTTP POST and content type should be JSON
	 * To make this method idempotent already registered id is served back if the phone number or email address
	 * is already present in the data store.
	 * Ideally an error should be returned if the phone number or email is already present, But for this task
	 * It is assumed that a user will not try to register with same phone or email different names
	 * @param registration Accepts a Json object that can be converted to {@link Registration}
	 * @return Returns the id created for the registration in the data store
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save")
	public Response save(Registration registration) {
		logger.debug("#############Received Save##################");
		logger.debug(registration.toString());
		
		if(registration.getPhoneNumber() == null || registration.getEmail() == null){
			return Response.serverError().build();
		}else {
			Registration registrationph = repository.getRegistraionByPhone(registration.getPhoneNumber());
			if(registrationph!=null) return Response.ok(registrationph.getId(),MediaType.APPLICATION_JSON).build();
			else{
				Registration registrationem = repository.getRegistrationByEmail(registration.getEmail());
				if(registrationem != null) return Response.ok(registrationem.getId(),MediaType.APPLICATION_JSON).build(); 
				else{
					long id = repository.saveRegistraion(registration);
					return Response.ok(id,MediaType.APPLICATION_JSON).build();
				}
			}
		}
		
    }

	/**
	 * The End point method returns all the registrations present in the data store
	 * Accepts HTTP GET request and returns the JSON list of {@link Registration}
	 * @return Returns all the {@link Registration} present in the data store as JSON List
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/listAll")
	public Response listAll() {
		logger.debug("#############Received List All##################");
				
		List<Registration> registrations = repository.getAllRegistrations();
		logger.debug("Registrations => "+registrations);
		
        return Response.ok(registrations,MediaType.APPLICATION_JSON).build();
    }
	
	/**
	 * The End point method returns all the registrations that contain the provided
	 * name. Accepts HTTP GET request and returns the JSON list of {@link Registration}
	 * @param name The name of the registered person
	 * @return Returns all the registrations that contain the provided name
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/name/{name}")
	public Response getByName(@PathParam("name") String name) {
		logger.debug("#############Received Get by Name##################");
				
		List<Registration> registrations = repository.getRegistratonsByname(name);
		logger.debug("Registrations => "+registrations);
		
        return Response.ok(registrations,MediaType.APPLICATION_JSON).build();
    }

	/**
	 * The End  point method returns the {@link Registration} that was registered
	 * by the phone number passed as the parameter
	 * @param phone The phone number of the registered person
	 * @return Returns the registration as JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/phone/{phone}")
	public Response getByPhone(@PathParam("phone") String phone) {
		logger.debug("#############Received Get by Phone##################");
				
		Registration registration = repository.getRegistraionByPhone(phone);
		logger.debug("Registration => "+registration);
		if (registration == null) return Response.ok("",MediaType.APPLICATION_JSON).build();
		else return Response.ok(registration,MediaType.APPLICATION_JSON).build();
    }

	/**
	 * The End  point method returns the {@link Registration} that was registered
	 * by the email id passed as the parameter
	 * @param email The email id of the registered person
	 * @return Returns the registration as JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/email/{email}")
	public Response getByEmail(@PathParam("email") String email) {
		logger.debug("#############Received Get by Email##################");
				
		Registration registration = repository.getRegistrationByEmail(email);
		logger.debug("Registration => "+registration);
		if (registration == null) return Response.ok("",MediaType.APPLICATION_JSON).build();
		else return Response.ok(registration,MediaType.APPLICATION_JSON).build();
    }

	/**
	 * The end point method deletes the registration having the id passed as parameter
	 * The Delete request should be sent using HTTP DELETE
	 * @param id Id of the registration to be deleted
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{id}")
	public Response deleteRegistration(@PathParam("id") long id){
		logger.debug("#############Received DELETE##################");
		logger.debug("ID: "+id);
		repository.deleteRegistration(id);
		return Response.ok("DELETED",MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addressComponents/{id}")
	public Response getAddressComponents(@PathParam("id") long id) {
		logger.debug("#############Received Get Address Components##################");
				
		List<AddressComponent> components = repository.getAddressComponents(id);

		if (components == null) return Response.ok("",MediaType.APPLICATION_JSON).build();
		else return Response.ok(components,MediaType.APPLICATION_JSON).build();
    }

	
	
}
