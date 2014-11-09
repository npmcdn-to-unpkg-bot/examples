<%@include file="importTags.include.jsp" %>

<div>
	<ul>
		<li><a href="<spring:url value='/nom/supplier/list'/>">Supplier</a></li>
		<li><a href="<spring:url value='/nom/location/list'/>">Locations</a></li>
		<li><a href="<spring:url value='/nom/region/list'/>">Region</a></li>
		<li><a href="<spring:url value='/nom/material/type/list'/>">Material type</a></li>
		<li><a href="<spring:url value='/nom/material/size/list'/>">Material sizes</a></li>
	</ul>
</div>
<hr/>

<c:if test="${not empty message}">
	<pre>${fn:escapeXml(message)}</pre>
	<hr/>
</c:if>
