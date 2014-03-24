<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags"
 %><%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" 
 %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
 %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" 
 %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" 
 %><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
????????

<c:forEach var="user" items="${page}" varStatus="status">
	  ${user.title}<br>
</c:forEach>


</body>
</html>