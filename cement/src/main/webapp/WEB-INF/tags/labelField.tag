<%@include file="/WEB-INF/jsp/fragments/importTagsOnly.include.jsp" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission"%>

<tr>
	<td><label class="control-label">${label}</label></td>

	<td><div class="controls" style="border-width:1px; border-style:solid;">
		<%-- <c:set target="displayValue" value="${empty(name) ? '&nbsp;' : name}" htmlEscape="true" /> --%>
		<label>${empty(name) ? "&nbsp;" :  name}</label>
	</div></td>
</tr>
