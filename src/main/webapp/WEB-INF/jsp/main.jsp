<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!doctype html>
<html lang="ko">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

<title>Skim paper</title>

<jsp:include page="./common/resources.jsp" />


<script type="text/javascript">
	var today = true;
	var autoLoad = false;
	var date = new Date('${initDay}');
	var page = 1; // 처음 로드할때 page 0은 가져오므로 1부터 시작
	var search = "${query}";
	var requestSent = false;
	
	var control = {
		
		createDateSection : function(oldDate) {
			var tmpDay =  new Date(oldDate);
			var OldDay = tmpDay.getDate();
			var OldMonth = tmpDay.getMonth() + 1;
			var OldYear = tmpDay.getFullYear();
			
			if (OldMonth.toString().length < 2) {
				OldMonth = '0' + OldMonth;
			}
			
			var dateSection = '<div class="bkg2"> <div class="row"> <div class="col-md-12 day-section"> <h3>' 
			+ OldYear + '.' + OldMonth + '.' + OldDay + '</h3></div></div></div>';  
		
			return dateSection;
		},
		
		getArticlesByDateAndPage : function() {
			if(!requestSent) {
				requestSent = true;
			
				$('#ajaxloader').removeClass('hide');
				$('#btn-get-more').addClass('hide');

				var reqURL = "<c:url value="/kr"/>" + "/" + "${menu}" + "/date/" + date.getTime() + "/page/" + page;
				if (search) {
					reqURL += "?q=";
					reqURL += encodeURI(encodeURIComponent(search));
				}
				
				$.ajax({
					type : "GET",
					url : reqURL
				}).done(function(response) {
					if (response.trim() == "end") {
						$('#ajaxloader').remove();
                        $('#btn-get-more').remove();
						return;
					} else if (response.trim() != "") {
						if (page == 0) {
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
                            }
						}

						$('#article-list-section').children().last().after(response);
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
                    $('#btn-get-more').removeClass('hide');
					$(window).data('ajaxready', true);
					requestSent = false;
				});
			}
		},
		
		getArticlesBySearch : function() {
			if(!requestSent) {
				requestSent = true;
				
				$('#ajaxloader').removeClass('hide');
                $('#btn-get-more').addClass('hide');

				var reqURL = "<c:url value="/kr/search"/>" + "/" + "${menu}";
				if (search) {
					reqURL += "?q=";
					reqURL += encodeURI(encodeURIComponent(search));

					var p = page + 1;
					reqURL += "&page.page=" + p;
					reqURL += "&page.size=" + 6; // Constant.PAGE_SIZE
					
				}

				$.ajax({
					type : "GET",
					url : reqURL
				}).done(function(response) {
					if (response.trim() == "end") {
                        $('#ajaxloader').remove();
                        $('#btn-get-more').remove();
						return;
					} else if (response.trim() != "") {
						if (page == 0) {
							$('#top-section').html(control.createDateSection(date));
							$('#article-list-section').html(response);	
						} else {
							$('#article-list-section').children().last().after(response);
						}
						
						page++;
					} 
				}).error(function(response) {
					alert("[ERROR] " + response.status + " : " + response.statusText);
				}).always(function() {
					$('#ajaxloader').addClass('hide');
                    $('#btn-get-more').removeClass('hide');
					$(window).data('ajaxready', true);
					requestSent = false;
				});
			}
		},

        getSomeMore : function() {
            if (search) {
                control.getArticlesBySearch();
            } else {
                control.getArticlesByDateAndPage();
            }
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
			if ( !search && $('#article-list-section').children().length < 4 ) {
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
                    $('#top-section').remove();
					$('#article-list-section').empty();
					
					search = $('#search-input').val();
					page = 0;					
					date =  new Date('${initDay}');
					today = true;
					
					control.getArticlesBySearch();
				}

                $(".collapse").collapse('hide');
			});
			
			// get more articles when scrolling down 
			$(window).data('ajaxready', true).scroll(function() {
				if ($(window).data('ajaxready') == false) {
					return;
				}
				
				if ($(window).scrollTop() == ($(document).height() - $(window).height())) {
					$(window).data('ajaxready', false);
					
					if (search) {
						control.getArticlesBySearch();
					} else {
						control.getArticlesByDateAndPage();	
					}
				}
			});

            ///////////////////////////////////////////////////////

            /* affix the navbar after scroll below header */
            $('#nav').affix({
                offset: {
                    top: $('header').height()-$('#nav').height()
                }
            });

            /* highlight the top nav as scrolling occurs */
            $('body').scrollspy({ target: '#nav' })

		});

	}));
	
	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	
	ga('create', 'UA-50349754-1', 'skimpaper.com');
	ga('require', 'displayfeatures');
	ga('send', 'pageview');

