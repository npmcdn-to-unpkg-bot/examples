<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>A.JSP</title>
</head>

<body>
	<a href=".">back to List</a><br/>
	<form:form modelAttribute="myData" method="POST">
		<form:errors path="*" cssClass="errorBox" />
		<app:inputField label="Id of the record" name="id"/>
		<app:inputField label="Just a name" name="name"/>
		<button type="submit">Save</button>
	</form:form>
<hr/>
<p>A.JSP</p>
<pre>${hello}</pre>
</body>
</html>
