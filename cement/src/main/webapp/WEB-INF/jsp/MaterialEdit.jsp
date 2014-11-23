<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-warning" href="${pagePath}..">back to List</a><br/>
<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:selectField label="Location" name="location.id" names="${locations}" />
	<app:selectField label="Material type" name="materialType.id" names="${materialTypes}" />
	<app:selectField label="Material size" name="materialSize.id" names="${materialSizes}" />
	<app:selectField label="Region" name="region.id" names="${regions}" />
	<app:selectField label="Supplier" name="supplier.id" names="${suppliers}" />
	<app:inputField label="Comment" name="comment"/>
	<button class="btn btn-primary" type="submit">Save</button>
	<c:if test="${!model['new']}"><a class="btn btn-danger" href="${pagePath}delete">Delete</a></c:if>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
