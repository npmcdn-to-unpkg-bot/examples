<%@include file="fragments/bodyHeader.jsp" %>

<a href="new">Add new</a>
<hr />

<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap2" >
	<datatables:column title="edit" cssStyle="width: 150px;" display="html">
		<a href="${fn:escapeXml(item.id)}"><c:out value="edit" /></a>&nbsp;|&nbsp;
		<a href="delete/${fn:escapeXml(item.id)}"><c:out value="delete" /></a>
	</datatables:column>
	<datatables:column title="Id" property="id" />
	<datatables:column title="Name" property="name" />
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
