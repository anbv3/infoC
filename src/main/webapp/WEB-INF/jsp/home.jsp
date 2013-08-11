<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>

<jsp:include page="./common/resources.jsp" />



<style type="text/css" media="only screen and (max-width: 1620px)">
.titem {
width: 244px;
}
</style>

<style type="text/css" media="only screen and (max-width: 1220px)">
.titem {
font-size: 12px;
width: 330px;
}
</style>




<script type="text/javascript">
<!--

$(function() {
	
	var $titSection = $('#title-section');
	$titSection.imagesLoaded(function() {
		$titSection.packery({
			itemSelector : '.titem',
			gutter : 5
		});
	});
	
	
	$('.story').each(function() {
		var $container = $(this);
		$container.imagesLoaded(function() {
			$container.packery({
				itemSelector : '.item',
				gutter : 5
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
	
	<div id="top-section">
		<div class="container-fluid bkg2">
			<div class="row-fluid">
				<div class="span2 section-title day-section">
					<h3>2013. 07. 13</h3>
				</div>
				
				<div class="span10 article-section">
					<div id="title-section">
						<div class="titem stock-section bkg4">
							<h5>코스피</h5>
							<h2>1,914.03</h2>
						</div>
						<div class="titem currency-section bkg4">
							<h5>코스닥</h5>
							<h2>554.31</h2>
						</div>
						<div class="titem stock-section bkg4">
							<h5>환율</h5>
							<h2>1115.50</h2>
						</div>
						<div class="titem stock-section bkg4">
							<h5>환율</h5>
							<h2>1115.50</h2>
						</div>
					</div>
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
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
				</div>

				<div class="span10 article-section">
					<div class="story">

					<c:forEach var="row" items="${entry.value}" varStatus="cnt">
						<div class="item">
							<div class="item_title bkg4"><a href="${row.link}" target="_blank">${row.title}</a></div>
							<div class="item_content">${row.contents}</div>
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
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
				</div>

				<div class="span10 article-section">
					<div class="story">

					<c:forEach var="row" items="${entry.value}" varStatus="cnt">
						<div class="item">
							<div class="item_title bkg4"><a href="${row.link}" target="_blank">${row.title}</a></div>
							<div class="item_content">${row.contents}</div>
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