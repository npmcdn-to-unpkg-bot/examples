<%@include file="fragments/bodyHeader.jsp" %>

<a href=".">back to List</a><br/>
<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:selectField label="Location" name="location.id" names="${locations}" />
	<app:selectField label="Material type" name="materialType.id" names="${materialTypes}" />
	<app:selectField label="Material size" name="materialSize.id" names="${materialSizes}" />
	<app:selectField label="Region" name="region.id" names="${regions}" />
	<app:selectField label="Supplier" name="supplier.id" names="${suppliers}" />
	<app:inputField label="Comment" name="comment"/>
	<button type="submit">Save</button>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
