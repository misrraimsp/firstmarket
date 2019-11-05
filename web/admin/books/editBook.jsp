<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8"/>
		<title>Book Edition Page</title>
	</head>
	
	<body>
		<h1>Book Edition Page</h1>
		<form id="updateForm" action="<%=request.getContextPath() + "/fc/admin/books/updateBook"%>">
			<fieldset>
				<legend>Edit Book</legend>
				<p>
					<label for="isbn">ISBN:</label>
					<input type="text" name="isbn" value="${requestScope.books[0].isbn}"/>
				</p>
				<p>
					<label for="title">Title:</label>
					<input type="text" name="title" value="${requestScope.books[0].title}"/>
				</p>
				<p>
					<input type="submit" value="Update"/>
				</p>
			</fieldset>
		</form>
		<form id="deleteForm" action="<%=request.getContextPath() + "/fc/admin/books/deleteBook"%>">
			<fieldset>
				<legend>Delete Book</legend>
				<p>
					<input type="hidden" name="isbn" value="${requestScope.books[0].isbn}"/>
				</p>
				<p>
					<input type="submit" value="Delete"/>
				</p>
			</fieldset>
		</form>
	</body>
	
</html>