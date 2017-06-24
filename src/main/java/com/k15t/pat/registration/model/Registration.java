package com.k15t.pat.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * This is the JPA model used to store the registration information.
 * JsonView class {@link Views.RegistratrionJson} are associated to 
 * generate only those fields that are required while exchanging JSON
 * <br>
 * Appropriate column lengths are set avoid flooding of data by malicious code.
 * <br>
 * toString method is overridden to facilitate logging of values for debugging
 * <br>
 * Phone number and email is made unique
 */

@Entity
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@JsonView(Views.RegistratrionJson.class)
	@Column(length = 60)
	private String name;

	@JsonView(Views.RegistratrionJson.class)
	@Column(length = 20)
	private String password;

	@JsonView(Views.RegistratrionJson.class)
	@Column(length = 120)
	private String address;

	@JsonView(Views.RegistratrionJson.class)
	@Column(length = 70,unique = true)
	private String email;

	@JsonView(Views.RegistratrionJson.class)
	@Column(length = 15,unique = true)
	private String phoneNumber;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Registration Info =>{");
		buff.append("Name: " + getName());
		buff.append(" ").append("Email: ").append(getEmail());
		buff.append(" ").append("Phone: ").append(getPhoneNumber());
		buff.append("}");
		return buff.toString();
	}


}
