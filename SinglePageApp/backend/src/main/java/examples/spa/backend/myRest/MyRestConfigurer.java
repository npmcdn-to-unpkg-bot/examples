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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import examples.spa.backend.component.EntityWithIdControllerBase;
import examples.spa.backend.component.UtilsService;
import examples.spa.backend.myRest.meta.MyDatabaseMeta;
import examples.spa.backend.myRest.ui.MyFormMeta;

public class MyRestConfigurer {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UtilsService utils;
	
	@Autowired
	DataSource dataSource;

	@Autowired
	ObjectMapper xmlObjectMapper;
	
	MyRestConfig myRestConfig;
	MyDatabaseMeta myDatabaseMeta;
	
	Resource config;
	
	@Autowired
	// idea borrowed from: http://stackoverflow.com/questions/10898056/how-to-find-all-controllers-in-spring-mvc
	RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	@PostConstruct
	void initialize() throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			myDatabaseMeta = new MyDatabaseMeta();
			DatabaseMetaData meta = conn.getMetaData();
			myDatabaseMeta.load(meta, null, null);
		}
		logger.debug(utils.toJson(myDatabaseMeta));

		loadConfig(config);
		
		Set<Class> controllers = new HashSet<>();
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> i : handlerMethods.entrySet()) {
			//i.getKey().getPatternsCondition().getPatterns().iterator().next()
			// i.getValue().getBeanType()MethodAnnotation(annotationType)
			Class clazz = i.getValue().getBeanType();
			if (EntityWithIdControllerBase.class.isAssignableFrom(clazz))
				controllers.add(clazz);
		}
		
		Pattern pattern = Pattern.compile("^(/?)([^/]+)(/?)");
		for (Class clazz : controllers) {
			RequestMapping ann = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
			String path = StringUtils.trimToNull((ann != null && ann.value() != null && ann.value().length > 0) ? ann.value()[0] : null);
			if (path == null) {
				logger.warn("No RequestMapping annotation for class " + clazz);
				continue;
			}
			Matcher matcher = pattern.matcher(path);
			String name = matcher.find() ? StringUtils.trimToNull(matcher.group(2)) : null;
			if (name == null) {
				logger.warn("Invalid RequestMapping annotation for class " + clazz);
				continue;
			}
			MyFormMeta formMeta = new MyFormMeta();
			formMeta.baseUrl = "/api" + path;
			formMeta.name = name;
			formMeta.label = StringUtils.capitalize(name);
			formMeta.bestRowIdColumns.add("id");
			// formMeta.fields
		}
	}
	
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
		
		// Analyze config
		for (MyRestObject item : myRestConfig.getRest()) {
			item.baseUrl = StringUtils.trimToNull(item.baseUrl);
			if (item.baseUrl == null) {
				item.baseUrl = "api/rest/" + item.name;
			}
			item.label = StringUtils.trimToNull(item.label);
			if (item.label == null) {
				item.label = item.name;
			}
			
		}
		
		this.myRestConfig = myRestConfig;
	}

	public Resource getConfig() {
		return config;
	}

	public void setConfig(Resource config) {
		this.config = config;
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
	
	public MyRestConfig getMyRestConfig() {
		return this.myRestConfig;
	}
	
	public MyDatabaseMeta getMyDatabaseMeta() {
		return myDatabaseMeta;
	}
}
