<%@include file="fragments/bodyHeader.jsp" %>

<a href="${pagePath}../passes">back to List</a><br/>

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
	<c:forEach items="${model.sieve}" varStatus="item">
		<tr>
		<td><label class="control-label">${model.sieve[item.index].sieve}</label></td>
		<td><form:hidden path="sieve[${item.index}].id" />
			<form:input path="sieve[${item.index}].value" />
		</td>
		</tr>
<%-- 
		<c:set var="inputName" value="map['${item.sieve}']" />
		<app:inputField label="sieves[${item.index}].sieve" name="sieves[${item.index}].sieve" />

		<c:set var="cssGroup"
			value="control-group ${item.status.error ? 'error' : '' }" />
		<div class="${cssGroup}">
			<label class="control-label"><c:out value="${fn:escapeXml(item.label)}" /></label>
			
			<div class="controls">
				<input name="map['${item.sieve}']" type="text" value="${fn:escapeXml(item.pass)}"/>
				<span class="help-inline">${item.status.errorMessage}</span>
			</div>
		</div>
 --%>
	</c:forEach>
	</table>

	<button type="submit">Save</button>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
