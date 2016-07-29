package examples.spa.backend.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.dbutils.QueryRunner;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.slavi.util.db.ResultSetToString;

import examples.spa.backend.myRest.meta.MyDatabaseMeta;

public class TestMyRestConfigurer {

	private void configureObjectMapper(ObjectMapper m) {
		AnnotationIntrospector i1 = new JacksonAnnotationIntrospector();
		AnnotationIntrospector i2 = new JaxbAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospectorPair(i1, i2);
		m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		m.setSerializationInclusion(Include.NON_NULL);
		m.configure(SerializationFeature.INDENT_OUTPUT, true);
		m.setAnnotationIntrospector(pair);
	}
	
	public ObjectMapper xmlObjectMapper() {
		ObjectMapper m = new XmlMapper();
		configureObjectMapper(m);
		return m;
	}
	
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper m = new ObjectMapper();
		configureObjectMapper(m);
		return m;
	}

	void doIt() throws Exception {
		ObjectMapper mapper = xmlObjectMapper();
		
		Connection conn = DriverManager.getConnection("jdbc:derby:memory:MyDbTest;create=true");
		QueryRunner run = new QueryRunner();
		run.update(conn, "create table a(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), name varchar(100), PRIMARY KEY (id))");
		run.update(conn, "create table b(id INTEGER NOT NULL, type integer not null, name varchar(100), PRIMARY KEY (id, type))");
		run.update(conn, "create table c(aid INTEGER NOT NULL constraint c_aid_fk references a, bid INTEGER, btype integer, name varchar(100), constraint c_pk primary key (aid, bid), constraint c_b_fk foreign key (bid, btype) references b, constraint c_uk unique(aid, name))");
		run.update(conn, "create index c_indx on c(name, bid)");

		//run.update(conn, "create table c(aid INTEGER NOT NULL references a, bid INTEGER NOT NULL references b, name varchar(100), primary key (aid, bid), unique(aid, name))");

		DatabaseMetaData meta = conn.getMetaData();
		ResultSetToString rss = new ResultSetToString();
		System.out.println(rss.resultSetToString(meta.getIndexInfo(null, null, "C", false, false), -1, Integer.MAX_VALUE, true, 40));
		//System.out.println(rss.resultSetToString(meta.getColumns(null, null, "C", null), -1, Integer.MAX_VALUE, true, 40));

		String sql = "select * from a for update";
		PreparedStatement ps = conn.prepareStatement(sql, ResultSet.FETCH_FORWARD, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = ps.executeQuery();
		rs.moveToInsertRow();
		rs.updateObject("name", "Some name");
		rs.insertRow();
		rs.close();
		ps.close();
		conn.commit();
		
		ps = conn.prepareStatement("select * from a");
		rs = ps.executeQuery();
		System.out.println(rss.resultSetToString(rs, -1, Integer.MAX_VALUE, true, 40));
		rs.close();
		ps.close();
		
/*		MyDatabaseMetaParams dbParams = new MyDatabaseMetaParams();
		dbParams.load(meta);
		mapper.writeValue(System.out, dbParams);*/
		
		MyDatabaseMeta myMeta = new MyDatabaseMeta();
		myMeta.load(meta, null, "APP");
		mapper.writeValue(System.out, myMeta);

		conn.close();
	}

	public static void main(String[] args) throws Exception {
		new TestMyRestConfigurer().doIt();
		System.out.println("Done.");
	}
}
