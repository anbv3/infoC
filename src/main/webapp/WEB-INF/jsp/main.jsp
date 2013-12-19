<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!doctype html>
<html lang="ko">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi"/>

<title>NewsYaa</title>

<jsp:include page="./common/resources.jsp" />

<style type="text/css" media="only screen and (max-width : 720px)">
.titem {
font-size: 12px;
width: 100%;
border-left: none;
border-right: none;
}

.item {
width: 100%;
border-left: none;
border-right: none;
}

</style>

<style type="text/css" media="only screen and (min-width : 721px) and (max-width: 1220px)">
.titem,.item {
font-size: 12px;
width: 244px;
}
</style>

<style type="text/css" media="only screen and (min-width : 1221px) and (max-width: 1620px)">
.titem {
width: 244px;
}
</style>

</head>


<body>
	<!-- header -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="brand" href="index.html" style="color:#FFF7BD">NewsYaa</a>
				<div class="nav-collapse collapse">
					<ul id="top-menu" class="nav">
						<li class="active"><a href="/main">주요</a></li>
						<li><a href="/politics">정치</a></li>
						<li><a href="/econ">경제</a></li>
						<li><a href="/society">사회</a></li>
						<li><a href="/culture">문화/생활</a></li>
						<li><a href="/ent">연예</a></li>
						<li><a href="/sport">스포츠</a></li>
						<li><a href="/it">IT</a></li>
					</ul>
					<ul class="nav pull-right">
					<li><a href="#" style="color:#FF823A">- 프리미엄 -</a></li>
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
					<h3><fmt:formatDate pattern="yyyy.MM.dd" value="${currentDay}"/></h3>
					
				</div>
				
				<div class="span10 article-section">
					<div id="title-section">
						<div class="titem stock-section bkg4">
							<h5>코스피 ${econ.kospiChange}</h5>
							<h2>${econ.kospi}</h2>
						</div>
						<div class="titem currency-section bkg4">
							<h5>코스닥 ${econ.kosdaqChange}</h5>
							<h2>${econ.kosdaq}</h2>
						</div>
						<div class="titem stock-section bkg4">
							<h5>미국 USD ${econ.usdChange}</h5>
							<h2>${econ.usd}원</h2>
						</div>
						<div class="titem stock-section bkg4">
							<h5>중국 CNY ${econ.cnyChange}</h5>
							<h2>${econ.cny}원</h2>
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
							<div class="span12 item_title bkg4"><a href="${row.link}" target="_blank">${row.title}</a></div>
							<div class="item_content">${row.mainContents}</div>
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
							<div class="span12 item_title bkg4"><a href="${row.link}" target="_blank">${row.title}</a></div>
							<div class="item_content">${row.mainContents}</div>
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

<script type="text/javascript">
(function(yourcode) {
	yourcode(window.jQuery, window, document);
}(function($, window, document) {
	$(function() {
		// The DOM is ready!
		
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
	// The rest of code goes here!

}));

</script>


</html>