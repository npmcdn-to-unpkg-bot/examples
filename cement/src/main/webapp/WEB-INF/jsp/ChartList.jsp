<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-success" href="${pagePath}new">Add new</a>
<hr />

<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap3" >
	<datatables:column title="" cssStyle="width: 150px;" display="html">
		<a class="icon-edit" href="${pagePath}${item.id}" title="Edit"></a>
		<a class="icon-view" href="${pagePath}${item.id}/view" target="showChart" title="View"></a>
	</datatables:column>
	<datatables:column title="Name" property="name" />
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
