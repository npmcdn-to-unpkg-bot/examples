<%@include file="fragments/bodyHeader.jsp" %>

<a href="${pagePath}../../samples">back to List</a><br/>

<table style="border-width:0px;">
<app:labelField label="Location"		name="${material.location.name}" />
<app:labelField label="Material type"	name="${material.materialType.name}" />
<app:labelField label="Material size"	name="${material.materialSize.name}" />
<app:labelField label="Region"			name="${material.region.name}" />
<app:labelField label="Supplier"		name="${material.supplier.name}" />
<app:labelField label="Comment"			name="${material.comment}"/>

<tr><td><hr/></td></tr>

<app:labelField label="Place"			name="${sample.samplePlace}" />
<app:labelField label="Date"			name="${sample.sampleDate}" />
<app:labelField label="Date measure"	name="${sample.dateMeasure}" />
<app:labelField label="Density"			name="${sample.density}" />
<app:labelField label="Weight"			name="${sample.weight}" />
<app:labelField label="Comment"			name="${sample.comment}"/>
</table>
<hr/>

<a href="${pagePath}../new">Add new</a>
<hr />


<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap2" filterable="false" pageable="false" sortable="false">
	<datatables:column title="Sieve label" property="label" />
	<c:forEach items="${passes}" var="passId">
		<datatables:column display="html">
			<datatables:columnHead>
				<c:out value="Pass ${passId}" /><a href="${pagePath}../${passId}">[edit]</a>&nbsp;|&nbsp;<a href="delete/${passId}">[x]</a>
			</datatables:columnHead>
			<c:out value="${item[passId]}" />
		</datatables:column>
	</c:forEach>
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
