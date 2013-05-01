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
유저 목록

<table>
<tbody>
<c:set var="first" value="${page.size * page.number}"></c:set>
<c:forEach var="user" items="${page.content}" varStatus="status">
	<tr>
	    <td>${page.totalElements - (first + status.index)}</td>
		<td><a href="<spring:url value="/users/${user.id}"/>"><c:out value="${user.name}" /></a></td>
	</tr>
</c:forEach>
</tbody>
</table>

<spring:url value="/users" var="next">
   <spring:param name="page.page" value="${page.number + 2}" ></spring:param>
   <spring:param name="page.size" value="${page.size}" ></spring:param>
</spring:url>

<spring:url value="/users" var="prev">
   <spring:param name="page.page" value="${page.number}" ></spring:param>
   <spring:param name="page.size" value="${page.size}" ></spring:param>
</spring:url>

<c:if test="${!page.firstPage}">
<a href="${prev}">이전</a>
</c:if>
<c:if test="${!page.lastPage}">
<a href="${next}">다음</a>
</c:if>

<br/>
<a href="<spring:url value="/users/form"></spring:url>">유저등록</a>
</body>
</html>