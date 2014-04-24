<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!doctype html>
<html lang="ko">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no"/>

<title>Skim Paper!</title>

<jsp:include page="./common/resources.jsp" />


<style type="text/css" >

.item_title {
padding: 8px 28px;
background-color: #fff;
color: #000;
line-height: 24px;
font-weight: bold;
letter-spacing: normal;
font-size: 20px;
font-family: 'Miller Headline Bold', 'Times New Roman', Times, Georgia, serif;
}
.item_title a {
color: #444;
}

</style>




<style type="text/css" media="only screen and (max-width : 768px)">
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
	var today = true;
	var autoLoad = false;
	var date =  new Date('${initDay}');
	var page = 1; // 처음 로드할때 page 0은 가져오므로 1부터 시작
	var search;
	
	var control = {
		
		createDateSection : function(oldDate) {
			var tmpDay =  new Date(oldDate);
			var OldDay = tmpDay.getDate();
			var OldMonth = tmpDay.getMonth() + 1;
			var OldYear = tmpDay.getFullYear();
			
			if (OldMonth.length == 1) {
				OldMonth = '0' + OldMonth;
			}
			
			var dateSection = '<div class="bkg2"> <div class="row"> <div class="col-md-12 day-section"> <h3>' 
			+ OldYear + '.' + OldMonth + '.' + OldDay + '</h3></div></div></div>';  
		
			return dateSection;
		},
		
		getArticlesByDateAndPage : function() {
			$('#ajaxloader').removeClass('hide');
			
			var reqURL = "<c:url value="/us"/>" + "/" + "${menu}" + "/date/" + date.getTime() + "/page/" + page;
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
						// 이전날 기사를 처음으로 가져온 경우 => 날자만 추가한다.
						// 안그럼 로딩 요청이 중복으로 들어가 결과도 중복으로 출력된다.
						
						if (search) {
							// 검색해서 가져온 경우
							if (today == true) {
								$('#top-section').html(control.createDateSection(date));
								$('#article-list-section').html(response);	
							} else {
								$('#article-list-section').children().last().after(control.createDateSection(date));
							}
						} else {
							if ($('#article-list-section').children().length < 1) {
								$('#article-list-section').html(control.createDateSection(date));
							} else {
								// today == false
								$('#article-list-section').children().last().after(control.createDateSection(date));
							}
							
							// 기사가 적어 자동 로딩할때만 기사 추가
							if (autoLoad == true) {
								$('#article-list-section').children().last().after(response);
								autoLoad = false;
							}
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

			// 내용이 적으면 전날거 다시 가져오기
			if ( $('#article-list-section').children().length < 4 ) {
				page = 0;
				autoLoad = true;
				var dayOfMonth = date.getDate();
				date = new Date(date.setDate(dayOfMonth - 1));
				today = false;
				
				control.getArticlesByDateAndPage();	
			}
			
			// 검색해서 가져오기
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
					date =  new Date('${initDay}');
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
			
			<div class="collapse navbar-collapse mobile-nav-list">
				<ul id="top-menu" class="nav navbar-nav">
					<li id="main-menu"><a href="/us/main">TOP</a></li>
					<li id="politics-menu"><a href="/us/politics">POLITICS</a></li>
					<li id="econ-menu"><a href="/us/econ">BUSINESS</a></li>
					<li id="culture-menu"><a href="/us/culture">LIFE</a></li>
					<li id="ent-menu"><a href="/us/ent">ENT</a></li>
					<li id="sport-menu"><a href="/us/sport">SPORTS</a></li>
					<li id="it-menu"><a href="/us/it">TECH</a></li>
					<li id="us-menu"><a style="color:#83E01F" href="/kr/main">Korea</a></li>
					<!-- <li id="us-menu"><a style="color:#ffde66" href="/login">Log In</a></li>  -->
				</ul>
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
			<div class="bkg2">
				<div class="row">
					<div class="col-md-12 day-section">
						<h3>
							${currentDay}
						</h3>
					</div>
				</div>
			</div>
		</div>

		<c:set var="rowColor" value="two"/>
		
		<div id="article-list-section">
			<jsp:include page="./common/articles.jsp" />
		</div>
		
		<div id="ajaxloader" class="row loading-bar bkg1">
			<div class="bar">
			    <i class="sphere"></i>
			</div>
		</div>
	
	</div> <!-- carousel-inner -->


	<%@ include file="./common/footer.jsp"%>
	
	
</body>

</html>
