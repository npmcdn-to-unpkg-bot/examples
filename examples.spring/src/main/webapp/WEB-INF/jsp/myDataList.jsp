<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>A.JSP</title>
</head>

<body>
	<hr />
	${fn:escapeXml(myDataList.size())}
	<hr />
	<p>A.JSP</p>
	<pre>${hello}</pre>
	<hr />

	<datatables:table id="myDataListTable" data="${myDataList}" >
		<datatables:column title="Name" property="name" />
	</datatables:table>
	<hr />
<%
/*

	<datatables:table id="myDataList" data="${myDataList}" row="myData" theme="bootstrap2" cssClass="table table-striped"
		pageable="false" info="false" export="pdf">
		<datatables:column title="Id" cssStyle="width: 150px;" display="html">
			<spring:url value="{id}" var="myDataUrl">
				<spring:param name="ownerId" value="${myData.id}" />
			</spring:url>
			<a href="${fn:escapeXml(myDataUrl)}"><c:out value="${myData.name} ${myData.body}" /></a>
		</datatables:column>
		<datatables:column title="Body" display="pdf">
			<c:out value="${myData.name} ${myData.body}" />
		</datatables:column>
		<datatables:export type="pdf" cssClass="btn" cssStyle="height: 25px;" />
	</datatables:table>
*/
%>
</body>
</html>
