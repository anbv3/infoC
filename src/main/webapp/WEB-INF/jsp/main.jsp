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
						<li id="us-menu"><a style="color:#6db4d0" href="/us/main">미국</a></li>
						<li id="us-menu"><a style="color:#ffde66" href="/login">로그인</a></li>
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
	
		<jsp:include page="./common/articles.jsp" />
	
	</div> <!-- carousel-inner -->


<%@ include file="./common/footer.jsp"%>

	
</body>

</html>
