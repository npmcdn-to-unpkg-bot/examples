<%@include file="fragments/bodyHeader.jsp" %>

<a href="list">back to List</a><br/>
<form:form modelAttribute="model" method="POST">
	<form:errors path="*" cssClass="errorBox" />
	<app:inputField label="Name" name="name"/>
	<button type="submit">Save</button>
</form:form>

<%@include file="fragments/bodyFooter.jsp" %>
