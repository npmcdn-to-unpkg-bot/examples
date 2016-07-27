package examples.spa.backend.myRest;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyRestConfigurer {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DataSource dataSource;

	@Autowired
	ObjectMapper xmlObjectMapper;
	
	MyRestConfig myRestConfig;
	
	Resource config;
	
	protected void loadConfig(Resource config) throws IOException, JDOMException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IntrospectionException, SQLException {
		if (config == null) {
			System.out.println("Resource is null.");
			return;
		}
		MyRestConfig myRestConfig = null;
		try (InputStream is = config.getInputStream()) {
			myRestConfig = xmlObjectMapper.readValue(is, MyRestConfig.class);
		}
		if (myRestConfig == null)
			return;
		if (myRestConfig.configItem == null)
			myRestConfig.configItem = new MyRestConfigItem[0];
		if (myRestConfig.rest == null)
			myRestConfig.rest = new MyRestObject[0];

		try (Connection connection = dataSource.getConnection()) {
			for (MyRestConfigItem item : myRestConfig.configItem) {
				if (item.field == null)
					item.field = new MyRestConfigField[0];
				String sql = "select * from (" + item.sql + ") t fetch first 1 row only";
				logger.debug(sql);
				try (
					PreparedStatement s = connection.prepareStatement(sql);
					ResultSet rs = s.executeQuery();
					) {
					ResultSetMetaData meta = rs.getMetaData();
					DbField dbField[] = new DbField[meta.getColumnCount()];
					for (int i = 0; i < dbField.length; i++) {
						DbField dbf = new DbField();
						dbf.assignFromRSMetaData(meta, i + 1);
						for (MyRestConfigField f : item.field) {
							if (dbf.columnName.equalsIgnoreCase(f.name)) {
								dbf.assignFromConfigField(f);
								break;
							}
						}
						dbField[i] = dbf;
					}
					item.setDbField(dbField);
				}
			}
			//////////////////
			DatabaseMetaData meta = connection.getMetaData();
			for (MyRestObject item : myRestConfig.rest) {
				// item.table;
				try (
					ResultSet rs = meta.getTables(null, null, item.getTable(), null);
					) {
					while (rs.next()) {
						
					}
				}
			}
		}
		
		this.myRestConfig = myRestConfig;
	}

	public Resource getConfig() {
		return config;
	}

	public void setConfig(Resource config) {
		this.config = config;
		try {
			loadConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MyRestConfigItem getConfigItem(String configName) {
		MyRestConfig myRestConfig = this.myRestConfig;
		if ((myRestConfig == null) || (myRestConfig.configItem == null) || (configName == null))
			return null;
		
		for (MyRestConfigItem item : myRestConfig.configItem) {
			if (item == null)
				continue;
			if (configName.equals(item.name))
				return item;
		}
		return null;
	}
}
