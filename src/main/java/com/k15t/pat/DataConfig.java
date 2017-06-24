package com.k15t.pat;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * This will configure an embedded database HSQL
 * and {@link JinqSource}
 * @see JinqSource
 *
 */
@Configuration
@EnableTransactionManagement
public class DataConfig {
	
	@Bean
	public DataSource dataSource() {

		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
			.setType(EmbeddedDatabaseType.HSQL) 
			.build();
		return db;
	}
	
	@Bean
	public JinqSource jinqSource() {
		return new JinqSource();
	}

}
