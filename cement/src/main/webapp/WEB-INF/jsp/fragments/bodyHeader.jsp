<%@include file="importTags.include.jsp" %>
<html>
<jsp:include page="/WEB-INF/jsp/fragments/headTag.jsp" />
<body>
<div class="container">

<div class="navbar">
	<div class="navbar-inner">
		<ul class="nav">
			<li><a href="<spring:url value='/material/'/>">Materials</a></li>
			<li><a href="<spring:url value='/nom/supplier/'/>">Suppliers</a></li>
			<li><a href="<spring:url value='/nom/location/'/>">Locations</a></li>
			<li><a href="<spring:url value='/nom/region/'/>">Regions</a></li>
			<li><a href="<spring:url value='/nom/material/type/'/>">Material types</a></li>
			<li><a href="<spring:url value='/nom/material/size/'/>">Material sizes</a></li>
			<li><a href="<spring:url value='/nom/sieve/'/>">Sieves</a></li>
		</ul>
	</div>
</div>
<hr/>

<c:if test="${not empty message}">
	<pre>${fn:escapeXml(message)}</pre>
	<hr/>
</c:if>

