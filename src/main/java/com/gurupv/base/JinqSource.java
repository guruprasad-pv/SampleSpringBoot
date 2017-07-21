package com.gurupv.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;
import org.springframework.stereotype.Component;

import com.gurupv.base.registration.model.Registration;
/**
 * 
 * This will configure JINQ streams
 * The data from database will be streamed as Streams {@link java.util.stream}
 *
 */
@Component
public class JinqSource {
	
	private JinqJPAStreamProvider streams;
	
	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory emf) throws Exception {
		streams = new JinqJPAStreamProvider(emf);
	}
	
	// Wrapper that passes through Jinq requests to Jinq
	public <U> JPAJinqStream<U> streamAll(EntityManager em, Class<U> entity) {
		return streams.streamAll(em, entity);
	}
	
	public JPAJinqStream<Registration> registrations(EntityManager em) {
		return streams.streamAll(em, Registration.class);
	}
	

}
