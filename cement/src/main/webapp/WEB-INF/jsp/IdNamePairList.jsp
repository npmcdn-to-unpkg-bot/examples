<%@include file="fragments/importTags.include.jsp" %>
<html>
<jsp:include page="fragments/headTag.jsp" />

<body>
	<jsp:include page="fragments/bodyHeader.jsp" />

	<a href="new/">Add new</a>
	<hr />

	<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap2" >
		<datatables:column title="edit" cssStyle="width: 150px;" display="html">
			<a href="item/${fn:escapeXml(item.id)}"><c:out value="edit" /></a>&nbsp;|&nbsp;
			<a href="delete/${fn:escapeXml(item.id)}"><c:out value="delete" /></a>
		</datatables:column>
		<datatables:column title="Id" property="id" />
		<datatables:column title="Name" property="name" />
	</datatables:table>
	<jsp:include page="fragments/bodyFooter.jsp" />
</body>
</html>
