<%@include file="fragments/importTags.include.jsp" %>
<html>
<jsp:include page="fragments/headTag.jsp" />

<body>
	<jsp:include page="fragments/bodyHeader.jsp" />

	<a href="list">back to List</a><br/>
	<form:form modelAttribute="model" method="POST">
		<form:errors path="*" cssClass="errorBox" />
		<app:inputField label="Name" name="name"/>
		<button type="submit">Save</button>
	</form:form>
	<jsp:include page="fragments/bodyFooter.jsp" />
</body>
</html>
