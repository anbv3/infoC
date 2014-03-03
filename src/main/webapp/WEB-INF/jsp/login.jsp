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

<style type="text/css">
body {
  padding-top: 40px;
  padding-bottom: 40px;
  background-color: #eee;
}

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
  margin-top: 20px;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: normal;
}
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.signup fieldset,
.signin fieldset,
.register fieldset {
  border-top: 5px solid #f3f4f6;
  padding: 60px 0 0;
  margin: 0 0 60px;
}
.signup .control-group label,
.signin .control-group label,
.register .control-group label {
  float: left;
  font-size: 13px;
  font-weight: bold;
  color: #808998;
  margin-top: 5px;
}
.primary {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  padding-right: 30px;
}
.form-actions {
  background: none;
  padding-left: 0 !important;
}
.help-block {
  font-weight: bold;
  font-size: 14px;
  line-height: 1.1;
  color: #b8bdc6;
  margin-top: 10px;
}

</style>

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
				<a class="navbar-brand" href="/main" style="color:#fff;font-weight: bold;">Skim Paper!</a>
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
					</ul>
				</nav>
			</div>
			<!--/.nav-collapse -->
		</div>
		<!-- <div class="color-strip"></div> -->
	</div>
	<!-- header -->

	<!-- ************************************************************************************************************************************ -->

	<div class="container">

      <form class="form-signin" role="form">
        <h2 class="form-signin-heading">로그인하세요.</h2>
        <input type="email" class="form-control" placeholder="Email address" required="" autofocus="">
        
        <!--  <input type="password" class="form-control" placeholder="Password" required="">  -->
        <br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
        <br>
        <span>처음 입력한 아이디면 자동으로 회원가입이 됩니다.</span>
        <span>이후 같은 아이디로 로그인하세요.</span>
        
      </form>

    </div>


	<div id="footer" class="row navbar-fixed-bottom">
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
