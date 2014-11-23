<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-warning" href="${pagePath}../passes">back to List</a><br/>

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

<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<table>
	<c:forEach items="${sieves}" varStatus="item" var="sieve">
		<tr>
			<td><app:inputField2 label="${sieve.name}" name="values[${item.index}]" />
			</td>
		</tr>
	</c:forEach>
	</table>

	<button class="btn btn-primary" type="submit">Save</button>
	<c:if test="${!isNew}"><a class="btn btn-danger" href="${pagePath}delete">Delete</a></c:if>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
