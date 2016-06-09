<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>TEST Angular JS</title>
<base href="<c:url value='/' />">
<link rel="stylesheet" href="<c:url value='/res/css/style.css' />">
<script type="text/javascript" src="<c:url value='/res/js/libs.js' />"></script>
<script type="text/javascript" src="<c:url value='/res/js/app.js' />"></script>
<script type="text/javascript">
angular.element(document).ready(function() {
	angular.bootstrap(document, ['myapp5'], {
		strictDi: true,
	});
});
</script>
</head>

<body>
	<myapp></myapp>
</body>
</html>
