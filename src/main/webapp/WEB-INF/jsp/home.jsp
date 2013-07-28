<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>

<jsp:include page="./common/include-resources.jsp" />


<script type="text/javascript">
<!--

$(function() {
	
	$("#ttt").on("click", function() {
		test();
       });
	
	$('.story').each(function() {
		var $container = $(this);
		$container.imagesLoaded(function() {
			//$container.packery({
			$container.packery({
				itemSelector: '.item',
				gutter: 15
			});
		});
	});
	
});
//-->
</script>

</head>


<body>
	<!-- header -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="index.html">InfoC</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="#">Home</a></li>
						<li class="active"><a href="#">IT</a></li>
						<li><a href="#">Contact</a></li>

					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
		<div class="color-strip"></div>
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<!-- body -->
	
	<div id="top-section" class="well well-large">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12 text-center">
					<h3>
						<strong>여러 기사 타이틀</strong> - 
						<span><em>흘러가는 영역</em></span>
					</h3>
				</div>
			</div>
		</div>
	</div>

<!-- From now to past -->
<c:forEach var="entry" items="${articleMap}">

	<c:if test="${(entry.key % 2) == 0}">
	    <c:set var="rowColor" value="one"/>
	</c:if>    
	<c:if test="${(entry.key % 2) != 0}">
		<c:set var="rowColor" value="two"/>
	</c:if>
	
	<c:if test="${entry.key <= currentHour}">

	<div class="${rowColor}">
		<div class="container-fluid">
			<div class="row-fluid article-section">

				<div class="span2 section-title text-left">
					<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
				</div>

				<div class="span10">
					<div class="story">

					<c:forEach var="row" items="${entry.value}" varStatus="cnt">
						<div class="item w2">
							<div class="item_title">${row.title}</div>
							<div class="item_content">${row.contents}</div>
							<div class="item_link"><a href="${row.link}" target="_blank">${row.link}</a></div>
						</div>
					</c:forEach>
						
					</div>
				</div>
				
			</div>
		</div>
	</div>
	
	</c:if>
    
</c:forEach>


<!-- From the rest -->
<c:forEach var="entry" items="${articleMap}">

	<c:if test="${(entry.key % 2) == 0}">
	    <c:set var="rowColor" value="one"/>
	</c:if>    
	<c:if test="${(entry.key % 2) != 0}">
		<c:set var="rowColor" value="two"/>
	</c:if>
	
	<c:if test="${entry.key > currentHour}">

	<div class="${rowColor}">
		<div class="container-fluid">
			<div class="row-fluid article-section">

				<div class="span2 section-title text-left">
					<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
				</div>

				<div class="span10">
					<div class="story">

					<c:forEach var="row" items="${entry.value}" varStatus="cnt">
						<div class="item w2">
							<div class="item_title">${row.title}</div>
							<div class="item_content">${row.contents}</div>
							<div class="item_link"><a href="${row.link}" target="_blank">${row.link}</a></div>
						</div>
					</c:forEach>
						
					</div>
				</div>
				
			</div>
		</div>
	</div>
	
	</c:if>
    
</c:forEach>
	

</body>

</html>