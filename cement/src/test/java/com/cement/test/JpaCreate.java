package com.cement.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.eclipse.persistence.expressions.ExpressionOperator;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cement.model.Region;
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
		em.getTransaction().begin();
		Region r = new Region();
		r.setName("R1");
		em.persist(r);
		System.out.println(r);
		
		r = new Region();
		r.setName("R2");
		em.persist(r);
		System.out.println(r);

		r = new Region();
		r.setName("R3");
		em.persist(r);
		System.out.println(r);
		em.getTransaction().commit();

		
		Query q = em.createQuery("select r from Region r where cast(r.id as char) like '%2%'");
		List items = q.getResultList();
		for (Object item : items)
			System.out.println(item);
		
/*		Class entityClass = Region.class;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery cq = builder.createQuery(entityClass);
		Root from = cq.from(entityClass);
		Expression exp = from.get("id").as(String.class);
		
		//exp = builder.function("CAST", String.class, exp, builder.);
		exp = builder.like(exp, "%3%");
		cq.where(exp);
		Query q = em.createQuery(cq);
		List items = q.getResultList();
		for (Object item : items)
			System.out.println(item);
		//ExpressionOperator.cast().expressionFor(exp, "");
		
*/
		
		q = em.createNativeQuery("select count(*) from mate_reg");
		System.out.println("ITEMS COUNT: " + q.getSingleResult());
		
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
		System.setProperty("derby.stream.error.method", "com.cement.misc.DerbyLogOverSlf4j.getLogger");
		new JpaCreate().doIt();
		System.out.println("Done.");
	}
}
