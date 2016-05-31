<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>TEST Angular JS</title>
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<script type="text/javascript" src="<c:url value='/js/libs.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/app.js' />"></script>
<script type="text/javascript">
angular.element(document).ready(function() {
	angular.bootstrap(document, ['myapp5'], {
		strictDi: true,
	});
});
</script>
</head>

<body>
	<h1 class="title">My Component Router</h1>
	<myapp></myapp>
</body>
</html>
