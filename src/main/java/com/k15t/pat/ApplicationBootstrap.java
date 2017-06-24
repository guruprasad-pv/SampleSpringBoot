package com.k15t.pat;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * Application bootstrap. Will inject mvc, velocity beans
 * post construct is commented. Can be used while running the
 * application to check the HSQL database contents
 *
 */
@SpringBootApplication
public class ApplicationBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationBootstrap.class, args);
	}

	/*
	 * @Bean public ServletRegistrationBean initJerseyServlet() {
	 * ServletRegistrationBean registration = new ServletRegistrationBean(new
	 * ServletContainer(), "/rest/*");
	 * registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
	 * JerseyConfig.class.getName()); return registration; }
	 */
	@Bean
	public WebMvcConfigurerAdapter forwardToIndex() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/").setViewName("forward:/registration.html");
			}
		};
	}

	@Bean
	public VelocityEngine velocityEngine() {
		VelocityEngine velocityEngine = new VelocityEngine();
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init(p);
		return velocityEngine;
	}

	
	/*
	 * This will start a Swing based Database viewer
	 * For this  to execute successfully without errors,
	 * the final jar should be run with java args -Djava.awt.headless=false
	 * 
	 */
	@PostConstruct
	public void startDBManager() {
		//DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", "" });
	}

}
