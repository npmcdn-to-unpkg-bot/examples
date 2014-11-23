<%@include file="/WEB-INF/jsp/fragments/importTagsOnly.include.jsp" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission"%>

<div class="label-div">
	<span>${label}</span><span class="label-value">${empty(name) ? "&nbsp;" :  name}</span>
</div>