</script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
  <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
</head>


<body>

    <div id="wrap">
        <header class="hidden-xs">
            <h2 id="top-title">Skim Paper</h2>
            <h3 id="top-desc">국내외 뉴스를 모아서 모아서 정리 및 요약해 드립니다!</h3>
        </header>

	<!-- header -->
	<div class="navbar navbar-custom navbar-inverse navbar-static-top" role="navigation" id="nav" style="margin-bottom:0">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span style="color:#fff">Menu</span>
				</button>
			</div>
			
			<div class="collapse navbar-collapse mobile-nav-list">
				<ul id="top-menu" class="nav navbar-nav navbar-left">
					<li id="main-menu"><a href="/kr/main">주요</a></li>
					<li id="politics-menu"><a href="/kr/politics">정치</a></li>
					<li id="econ-menu"><a href="/kr/econ">경제</a></li>
					<li id="society-menu"><a href="/kr/society">사회</a></li>
					<li id="culture-menu"><a href="/kr/culture">문화</a></li>
					<li id="ent-menu"><a href="/kr/ent">연예</a></li>
					<li id="sport-menu"><a href="/kr/sport">스포츠</a></li>
					<li id="it-menu"><a href="/kr/it">IT</a></li>
					<li id="others-menu"><a href="/kr/others">기타</a></li>
					<li id="us-menu"><a style="color: #83E01F" href="/us/main">USA</a></li>
					<!-- <li id="us-menu"><a style="color:#ffde66" href="/login">로그인</a></li>  -->
				</ul>

				<div class="col-md-3 navbar-right" >
					<form class="navbar-form form-inline" onsubmit="return false">
						<div class="input-group">
							<input id="search-input" type="text" class="form-control"> 
							<span class="input-group-btn">
								<button id="search-btn" class="btn btn-primary" type="button"><i class="glyphicon glyphicon-search"></i></button>
							</span>
						</div>
						<!-- /input-group -->
					</form>
				</div>
				<!-- /.col-lg-6 -->

			</div>
			<!--/.nav-collapse -->
		</div>
		<!-- container -->
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<!-- body -->
	<div class="carousel-inner side-collapse-container">

		<div id="top-section">
		<c:if test="${empty query}">
			<div class="bkg2">
				<div class="row">
					<div class="col-md-12 day-section">
						<h3>
							${currentDay}
						</h3>
					</div>
				</div>
			</div>
		</c:if>
		</div>

		<div id="article-list-section">
			<c:if test="${empty query}">
				<jsp:include page="./common/articles.jsp" />
			</c:if>	
			<c:if test="${not empty query}">
				<jsp:include page="./common/searched-articles.jsp" />
			</c:if>	
		</div>

        <div id="btn-get-more" class="row">
            <div class="col-sm-12 text-center">
                <button class="btn btn-primary btn-lg btn-block" onclick="control.getSomeMore();" style="padding:10px 30px;">
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </button>
            </div>
        </div>

		<div id="ajaxloader" class="row loading-bar bkg1 hide">
			<div class="bar">
			    <i class="sphere"></i>
			</div>
		</div>
		
	</div>
	<!-- carousel-inner -->
</div>

	<%@ include file="./common/footer.jsp"%>

</body>

</html>
