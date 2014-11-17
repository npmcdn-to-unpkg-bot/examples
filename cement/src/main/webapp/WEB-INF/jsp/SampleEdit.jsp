<%@include file="fragments/bodyHeader.jsp" %>

<a href="${pagePath}../samples">back to List</a><br/>

<table style="border-width:0px;">
<app:labelField label="Location"		name="${material.location.name}" />
<app:labelField label="Material type"	name="${material.materialType.name}" />
<app:labelField label="Material size"	name="${material.materialSize.name}" />
<app:labelField label="Region"			name="${material.region.name}" />
<app:labelField label="Supplier"		name="${material.supplier.name}" />
<app:labelField label="Comment"			name="${material.comment}"/>
</table>
<hr/>

<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:inputField label="Place" name="samplePlace" />
	<app:inputField label="Date" name="sampleDate" />
	<app:inputField label="Date measure" name="dateMeasure" />
	<app:inputField label="Density" name="density" />
	<app:inputField label="Weight" name="weight" />
	<app:inputField label="Comment" name="comment"/>
	<button type="submit">Save</button>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
