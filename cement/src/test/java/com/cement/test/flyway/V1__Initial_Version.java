package com.cement.test.flyway;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

public class V1__Initial_Version implements JdbcMigration {

	public void migrate(Connection connection) throws Exception {
		System.out.println("Migrate");
	}

}
