<%@include file="fragments/bodyHeader.jsp" %>

<a href="../list">back to List</a><br/>
<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:selectField label="Material type" name="materialType" names="${materialTypes}" size="10" />
	<app:inputField label="Comment" name="comment"/>
	<button type="submit">Save</button>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
