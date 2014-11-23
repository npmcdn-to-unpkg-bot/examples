<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-warning" href="${pagePath}..">back to List</a><br/>
<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:inputField label="Name" name="name"/>
	<button class="btn btn-primary" type="submit">Save</button>
	<c:if test="${!model['new']}"><a class="btn btn-danger" href="${pagePath}delete">Delete</a></c:if>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
