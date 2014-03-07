<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="icon" type="image/x-icon" href="/img/favicon.ico" />
<link rel="shortcut icon" type="image/x-icon" href="/img/favicon.ico" />
<!-- <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />  -->

<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/css/common.css"/>'/>

<script type='text/javascript' src='<c:url value="/js/jquery/jquery-2.0.3.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/imagesloaded.pkgd.min.js"/>'></script>
<script type='text/javascript' src='<c:url value="/js/jquery/packery.pkgd.min.js"/>'></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

<script>
var nua = navigator.userAgent;
var isAndroid = (nua.indexOf('Mozilla/5.0') > -1 && nua.indexOf('Android ') > -1 && nua.indexOf('AppleWebKit') > -1 && nua.indexOf('Chrome') === -1);
if (isAndroid) {
  $('select.form-control').removeClass('form-control').css('width', '100%');
}
</script>