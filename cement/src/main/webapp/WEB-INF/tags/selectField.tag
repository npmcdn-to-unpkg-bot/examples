<%@include file="/WEB-INF/jsp/fragments/importTagsOnly.include.jsp" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="names" required="true" rtexprvalue="true" type="java.util.List"
	description="Names in the list" %>
<%@ attribute name="size" required="false" rtexprvalue="true"
	description="Size of Select" %>
<%@ attribute name="allowEmpty" required="false" rtexprvalue="true" type="java.lang.Boolean"
	description="Shall an empty selection be put as a first element" %>

<c:set var="size" value="${(empty size) ? 1 : size}" />
<c:set var="allowEmpty" value="${(empty allowEmpty) ? true : allowEmpty}" />

<spring:bind path="${name}">
	<c:set var="cssGroup" value="control-group ${status.error ? 'error' : '' }"/>
	<div class="${cssGroup}">
		<label class="control-label">${label}</label>

		<div class="controls">
			<form:select path="${name}" size="${size}">
				<c:if test="${allowEmpty}">
					<form:option value="" label="---"/>
				</c:if>
				<form:options items="${names}" itemValue="id" itemLabel="name" />
			</form:select>
			<span class="help-inline">${status.errorMessage}</span>
		</div>
	</div>
</spring:bind>
