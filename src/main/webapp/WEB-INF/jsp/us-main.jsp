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

var page = 1;

var control = {
	getArticlesByPage : function() {
		
		var reqURL = "<c:url value="/us/"/>" + "/" + "${menu}" + "/" + page;

		$.ajax({
			type : "GET",
			url : reqURL
		}).done(function(response) {

			if (response.trim() != "") {
				$('#article-section').children().last().after(response);
				page++;
			}

			$(window).data('ajaxready', true);
		}).error(function(response) {
			alert("[ERROR] " + response.status + " : "	+ response.statusText);
		});
	}
};


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
				alert("not ready yet!");
			});

			// get more articles when scrolling down 
			$(window).data('ajaxready', true).scroll(function() {
				if ($(window).data('ajaxready') == false) {
					return;
				}
				
				if ($(window).scrollTop() > ($(document).height() - $(window).height())* 0.8
						&& $(window).scrollTop() < ($(document).height() - $(window).height())* 0.9) {
					
					$(window).data('ajaxready', false);
					control.getArticlesByPage();
				}
			});

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
				<a class="navbar-brand" href="/us/main" style="color: #fff; font-weight: bold;">Skim Paper!</a>
			</div>

			<div class="navbar-inverse side-collapse in">
				<nav role="navigation" class="navbar-collapse">
					<ul id="top-menu" class="nav navbar-nav">
						<li id="main-menu"><a href="/us/main">TOP</a></li>
						<li id="politics-menu"><a href="/us/politics">POLITICS</a></li>
						<li id="econ-menu"><a href="/us/econ">BUSINESS</a></li>
						<li id="culture-menu"><a href="/us/culture">LIFE</a></li>
						<li id="ent-menu"><a href="/us/ent">ENT</a></li>
						<li id="sport-menu"><a href="/us/sport">SPORTS</a></li>
						<li id="it-menu"><a href="/us/it">TECH</a></li>
						<li id="us-menu"><a style="color:#6db4d0" href="/kr/main">Korea</a></li>
						<!-- <li id="us-menu"><a style="color:#ffde66" href="/login">Log In</a></li>  -->
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
			</div>
		</div>

		<c:set var="rowColor" value="two"/>
		<jsp:include page="./common/articles.jsp" />
	
	</div> <!-- carousel-inner -->


	<%@ include file="./common/footer.jsp"%>
	
	
</body>

</html>
