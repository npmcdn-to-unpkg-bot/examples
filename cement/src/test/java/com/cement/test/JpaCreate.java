package com.cement.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.slavi.util.StringPrintStream;

public class JpaCreate {

	public static String dbToXml(DataSource dataSource) throws SQLException, IOException {
		StringPrintStream out = new StringPrintStream();
		dbToXml(dataSource, out);
		return out.toString();
	}
	
	public static void dbToXml(DataSource dataSource, OutputStream out) throws SQLException, IOException {
		Connection conn = dataSource.getConnection();
		try {
			Platform  platform = PlatformFactory.createNewPlatformInstance(dataSource);
			Database db = platform.readModelFromDatabase(conn, "cement");
			DatabaseIO dbio = new DatabaseIO();
			Writer wr = new OutputStreamWriter(out);
			dbio.write(db, wr);
			wr.close();
		} finally {
			DbUtils.close(conn);
			IOUtils.closeQuietly(out);
		}
	}

	public void createDummyTableWithData(Connection connection) throws SQLException {
		Statement st = connection.createStatement();
		st.execute("create table asd(a int, b char(10))");
		st.execute("insert into asd(a,b) values(1,'123')");
		connection.commit();
	}

	void flyWay(DataSource dataSource) throws Exception {
		System.out.println(dbToXml(dataSource));

		Flyway flyway = new Flyway();
		flyway.setLocations("com/cement/test/flyway");
		flyway.setDataSource(dataSource);
		flyway.migrate();
	}

	public void createORMs(ApplicationContext appContext) {
		EntityManagerFactory emf = appContext.getBean("entityManagerFactory", EntityManagerFactory.class);
		EntityManager em = emf.createEntityManager();
		// ... more
		em.close();
	}
	
	void doIt() throws Exception {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("JpaCreate-sping.xml", getClass());
		DataSource dataSource = appContext.getBean("dataSource", DataSource.class);

//		flyWay(dataSource);
		createORMs(appContext);
		
		dbToXml(dataSource);
		appContext.close();
	}

	public static void main(String[] args) throws Exception {
		new JpaCreate().doIt();
		System.out.println("Done.");
	}
}
