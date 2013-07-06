<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./common/includes.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>

<jsp:include page="./common/include-resources.jsp" />


<style type="text/css">
.item {
	width: 25%;
	margin-bottom: 5px;
	text-align: justify;
	word-wrap: break-word;
}

.item.w2 {
	width: 25%;
}

.color-strip {
	background: #ccc url('img/color-strip.png') no-repeat 20% top;
	height: 5px;
}

#top-section {
	margin-top: 40px;
	margin-bottom: 0;
}

.one {
	margin: 0 auto;
	padding: 20px 0 20px 0;
	color: #000;
}

.two {
	color: #fff;
	background: #333;
	margin: 0 auto;
	padding: 20px 0 20px 0;
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
						<li class="active"><a href="index.html">Home</a></li>
						<li class="dropdown"><a href="#" class="dropdown-toggle js-activated" data-toggle="dropdown">IT
								<b class="caret"></b>
						</a>
							<ul class="dropdown-menu">
								<li class="nav-header">General</li>
								<li><a href="about.html">About</a></li>
								<li><a href="services.html">Services</a></li>
								<li><a href="singleproject.html">Single Project</a></li>
								<li><a href="faq.html">FAQ</a></li>
								<li><a href="testimonials.html">Testimonials</a></li>
								<li class="divider"></li>
								<li class="nav-header">Portfolio Fixed</li>
								<li><a href="portfolio2.html">Two Columns</a></li>
								<li><a href="portfolio3.html">Three Columns</a></li>
								<li><a href="portfolio4.html">Four Columns</a></li>
							</ul></li>

						<li class="dropdown"><a href="#" class="dropdown-toggle js-activated" data-toggle="dropdown">Econ
								<b class="caret"></b>
						</a>
							<ul class="dropdown-menu">
								<li><a href="columns.html">Columns</a></li>
								<li><a href="team.html">Team Styles</a></li>
								<li><a href="elements.html">More Elements</a></li>
								<li><a href="icons.html">Icons</a></li>
							</ul></li>
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
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">

						<div class="item w2">
							<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg" />
						</div>

						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>

						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>

	<div class="two">
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div class="story">
						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도</div>
						<div class="item w2">
							<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg" />
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장
						</div>

						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도</div>
						
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>

						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…
							김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>


	<div class="one">
		<div class="container-fluid">
			<div class="row-fluid">

				<div class="span2 section-title text-left">
					<h3>13:00 ~ 14:00</h3>
				</div>

				<div class="span10">
					<div id="ccc" class="story">
						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item w2">
							<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg" />
						</div>
						<div class="item">뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item">
							뉴스타파 "김병진 전 대림산업 회장 등 4명 조세피난처 법인 설립"(종합) 김병진 전 회장 벤처 운영하면서 동시 설립…김재훈씨 `더 클래스 효성` 투자 특혜 의혹도
						</div>
						<div class="item w2">
							<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg" />
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>


</body>

</html>