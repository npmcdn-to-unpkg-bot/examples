<%@include file="importTags.include.jsp" %>
<html>
<jsp:include page="/WEB-INF/jsp/fragments/headTag.jsp" />
<body>
<div class="container">

<div class="navbar">
	<div class="navbar-inner">
		<ul class="nav">
			<li><a href="<spring:url value='/material/'/>">Material</a></li>
			<li><a href="<spring:url value='/nom/supplier/'/>">Supplier</a></li>
			<li><a href="<spring:url value='/nom/location/'/>">Locations</a></li>
			<li><a href="<spring:url value='/nom/region/'/>">Region</a></li>
			<li><a href="<spring:url value='/nom/material/type/'/>">Material type</a></li>
			<li><a href="<spring:url value='/nom/material/size/'/>">Material sizes</a></li>
		</ul>
	</div>
</div>
<hr/>

<c:if test="${not empty message}">
	<pre>${fn:escapeXml(message)}</pre>
	<hr/>
</c:if>

