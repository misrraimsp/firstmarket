<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>Show Books Page</title>
	</head>

	<body>
		<table border="1">
			<tr>
				<td><b>ISBN</b></td>
				<td><b>Title</b></td>
			</tr>
			
			<c:forEach var="book" items="${requestScope.books}">
			<tr>
				<td><c:out value="${book.isbn}"/></td>
				<td><c:out value="${book.title}"/></td>
			</tr>
			</c:forEach>
			
		</table>
		<br/>
		<a href="/firstmarket/fc/admin">go back</a>
	</body>

</html>