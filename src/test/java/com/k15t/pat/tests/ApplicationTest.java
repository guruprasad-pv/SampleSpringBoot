package com.k15t.pat.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k15t.pat.registration.RegistrationController;
import com.k15t.pat.registration.RegistrationResource;
import com.k15t.pat.registration.model.AddressComponent;
import com.k15t.pat.registration.model.Registration;
import com.k15t.pat.registration.model.Views;
/**
 * Test cases
 * Tests REST end points, and consequently the data store.
 * Spring application for test is started at a random port,
 * and the port number is injected for reference in tests
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	public static String MY_BASE_URL = "http://localhost:";

	public static String MY_SAVE_URL = "/rest/registration/save";

	public static String MY_GET_REG_BY_NAME_URL = "/rest/registration/name/";
	
	public static String MY_GET_REG_BY_PHONE_URL = "/rest/registration/phone/";
	
	public static String MY_GET_REG_BY_EMAIL_URL = "/rest/registration/email/";
	
	public static String MY_DELETE_REG_URL = "/rest/registration/delete/";
	
	public static String MY_GET_ADDCOMP_URL = "/rest/registration/addressComponents/";
	
	public static String MY_PHONE ="712246824594";
	
	public static String MY_EMAIL ="test@spring.new";
	
	
	private Registration newRegistration;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RegistrationController controller;

	@Autowired
	private RegistrationResource restResource;
	

	
	/**
	 * The data is setup. 
	 * A registration is saved by a rest call.
	 * Also test if the registration is actually saved
	 *
	 */
	@Before
	public void setupData() {
		
		
		newRegistration = new Registration();
		newRegistration.setName("Testname");
		newRegistration.setEmail(MY_EMAIL);
		newRegistration.setPassword("test");
		newRegistration.setAddress("Test Address");
		newRegistration.setPhoneNumber(MY_PHONE);
		
		String urlToPost = MY_BASE_URL + port;
		urlToPost += MY_SAVE_URL;


		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = mapper.writerWithView(Views.RegistratrionJson.class).writeValueAsString(newRegistration);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Some problem with the JSON mapper!!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(result, headers);

		String idStr = restTemplate.postForObject(urlToPost, entity, String.class);
		assertTrue(idStr!=null);
		newRegistration.setId(Long.parseLong(idStr));

		
		
	}

	/**
	 * Basic test to check if the spring has been initialized 
	 * is the controller initialized?
	 * is the Rest end point initialized?
	 * 
	 */
	@Test
	public void testIfSpringHasInitialized() throws Exception {
		assertThat(controller).isNotNull();
		assertThat(restResource).isNotNull();
	}
	
	/**
	 * Test by getting the saved registration by Name
	 *  
	 */
	@Test
	public void testRESTGetByName() {
		// Get the saved registration by name
		String getUrl = MY_BASE_URL + port;
		getUrl += MY_GET_REG_BY_NAME_URL;
		getUrl += "Testname";

		ResponseEntity<Registration[]> response = restTemplate.getForEntity(getUrl, Registration[].class);

		assertTrue((response.getBody()).length > 0);

		assertTrue(Arrays.asList(response.getBody()).stream()
				.filter(reg -> reg.getName().contains("Testname"))
				.count() > 0);
	}

	/**
	 * Try to get the saved Registration by using phone number.
	 * Use the REST end point getByPhone
	 * 
	 */
	@Test
	public void testRESTGetByPhone(){
		String getUrl = MY_BASE_URL + port;
		getUrl += MY_GET_REG_BY_PHONE_URL;
		getUrl += MY_PHONE;

		ResponseEntity<Registration> response = restTemplate.getForEntity(getUrl, Registration.class);
		Registration registration = response.getBody();
		assertTrue(registration != null);
		
		assertTrue(registration.getPhoneNumber().equals(MY_PHONE));
	}
	
	/**
	 * Try to get the saved Registration by using email.
	 * Use the REST end point getByEmail
	 * 
	 */

	@Test
	public void testRESTGetByEmail(){
		
		String getUrl = MY_BASE_URL + port;
		getUrl += MY_GET_REG_BY_EMAIL_URL;
		getUrl += MY_EMAIL;

		ResponseEntity<Registration> response = restTemplate.getForEntity(getUrl, Registration.class);
		Registration registration = response.getBody();
		assertTrue(registration != null);
		
		assertTrue(registration.getEmail().equals(MY_EMAIL));
		
	}
	
	/**
	 * This test will check if the REST save is actually idempotent
	 * The test will try to save a registration with already existing phone number,
	 * and will compare the IDs returns if it is same as existing registration
	 */
	@Test
	public void testDoubleRegistrationBySamePhoneNumber(){
		String urlToPost = MY_BASE_URL + port;
		urlToPost += MY_SAVE_URL;

		Registration duplicateRegistration = new Registration();
		duplicateRegistration.setName("Duplicate name");
		duplicateRegistration.setPhoneNumber(MY_PHONE);
		duplicateRegistration.setAddress("test");
		duplicateRegistration.setPassword("test");
		duplicateRegistration.setEmail("test@email.com");
		
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = mapper.writerWithView(Views.RegistratrionJson.class).writeValueAsString(newRegistration);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Some problem with the JSON mapper!!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(result, headers);

		String idStr = restTemplate.postForObject(urlToPost, entity, String.class);
		assertTrue(idStr!=null);

		//Check if the returned id is same as existing id
		
		assertTrue(Long.parseLong(idStr) == newRegistration.getId());
		
	}
	
	/**
	 * Tests to see if address is split into components
	 * 
	 */
	public void testForAddressComponents(){
		newRegistration.setAddress("Werbellin Strasse 69 Neukoeln 12053 Berlin");
		newRegistration.setPhoneNumber("123456789012");
		newRegistration.setEmail("test@email.com");
		newRegistration.setName("Test name");
		
		String urlToPost = MY_BASE_URL + port;
		urlToPost += MY_SAVE_URL;


		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = mapper.writerWithView(Views.RegistratrionJson.class).writeValueAsString(newRegistration);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail("Some problem with the JSON mapper!!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(result, headers);

		String idStr = restTemplate.postForObject(urlToPost, entity, String.class);
		assertTrue(idStr!=null);
		
		String getUrl = MY_BASE_URL + port;
		getUrl += MY_GET_ADDCOMP_URL;
		getUrl += idStr;

		ResponseEntity<AddressComponent[]> response = restTemplate.getForEntity(getUrl, AddressComponent[].class);
		AddressComponent[] components = response.getBody();
		
		//Check if address components are created
		assertTrue(components.length>2);
		
		String deleteUrl = MY_BASE_URL + port;
		deleteUrl += MY_DELETE_REG_URL;
		deleteUrl += idStr;

		ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteUrl,HttpMethod.DELETE,null,String.class);
		assertThat(deleteResponse.getBody()).contains("DELETED");

		
	}
	
	/**
	 * Clean up the data after each test
	 */
	@After
	public void tearDownData() {
		String deleteUrl = MY_BASE_URL + port;
		deleteUrl += MY_DELETE_REG_URL;
		deleteUrl += newRegistration.getId();

		ResponseEntity<String> response = restTemplate.exchange(deleteUrl,HttpMethod.DELETE,null,String.class);
		assertThat(response.getBody()).contains("DELETED");
	
	}

}
