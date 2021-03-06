<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"			uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<c:url value="/" var="baseUrl" />
<!-- 
-->
	<script data-main="main" src="js/main.js" type="text/javascript"></script>
	<link href="css/style.css" rel="stylesheet"/>
</head>
<body>
<div id="container">
<p>Hi!</p>
</div>

<div ng-controller="StoreController as store">
	<h1>{{store.product.name}}</h1>
	<h2>{{store.product.price}}</h2>
	<p>{{store.product.description}}</p>
</div>

<div id="output"></div>

</body>
</html>