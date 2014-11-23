<%@include file="importTags.include.jsp" %>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<c:if test="${not empty title}"><title>${fn:escapeXml(title)}</title></c:if>

	<spring:url value="/webjars/bootstrap/3.3.1/css/bootstrap.css" var="bootstrapCss"/>
	<link href="${bootstrapCss}" rel="stylesheet"/>

<%-- 	<spring:url value="/webjars/bootstrap/3.3.1/js/bootstrap.js" var="bootstrapJs"/>
	<script src="${bootstrapJs}" ></script>
 --%>
	<spring:url value="/resources/css/style.css" var="cementCss"/>
	<link href="${cementCss}" rel="stylesheet"/>

	<spring:url value="/webjars/jquery/2.1.1/jquery.js" var="jQuery"/>
	<script src="${jQuery}"></script>

	<!-- jquery-ui.js file is really big so we only load what we need instead of loading everything -->
	<spring:url value="/webjars/jquery-ui/1.11.2/jquery.ui.js" var="jQueryUiCore"/>
	<script src="${jQueryUiCore}"></script>

<%-- 	<spring:url value="/webjars/jquery-ui/1.11.2/jquery.ui.datepicker.js" var="jQueryUiDatePicker"/>
    <script src="${jQueryUiDatePicker}"></script>
 --%>    
    <!-- jquery-ui.css file is not that big so we can afford to load it -->
    <spring:url value="/webjars/jquery-ui/1.11.2/jquery-ui.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"></link>
<%/* 
*/%>
</head>


