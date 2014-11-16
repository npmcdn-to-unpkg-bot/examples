<%@include file="fragments/bodyHeader.jsp" %>

<a href="..">back to material</a><br/>

<table style="border-width:0px;">
<app:labelField label="Location"		name="${material.location.name}" />
<app:labelField label="Material type"	name="${material.materialType.name}" />
<app:labelField label="Material size"	name="${material.materialSize.name}" />
<app:labelField label="Region"			name="${material.region.name}" />
<app:labelField label="Supplier"		name="${material.supplier.name}" />
<app:labelField label="Comment"			name="${material.comment}"/>
</table>
<hr/>

<a href="new/">Add new</a>
<hr />

<datatables:table id="itemListTable" data="${material.samples}" row="item" theme="bootstrap2" filterable="false" >
	<datatables:column title="edit" cssStyle="width: 150px;" display="html">
		<a href="${fn:escapeXml(item.id)}"><c:out value="edit" /></a>&nbsp;|&nbsp;
		<a href="${fn:escapeXml(item.id)}/passes"><c:out value="view" /></a>&nbsp;|&nbsp;
		<a href="delete/${fn:escapeXml(item.id)}"><c:out value="delete" /></a>
	</datatables:column>
	<datatables:column title="Id" property="id" />
	<datatables:column title="Place" property="samplePlace" />
	<datatables:column title="Date" property="sampleDate" />
	<datatables:column title="Date measure" property="dateMeasure" />
	<datatables:column title="Density" property="density" />
	<datatables:column title="Weight" property="weight" />
	<datatables:column title="Comment" property="comment" />
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
