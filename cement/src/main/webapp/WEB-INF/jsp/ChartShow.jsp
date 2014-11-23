<%@include file="fragments/bodyHeader.jsp" %>

<a class="btn btn-warning" href="${pagePath}../..">back to List</a>
<hr/>

<p>${chart.name}</p>
<hr/>

<canvas id='myChart' width="1000px" height="500px">
</canvas>

<spring:url value="/webjars/chartjs/1.0.1-beta.2/Chart.min.js" var="chartjs"/>
<script type='text/javascript' src="${chartjs}"></script>
<script type='text/javascript'>
(function () {
	var ctx = document.getElementById('myChart').getContext('2d');
	var data = {
		labels: [${labels}],
		datasets: [
	<c:forEach items="${datasets}" var="dataset">
			{
				strokeColor: "${dataset[1]}",
				data : [${dataset[2]}]
			},
	</c:forEach>
	]};
	var options = {
		animation: false,
		bezierCurve : false,
		pointDot: false,
		datasetStrokeWidth: 2,
		datasetStroke: false,
		datasetFill: false
	};
	var chartBikers = new Chart(ctx).Line(data,options);
})();
</script>
<hr/>
<div style="margin-left: auto; margin-right: auto; width: 70%;">
	<p>Legend:</p>
	<div>
	<c:forEach items="${datasets}" var="dataset">
		<span style="text-align: center; min-width: 150px; float: left; margin-left: 7px; border-style: solid; border-width: 1px; border-radius: 6px; border-color: ${dataset[1]};">
			${dataset[0]}
		</span>
	</c:forEach>
	</div>
</div>

<div>&nbsp;</div>

<%@include file="fragments/bodyFooter.jsp" %>
