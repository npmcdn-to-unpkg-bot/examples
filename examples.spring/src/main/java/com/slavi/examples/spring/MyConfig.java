package com.slavi.examples.spring;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MyConfig {

	Logger log = LoggerFactory.getLogger(getClass());

	DataSource dataSource;

	QueryRunner queryRunner;
	
	public MapListHandler mapListHandler = new MapListHandler();
	
	public MapHandler mapHandler = new MapHandler();
	
	public ArrayListHandler arrayListHandler = new ArrayListHandler();
	
	public ScalarHandler scalarHandler = new ScalarHandler();
	
	public MyConfig() {}
	
	@Autowired
	public MyConfig(DataSource dataSource) {
		this.dataSource = dataSource;
		queryRunner = new QueryRunner(dataSource);
	}
	
	public List<Map<String, Object>> queryAsMapList(String sql, Object ... params) {
		try {
			return queryRunner.query(sql, mapListHandler, params);
		} catch (SQLException e) {
			log.error("Error getting data", e);
		}
		return null;
	}
	
	public Map<String, Object> queryAsMap(String sql, Object ... params) {
		try {
			return queryRunner.query(sql, mapHandler, params);
		} catch (SQLException e) {
			log.error("Error getting data", e);
		}
		return null;
	}
	
	public Object queryAsObject(String sql, Object ... params) {
		try {
			return queryRunner.query(sql, scalarHandler, params);
		} catch (SQLException e) {
			log.error("Error getting data", e);
		}
		return null;
	}

	public void dummy() {
		try {
			System.out.println("Time on DB server is " + queryAsObject("SELECT CURRENT_TIMESTAMP FROM SYSIBM.SYSDUMMY1"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
