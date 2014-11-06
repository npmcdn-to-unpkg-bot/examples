<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.slavi.util.web.ContextUtil"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My Spring Example</title>
</head>
<body>
	<p>Hi</p>
	<pre>
<%
try {
	Context ctx = new InitialContext();
	
	out.println(ContextUtil.contextToString(ctx));
	
	//DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cement");
//	org.sqlite.SQLiteDataSource ds = new org.sqlite.SQLiteDataSource();
//	ds.setUrl("jdbc:sqlite:Databases/cement.rcp");
//	out.println("Found " + ds.getClass());
	//ds.setUrl("jdbc:sqlite:Databases/cement.rcp");
	DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cement");
	Connection conn = ds.getConnection();
//	Connection conn = DriverManager.getConnection("jdbc:sqlite:Databases/cement.rcp");
	
	PreparedStatement st = conn.prepareStatement("select count(*) from mate_geo");
	ResultSet rs = st.executeQuery();
	rs.next();
	System.out.println(rs.getLong(1));
	//rs = conn.getMetaData().getTables(null, null, null, null)
	System.out.println();
	
/*	
	DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cement");
	out.println("Found " + ds.getClass());
	Connection conn = ds.getConnection();
	out.println(conn.getClass());
	conn.close();
	PreparedStatement st;
//	st = conn.prepareStatement("create table asd(id int)");
//	out.println(st.execute());
	st = conn.prepareStatement("select * from asd");
	out.println(st.execute());
*/
} catch (Exception e) {
	PrintWriter w = new PrintWriter(out);
	e.printStackTrace(w);
}
%>
</pre>
</body>
</html>