<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!doctype html>
<html lang="ko">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no"/>

<title>Skim paper</title>

<jsp:include page="./common/resources.jsp" />

<style type="text/css" media="only screen and (max-width : 721px)">
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

<style type="text/css" media="only screen and (min-width : 722px) and (max-width: 1100px)">
.titem {
font-size: 12px;
width: 180px;
}
.item {
width: 350px;
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

<script type="text/javascript">


	(function(yourcode) {
		yourcode(window.jQuery, window, document);
	}(function($, window, document) {
		$(function() {
			// side slider
			var sideslider = $('[data-toggle=collapse-side]');
			var sel = sideslider.attr('data-target');
			var sel2 = sideslider.attr('data-target-2');
			sideslider.click(function(event) {
				$(sel).toggleClass('in');
				$(sel2).toggleClass('out');
			});

			// active menu
			var menu = "#" + "${menu}" + "-menu";
			$(menu).addClass("active");
			
			$('.js-add-article').on('click', function(e) {
				e.preventDefault();
				
				alert("준비중");
				
			});
			/*
			
			var hour = 1;

			getArticlesByPage = function() {
				
				var reqURL = "<c:url value="/kr/"/>" + "/" + "${menu}";
				
				reqURL = reqURL.concat("/hour/").concat(hour);

				$.ajax({
					type : "GET",
					url : reqURL
				}).done(function(response) {
					
					if(response.trim() != "") {
						$('#pkg-list').children().last().after(response);
						pageNum++;
					} 
					
					$(window).data('ajaxready', true);
				}).error(function(response) {
					alert( "[ERROR] " + response.status + " : "+ response.statusText );
				});
			};
			
			
			// get more articles when scrolling down 
			$(window).data('ajaxready', true).scroll(function() {
				if ($(window).data('ajaxready') == false) {
					return;
				}
				if ($(window).scrollTop() == $(document).height() - $(window).height()) {
					$(window).data('ajaxready', false);
					
					//control.getPackageByOsTypeAndStageWithPage();
					
				}
			});
			*/
		});
		// The rest of code goes here!

	}));
</script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>


<body>
	<!-- header -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button data-toggle="collapse-side" data-target=".side-collapse" data-target-2=".side-collapse-container" type="button" class="navbar-toggle pull-right">
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/main" style="color:#fff;font-weight: bold;">SkimPaper</a>
			</div>

			<div class="navbar-inverse side-collapse in">
				<nav role="navigation" class="navbar-collapse">
					<ul id="top-menu" class="nav navbar-nav">
						<li id="main-menu"><a href="/main">주요</a></li>
						<li id="politics-menu"><a href="/politics">정치</a></li>
						<li id="econ-menu"><a href="/econ">경제</a></li>
						<li id="society-menu"><a href="/society">사회</a></li>
						<li id="culture-menu"><a href="/culture">문화/생활</a></li>
						<li id="ent-menu"><a href="/ent">연예</a></li>
						<li id="sport-menu"><a href="/sport">스포츠</a></li>
						<li id="it-menu"><a href="/it">IT</a></li>
						<li id="others-menu"><a href="/others">기타</a></li>
						<li id="us-menu"><a href="/us/main">미국</a></li>
						<li id="us-menu"><a style="color:#6db4d0" href="/login">로그인</a></li>
					</ul>
				</nav>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!-- <div class="color-strip"></div> -->
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<!-- body -->
	<div class="carousel-inner side-collapse-container">
	
	<div id="top-section">
		<div class="bkg2" style="padding-top:25px;">

			<c:if test="${menu == 'main'}">		
			<div class="row">
				<div class="col-md-2 day-section">
					<h3 style=""><fmt:formatDate pattern="yyyy.MM.dd" value="${currentDay}"/></h3>
				</div>
				
				<div class="col-md-10 article-section container">
					<div id="title-section" class="row">
						<div class="col-xs-2 titem econ-section bkg3">
							<h5>코스피 ${econ.kospiChange}</h5>
							<h2>${econ.kospi}</h2>
						</div>
						<div class="col-xs-2 titem econ-section bkg3">
							<h5>코스닥 ${econ.kosdaqChange}</h5>
							<h2>${econ.kosdaq}</h2>
						</div>
						<div class="col-xs-2 titem econ-section bkg3">
							<h5>미국 USD ${econ.usdChange}</h5>
							<h2>${econ.usd}원</h2>
						</div>
						<div class="col-xs-2 titem econ-section bkg3">
							<h5>중국 CNY ${econ.cnyChange}</h5>
							<h2>${econ.cny}원</h2>
						</div>
					</div>
					
					<script type="text/javascript">
				    $('#title-section').each(function() {
						var $container = $(this);
						$container.imagesLoaded(function() {
							$container.packery({
								itemSelector : '.titem',
								gutter : 5
							});
						});
					});
			    	</script>
    	
				</div>
			</div>
			</c:if>
			
		</div>
		
	</div>


<c:set var="rowColor" value="two"/>

<c:forEach var="entry" items="${articleMap}" varStatus="loop">

	<c:choose>
 		<c:when test="${rowColor == 'one'}"><c:set var="rowColor" value="two"/></c:when>
 		<c:otherwise><c:set var="rowColor" value="one"/></c:otherwise>
	</c:choose>
	
	<div class="row ${rowColor}">
		<div class="col-md-2 section-title text-left">
			<h3>${entry.key}:00 ~ ${entry.key+1}:00</h3>
		</div>

		<!-- article section -->
		<div class="col-md-10 article-section container">
			<div id="story-${entry.key}" class="story row">

			<c:forEach var="row" items="${entry.value}" varStatus="cnt">
				<div class="item">
					<div class="item_title col-xs-12">
						<a href="${row.link}" target="_blank">${row.title}</a>
					</div>
					
					<c:if test="${not empty row.img}">
					<div> <img class="item_img" src="${row.img}"/></div>
					</c:if>
					<c:if test="${empty row.img}">
					<hr class="item_title-separator">
					</c:if>
					
					<div class="item_content">${row.mainContents}</div>
					
					<div class="panel-group panel-group-sArt">
					  <div class="panel panel-default panel-sArt" >
					   
					    <div class="panel-heading panel-heading-sArt row">
					    
					      <h4 class="panel-title panel-${entry.key} col-xs-4">
					        <a data-toggle="collapse" data-parent="#accordion" href="#collapse-trans-${entry.key}-${cnt.index}">
					          <span class="glyphicon glyphicon-transfer"></span>
					        </a>
					      </h4>
					      
					      <h4 class="panel-title col-xs-4">
					      	<a class="js-add-article" href="#">
					          <span class="glyphicon glyphicon-plus <c:if test="${empty row.simularList}"> icon-disabled </c:if>"></span>
					        </a>
					      </h4>
					      
					      <h4 class="panel-title panel-${entry.key} col-xs-4">
					        <a data-toggle="collapse" data-parent="#accordion" href="#collapse-${entry.key}-${cnt.index}">
					         <span class="glyphicon glyphicon-list <c:if test="${empty row.simularList}"> icon-disabled </c:if>"></span>
					        </a>
					      </h4>
					      
					    </div>
					    
					   
					    <div id="collapse-${entry.key}-${cnt.index}" class="collapse-${entry.key} panel-collapse panel-collapse-sArt collapse">
					      <div class="panel-body panel-body-sArt">
					      	<c:forEach var="sArticle" items="${row.simularList}">
						      	<div class="panel-article-info col-xs-12">
									<a href="${sArticle.link}" target="_blank">${sArticle.title}</a>
								</div>
					      	</c:forEach>
					      </div>
					    </div>
					     
					    
					    <div id="collapse-trans-${entry.key}-${cnt.index}" class="collapse-${entry.key} panel-collapse panel-collapse-sArt collapse">
					      <div class="panel-body panel-body-sArt">
					     	 <div class="panel-article-info col-xs-12">
						      	${row.transedContents}
						     </div>
					      </div>
					    </div>
					    
					  </div>
  					</div>
					
				</div>
				
			</c:forEach>
				
			</div>
		</div>
		<!-- // article section -->
	</div>
	
    <script type="text/javascript">
	    $('#story-${entry.key}').each(function() {
			var $container = $(this);
			$container.imagesLoaded(function() {
				$container.packery({
					itemSelector : '.item',
					gutter : 5
				});
			});
		});
	    
	    $('.collapse-${entry.key}').on('shown.bs.collapse hidden.bs.collapse', function () {
	    	$('#story-${entry.key}').each(function() {
				var $container = $(this);
				$container.imagesLoaded(function() {
					$container.packery({
						itemSelector : '.item',
						gutter : 5
					});
				});
			});
	    });
	   
    </script>
</c:forEach>
	
	</div> <!-- carousel-inner -->

	<div id="footer" class="row">
      	<div class="container">
	      	<div class="col-md-6">
	      		<span>Copyright © 2013–2014, NewsYaa, Inc. All Rights Reserved.</span>
	      	</div>
	      	<div class="col-md-6">
	      		<span>Crafted with love <strong>Jinppang S.</strong>  &nbsp; | &nbsp; Contact: anbv333@gmail.com</span>
	      	</div>
        
        </div>
    </div>
</body>

</html>
