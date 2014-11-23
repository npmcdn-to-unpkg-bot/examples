<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-success" href="${pagePath}new">Add new</a>
<hr />

<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap3" filterable="false" >
	<datatables:column title="edit" cssStyle="width: 150px;" display="html">
		<a class="icon-edit" href="${pagePath}${item.id}" title="Edit"></a>
		<a class="icon-view" href="${pagePath}${item.id}/samples" title="View"></a>
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
