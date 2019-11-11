<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>Books Manager</title>
	</head>

	<body>
		<h1>Books Manager</h1>
		<p><a href="/firstmarket/fc/admin">go back</a></p>
		<p><a href="/firstmarket/fc/admin/books/newBook">new book</a></p>
		<p>Please, to edit some book just click on the ISBN reference</p>
		<br/>
		<table border="1">
			<tr>
				<td><b>ISBN</b></td>
				<td><b>Title</b></td>
				<td><b>Category</b></td>
			</tr>
			
			<c:forEach var="book" items="${requestScope.books}">
			<tr>
				<td><a href="/firstmarket/fc/admin/books/editBook?isbn=${book.isbn}"><c:out value="${book.isbn}"/></a></td>
				<td><c:out value="${book.title}"/></td>
				<td><c:out value="${book.category.name}"/></td>
			</tr>
			</c:forEach>
			
		</table>
		
	</body>

</html>