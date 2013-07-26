<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/css/bootstrap.css"/>'/>
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/css/bootstrap-responsive.css"/>'/>

<script type='text/javascript' src='<c:url value="/js/jquery/jquery-1.9.1.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/jquery-ui.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/imagesloaded.pkgd.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/jquery.masonry.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/packery.pkgd.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/bootstrap.min.js"/>'></script>

<style type="text/css">

body {
line-height: 25px;
}
.color-strip {
	background: #ccc url('img/color-strip.png') no-repeat 20% top;
	height: 5px;
}


.item {
	width: 270px;
	margin-bottom: 5px;
	text-align: justify;
	word-wrap: break-word;
	
	border-width: 1px;
	border-style: solid;
	border-color: #a0a0a0 #e0e4e6 #e0e4e6;
}

.one {
	margin: 0 auto;
	padding: 20px 0 20px 0;
}

.two {
	background: #555152;
	margin: 0 auto;
	padding: 20px 0 20px 0;
}

.item_title {
padding: 8px;
border-bottom: 1px solid #e1e1e1;
background-color: #f4f6f7;;
}

.item_content {
padding: 8px;
background-color: #FCFCEE;
}

.item_link {
padding: 8px;
border-bottom: 1px solid #e1e1e1;
background-color: #E6FCF4;;
}

.two .item {
border: none;
}

.two .section-title {
color:#fff;
}

.two .item_title {
background-color: #EFFFCD;
border-top: 1px solid #99173C;
}

.two .item_content {
background-color: #fff;
}

.two .item_link {
background-color: #E6FCF4;
}


#top-section {
	margin-top: 40px;
	margin-bottom: 0;
}

.article-section {
margin-left: 15px;
}

</style>