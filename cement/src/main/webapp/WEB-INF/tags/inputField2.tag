<%@include file="/WEB-INF/jsp/fragments/importTagsOnly.include.jsp" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission"%>

<spring:bind path="${name}">
	<c:set var="cssGroup"
		value="control-group ${status.error ? 'error' : '' }" />
	<div class="${cssGroup}">
		<label class="control-label">${label}</label>
	</div>
	<div class="${cssGroup}">
		<form:input path="${name}" />
		<c:if test="${status.error}"><span class="help-inline">${status.errorMessage}</span></c:if>
	</div>
</spring:bind>