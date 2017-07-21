package com.gurupv.base.registration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class AddressComponent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@JsonView(Views.AddressComponentJson.class)
	@Column(length=120)
	String addresscomponent;
	
	@JsonIgnore
	@ManyToOne
	Registration registration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddresscomponent() {
		return addresscomponent;
	}

	public void setAddresscomponent(String addresscomponent) {
		this.addresscomponent = addresscomponent;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
}
