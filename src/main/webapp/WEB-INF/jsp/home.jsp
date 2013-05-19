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
	width: 450px;
	padding: 10px 10px 10px 10px;
	margin:0;
	float: left;
	border: solid 1px #B4BBCD;
	min-height: 50px;
	text-align: justify;
	word-wrap: break-word;
}

.color-strip {
background: #ccc url('img/color-strip.png') no-repeat 20% top;
height: 5px;
}

#top-section {
margin-top: 40px;
margin-bottom:0;
}

.one {
margin: 0 auto;
padding: 20px 0 20px 0;
color:#000;
}


.two {
color:#fff;
background: #333;
margin: 0 auto;
padding: 20px 0 20px 0;
}



</style>

<script type="text/javascript">
<!--

	$(function() {

		$('.stroy').imagesLoaded(function() {
			$('.stroy').masonry({
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
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<a class="brand" href="index.html">InfoC</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href="index.html">Home</a></li>
						<li class="dropdown">
						<a href="#" class="dropdown-toggle js-activated" data-toggle="dropdown">IT 
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
							</ul>
						</li>
						
						<li class="dropdown">
						<a href="#" class="dropdown-toggle js-activated" data-toggle="dropdown">Econ 
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

    <div id="top-section" class="well well-large">
      <div class="container-fluid">
        <div class="row-fluid">
          <div class="span12 text-center">
            <h3><strong>LOREM IPSUM DOLOR</strong> - <span><em>Curabitur viverra nulla non tellus suscipit condimentum eget.</em></span></h3>
          </div>
        </div>
      </div>
    </div>

	<div class="one">
		<div class="container-fluid">
			<div class="story">
				<div class="row-fluid">

					<div class="span2 section-title text-left">
						<h3>13:00 ~ 14:00</h3>
					</div>

					<div class="span9">
					<div class="stroy">
						
						<div class="item">
						<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg"/>
						</div>
						
						<div class="item">2
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh erat, sagittis sit amet congue at,
								aliquam eu libero. Integer molestie, turpis vel ultrices facilisis, nisi mauris sollicitudin mauris, volutpat
								elementum enim urna eget odio. Donec egestas aliquet facilisis. Nunc eu nunc eget neque ornare fringilla. Nam
								vel sodales lectus. Nulla in pellentesque eros. Donec ultricies, enim vitae varius cursus, risus mauris iaculis
								neque, euismod sollicitudin metus erat vitae sapien. Sed pulvinar.</p>
						</div>
						<div class="item">3
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh erat, sagittis sit amet congue at,
								aliquam eu libero. Integer molestie, turpis vel ultrices facilisis, nisi mauris sollicitudin mauris, volutpat
								neque, euismod sollicitudin metus erat vitae sapien. Sed pulvinar.</p>
						</div>
						<div class="item">4</div>
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>
	
	<div class="two">
		<div class="container-fluid">
			<div class="story">
				<div class="row-fluid">

					<div class="span2 section-title text-left">
						<h3>13:00 ~ 14:00</h3>
					</div>

					<div class="span9">
					<div class="stroy">
						<div class="item">1</div>
						<div class="item">
						<img src="http://sccdn.chosun.com/news/html/2013/05/19/2013051901001461700122841.jpg"/>
						</div>
						<div class="item">3
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh erat, sagittis sit amet congue at,
								aliquam eu libero. Integer molestie, turpis vel ultrices facilisis, nisi mauris sollicitudin mauris, volutpat
								elementum enim urna eget odio. Donec egestas aliquet facilisis. Nunc eu nunc eget neque ornare fringilla. Nam
								vel sodales lectus. Nulla in pellentesque eros. Donec ultricies, enim vitae varius cursus, risus mauris iaculis
								neque, euismod sollicitudin metus erat vitae sapien. Sed pulvinar.</p>
						</div>
						
						<div class="item">4</div>
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>


<div class="one">
		<div class="container-fluid">
			<div class="story">
				<div class="row-fluid">

					<div class="span2 section-title text-left">
						<h3>13:00 ~ 14:00</h3>
					</div>

					<div class="span9">
						<div class="stroy">
							<div class="item">1</div>
							<div class="item">2
								<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh erat, sagittis sit amet congue at,
									aliquam eu libero. Integer molestie, turpis vel ultrices facilisis, nisi mauris sollicitudin mauris, volutpat
									elementum enim urna eget odio. Donec egestas aliquet facilisis. Nunc eu nunc eget neque ornare fringilla. Nam
									vel sodales lectus. Nulla in pellentesque eros. Donec ultricies, enim vitae varius cursus, risus mauris iaculis
									neque, euismod sollicitudin metus erat vitae sapien. Sed pulvinar.</p>
							</div>
							<div class="item">3
								<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean nibh erat, sagittis sit amet congue at,
									aliquam eu libero. Integer molestie, turpis vel ultrices facilisis, nisi mauris sollicitudin mauris, volutpat
									neque, euismod sollicitudin metus erat vitae sapien. Sed pulvinar.</p>
							</div>
							<div class="item">1</div>
						</div>
					</div>
				</div>
			</div>
			<!--.story-->
		</div>
	</div>
	
	
</body>

</html>