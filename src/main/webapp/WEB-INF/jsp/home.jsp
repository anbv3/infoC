<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>

<jsp:include page="./common/include-resources.jsp" />


<style type="text/css">

body {
line-height: 25px;
}
.color-strip {
	background: #ccc url('img/color-strip.png') no-repeat 20% top;
	height: 5px;
}


.item {
	width: 270px;
	margin-bottom: 5px;
	text-align: justify;
	word-wrap: break-word;
	
	border-width: 1px;
	border-style: solid;
	border-color: #a0a0a0 #e0e4e6 #e0e4e6;
}

.one {
	margin: 0 auto;
	padding: 20px 0 20px 0;
}

.two {
	background: #555152;
	margin: 0 auto;
	padding: 20px 0 20px 0;
}

.item_title {
padding: 8px;
border-bottom: 1px solid #e1e1e1;
background-color: #f4f6f7;;
}

.item_content {
padding: 8px;
background-color: #FCFCEE;
}

.item_link {
padding: 8px;
border-bottom: 1px solid #e1e1e1;
background-color: #f3f6fb;;
}

.two .item {
border: none;
}

.two .section-title {
color:#fff;
}

.two .item_title {
background-color: #EFFFCD;
border-top: 1px solid #99173C;
}

.two .item_content {
background-color: #FCFCEE;
}

.two .item_link {
background-color: #E6FCF4;
}


#top-section {
	margin-top: 40px;
	margin-bottom: 0;
}

.article-section {
margin-left: 15px;
}

</style>

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
						<strong>LOREM IPSUM DOLOR</strong> - 
						<span><em>Curabitur viverra nulla non tellus suscipit condimentum eget.</em></span>
					</h3>
				</div>
			</div>
		</div>
	</div>

	<div class="one">
		<div class="container-fluid">
			<div class="row-fluid article-section">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">

					<c:forEach var="row" items="${articleList}" varStatus="cnt">
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

	<div class="two">
		<div class="container-fluid">
			<div class="row-fluid article-section">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">
					
					<c:forEach var="row" items="${articleList2}" varStatus="cnt">
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


</body>

</html>