<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!doctype html>
<html lang="ko">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0" />

<title>Skim paper</title>

<jsp:include page="./common/resources.jsp" />

<style type="text/css" media="only screen and (max-width : 768px)">
.titem {
	width: 100%;
	font-size: 12px;
	border-left: none;
	border-right: none;
}

.item {
	border-left: none;
	border-right: none;
}

.item,.item_title,item_content {
	width: 100%;
}
</style>

<style type="text/css" media="only screen and (min-width : 769px) and (max-width: 1100px)">
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
	var autoLoad = false;
	var date =  new Date('<fmt:formatDate pattern="MM/dd/yyyy hh:mm:ss" value="${currentDay}"/>');
	var page = 1; // 처음 로드할때 page 0은 가져오므로 1부터 시작
	var search;
	
	var control = {
		
		createDateSection : function(oldDate) {
			var tmpDay =  new Date(oldDate);
			var OldDay = tmpDay.getDate();
			var OldMonth = tmpDay.getMonth() + 1;
			var OldYear = tmpDay.getFullYear();
			
			var dateSection = '<div class="bkg2"> <div class="row"> <div class="col-md-12 day-section"> <h3>' 
			+ OldYear + '.' + OldMonth + '.' + OldDay + '</h3></div></div></div>';  
		
			return dateSection;
		},
		
		getArticlesByDateAndPage : function() {
			$('#ajaxloader').removeClass('hide');
			
			var reqURL = "<c:url value="/kr"/>" + "/" + "${menu}" + "/date/" + date.getTime() + "/page/" + page;
			if (search) {
				reqURL += "?search=";
				reqURL += encodeURI(encodeURIComponent(search));
			}
			
			$.ajax({
				type : "GET",
				url : reqURL,
			}).done(function(response) {
				if (response.trim() == "end") {
					$('#ajaxloader').remove();
					return;
				} else if (response.trim() != "") {
					
					if (page == 0) {
						$('#article-list-section').children().last().after(control.createDateSection(date));
						if (search && today == true) {
							$('#article-list-section').html(response);	
						}
						
						if (autoLoad == true) {
							$('#top-section').html(control.createDateSection(date));	
							$('#article-list-section').html(response);	
							autoLoad = false;
						}						
					} else {
						$('#article-list-section').children().last().after(response);
					}
					
					page++;
				} else {
					var dayOfMonth = date.getDate();
					date = new Date(date.setDate(dayOfMonth - 1));
					page = 0;
					today = false;
					
					control.getArticlesByDateAndPage();
				}
				
			}).error(function(response) {
				alert("[ERROR] " + response.status + " : " + response.statusText);
			}).always(function() {
				$('#ajaxloader').addClass('hide');
				$(window).data('ajaxready', true);
			});
		}
	};

	(function(yourcode) {
		yourcode(window.jQuery, window, document);
	}(function($, window, document) {
		$(function() {
			// active menu
			var menu = "#" + "${menu}" + "-menu";
			$(menu).addClass("active");

			if ( $('#article-list-section').children().length <= 0 ) {
				page = 0;
				autoLoad = true;
				var dayOfMonth = date.getDate();
				date = new Date(date.setDate(dayOfMonth - 1));
				
				control.getArticlesByDateAndPage();	
			}
			
			$('#search-input').on('keyup', function(e) {
				var kcode = (window.event) ? event.keyCode : event.which;
	            if (kcode == 13) {
	            	$( "#search-btn" ).trigger( "click" );
	            }
			});
			
			$('#search-btn').on('click', function() {
				if ($('#search-input').val()) {
					$('#article-list-section').empty();
					
					search = $('#search-input').val();
					page = 0;					
					date =  new Date('<fmt:formatDate pattern="MM/dd/yyyy hh:mm:ss" value="${currentDay}"/>');
					today = true;
					control.getArticlesByDateAndPage();	
				}
			});
			
			// get more articles when scrolling down 
			$(window).data('ajaxready', true).scroll(function() {
				if ($(window).data('ajaxready') == false) {
					return;
				}
				
				if ($(window).scrollTop() == ($(document).height() - $(window).height())) {
					$(window).data('ajaxready', false);
					control.getArticlesByDateAndPage();
				}
			});

		});

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
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/main" style="color: #fff; font-weight: bold;">Skim Paper!</a>
			</div>
			
			<div class="collapse navbar-collapse">
				<ul id="top-menu" class="nav navbar-nav">
					<li id="main-menu"><a href="/main">주요</a></li>
					<li id="politics-menu"><a href="/politics">정치</a></li>
					<li id="econ-menu"><a href="/econ">경제</a></li>
					<li id="society-menu"><a href="/society">사회</a></li>
					<li id="culture-menu"><a href="/culture">문화</a></li>
					<li id="ent-menu"><a href="/ent">연예</a></li>
					<li id="sport-menu"><a href="/sport">스포츠</a></li>
					<li id="it-menu"><a href="/it">IT</a></li>
					<li id="others-menu"><a href="/others">기타</a></li>
					<li id="us-menu"><a style="color: #83E01F" href="/us/main">USA</a></li>
					<!-- <li id="us-menu"><a style="color:#ffde66" href="/login">로그인</a></li>  -->
				</ul>

				<div class="col-lg-3 pull-right">
					<form class="navbar-form form-inline" onsubmit="return false">
						<div class="input-group">
							<input id="search-input" type="text" class="form-control"> 
							<span class="input-group-btn">
								<button id="search-btn" class="btn btn-primary" type="button">검색</button>
							</span>
						</div>
						<!-- /input-group -->
					</form>
				</div>
				<!-- /.col-lg-6 -->


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
			<c:if test="${menu != 'main'}">
				<div class="bkg2">
					<div class="row">
						<div class="col-md-12 day-section">
							<h3>
								<fmt:formatDate pattern="yyyy.MM.dd" value="${currentDay}" />
							</h3>
						</div>
					</div>
				</div>
			</c:if>

			<c:if test="${menu == 'main'}">
				<div class="bkg2" style="padding-top: 25px;">
					<div class="row">
						<div class="col-md-2 day-section">
							<h3>
								<fmt:formatDate pattern="yyyy.MM.dd" value="${currentDay}" />
							</h3>
						</div>

						<div class="col-md-10 container">
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
				</div>
			</c:if>

		</div>


		<c:set var="rowColor" value="two" />

		<div id="article-list-section">
			<jsp:include page="./common/articles.jsp" />
		</div>

		<div id="ajaxloader" class="row loading-bar bkg1 hide">
			<div class="bar">
			    <i class="sphere"></i>
			</div>
		</div>
		
	</div>
	<!-- carousel-inner -->


	<%@ include file="./common/footer.jsp"%>


</body>

</html>
