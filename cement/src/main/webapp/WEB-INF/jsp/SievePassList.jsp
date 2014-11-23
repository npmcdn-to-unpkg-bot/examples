<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-warning" href="${pagePath}../../samples">back to List</a><br/>
<br/>

<div class="panel panel-default">
	<div class="panel-heading"><h3 class="panel-title">Material</h3></div>
	<div class="panel-body">
		<app:labelField label="Location"		name="${material.location.name}" />
		<app:labelField label="Material type"	name="${material.materialType.name}" />
		<app:labelField label="Material size"	name="${material.materialSize.name}" />
		<app:labelField label="Region"			name="${material.region.name}" />
		<app:labelField label="Supplier"		name="${material.supplier.name}" />
		<app:labelField label="Comment"			name="${material.comment}"/>
	</div>
</div>

<div class="panel panel-default">
	<div class="panel-heading"><h3 class="panel-title">Sample</h3></div>
	<div class="panel-body">
		<app:labelField label="Place"			name="${sample.samplePlace}" />
		<app:labelField label="Date"			name="${sample.sampleDate}" />
		<app:labelField label="Date measure"	name="${sample.dateMeasure}" />
		<app:labelField label="Density"			name="${sample.density}" />
		<app:labelField label="Weight"			name="${sample.weight}" />
		<app:labelField label="Comment"			name="${sample.comment}"/>
	</div>
</div>

<a class="btn btn-success" href="${pagePath}../new">Add new</a>
<hr />


<datatables:table id="itemListTable" data="${model}" row="item" theme="bootstrap3" filterable="false" pageable="false" sortable="false">
	<datatables:column title="Sieve label">
		<c:out value="${item[0].name}" />
	</datatables:column>
	<c:forEach items="${passes}" varStatus="pass" var="passId">
		<datatables:column display="html">
			<datatables:columnHead>
				<c:out value="Pass ${passId}" />
				<a class="icon-edit" href="${pagePath}../${passId}" title="Edit"></a>
			</datatables:columnHead>
			<c:out value="${item[pass.index+1]}" />
		</datatables:column>
	</c:forEach>
</datatables:table>

<%@include file="fragments/bodyFooter.jsp" %>
