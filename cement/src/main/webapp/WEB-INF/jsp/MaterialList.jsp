<%@include file="fragments/bodyHeader.jsp" %>

<a href="new/">Add new</a>
<hr />

<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap2" filterable="false" >
	<datatables:column title="edit" cssStyle="width: 150px;" display="html">
		<a href="item/${fn:escapeXml(item.id)}"><c:out value="edit" /></a>&nbsp;|&nbsp;
		<a href="delete/${fn:escapeXml(item.id)}"><c:out value="delete" /></a>
	</datatables:column>
	<datatables:column title="Id" property="id" />
	<datatables:column title="Type" property="materialType.name" />
	<datatables:column title="Size" property="materialSize.name" />
	<datatables:column title="Region" property="region.name" />
	<datatables:column title="Location" property="location.name" />
	<datatables:column title="Supplier" property="supplier.name" />
	<datatables:column title="Comment" property="comment" />
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
