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
width:100%;
font-size: 12px;
border-left: none;
border-right: none;
}

.item {
border-left: none;
border-right: none;
}

.item, .item_title, item_content {
width:100%;
}
</style>

<style type="text/css" media="only screen and (min-width : 721px) and (max-width: 1100px)">
.titem {
font-size: 12px;
width: 180px;
}

</style>

<style type="text/css" media="only screen and (min-width : 1101px) and (max-width: 1220px)">
.titem {
font-size: 12px;
width: 224px;
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
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<col-md- class="sr-only">Toggle navigation</col-md-> <col-md- class="icon-bar"></col-md-> <col-md- class="icon-bar"></col-md->
					<col-md- class="icon-bar"></col-md->
				</button>
				<a class="navbar-brand" href="/main" style="color: #FFF7BD">NewsYaa</a>
			</div>


			<div class="collapse navbar-collapse">
				<ul id="top-menu" class="nav navbar-nav">
					<li id="main-menu"><a href="/main">주요</a></li>
					<li id="politics-menu"><a href="/politics">정치</a></li>
					<li id="econ-menu"><a href="/econ">경제</a></li>
					<li id="society-menu"><a href="/society">사회</a></li>
					<li id="culture-menu"><a href="/culture">문화/생활</a></li>
					<li id="ent-menu"><a href="/ent">연예</a></li>
					<li id="sport-menu"><a href="/sport">스포츠</a></li>
					<li id="it-menu"><a href="/it">IT</a></li>
				</ul>

				<!-- 
				<ul class="nav pull-right">
				<li><a href="#" style="color:#FF823A">- 프리미엄 -</a></li>
				</ul>
				 -->
			</div>
			<!--/.nav-collapse -->
		</div>
		<div class="color-strip"></div>
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<!-- body -->
	
	<div id="top-section">
		<div class="bkg2" style="padding-top:25px;">
			<div class="row">
				<div class="col-md-2 day-section">
					<h3 style="line-height: 90px;"><fmt:formatDate pattern="yyyy.MM.dd" value="${currentDay}"/></h3>
				</div>
				
				<div class="col-md-10 article-section container">
					<div id="title-section" class="row">
						<div class="col-xs-2 titem stock-section bkg3">
							<h5>코스피 ${econ.kospiChange}</h5>
							<h2>${econ.kospi}</h2>
						</div>
						<div class="col-xs-2  titem currency-section bkg3">
							<h5>코스닥 ${econ.kosdaqChange}</h5>
							<h2>${econ.kosdaq}</h2>
						</div>
						<div class="col-xs-2  titem stock-section bkg3">
							<h5>미국 USD ${econ.usdChange}</h5>
							<h2>${econ.usd}원</h2>
						</div>
						<div class="col-xs-2  titem stock-section bkg3">
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

	<div class="row ${rowColor}">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div class="story row">

			<c:forEach var="row" items="${entry.value}" varStatus="cnt">
				<div class="item">
					<div class="item_title col-xs-12">
						<a href="${row.link}" target="_blank">${row.title}</a>
					</div>
					
					<c:if test="${not empty row.img}">
					<div> <img class="item_img" src="${row.img}"/></div>
					</c:if>
					
					<div class="item_content">${row.mainContents}</div>
				</div>
			</c:forEach>
				
			</div>
		</div>
		<!-- // article section -->
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

	<div class="row ${rowColor}">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div class="story">

			<c:forEach var="row" items="${entry.value}" varStatus="cnt">
				<div class="item">
					<div class="item_title col-xs-12">
						<a href="${row.link}" target="_blank">${row.title}</a>
					</div>
					
					<c:if test="${not empty row.img}">
					<div> <img class="item_img" src="${row.img}"/></div>
					</c:if>
					
					<div class="item_content">${row.mainContents}</div>
				</div>
			</c:forEach>
				
			</div>
		</div>
		<!-- // article section -->
	</div>
	
	</c:if>
    
</c:forEach>
	

</body>

<script type="text/javascript">

(function(yourcode) {
	yourcode(window.jQuery, window, document);
}(function($, window, document) {
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
		
		// top menu
		var urlList = document.URL.split('/');
		var menu = "#" + urlList[3] + "-menu";
		$(menu).addClass("active");
	});	
	// The rest of code goes here!

}));

</script>


</html>