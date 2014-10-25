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
	
	DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/exampleDB");
	out.println("Found " + ds.getClass());
	Connection conn = ds.getConnection();
	out.println(conn.getClass());
	PreparedStatement st;
//	st = conn.prepareStatement("create table asd(id int)");
//	out.println(st.execute());
	st = conn.prepareStatement("select * from asd");
	out.println(st.execute());
} catch (Exception e) {
	PrintWriter w = new PrintWriter(out);
	e.printStackTrace(w);
}
%>
</pre>
</body>
</html>