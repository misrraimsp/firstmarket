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
					<input type="text" name="isbn_view" value="${requestScope.books[0].isbn}" disabled/>
					<input type="hidden" name="isbn" value="${requestScope.books[0].isbn}"/>
				</p>
				<p>
					<label for="title">Title:</label>
					<input type="text" name="title" value="${requestScope.books[0].title}"/>
				</p>
				<p>
					<label for="category">Category:</label>
					<select name="category_id">
						<c:forEach var="category" items="${applicationScope.categoryServer.indentedCategories}">
							<c:set var="selector" value="0"/>
							<c:if test="${requestScope.books[0].category.category_id == category.category_id}">
								<c:set var="selector" value="1"/>
							</c:if>
							<c:choose>
								<c:when test="${selector == 0}"><!-- normal -->
									<option value="${category.category_id}">${category.name}</option>
								</c:when>
								<c:when test="${selector == 1}"><!-- current category is pre-selected -->
									<option value="${category.category_id}" selected>${category.name}</option>
								</c:when>
								<c:otherwise>
									<option value="error" selected>error</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
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
		<p><a href="/firstmarket/fc/admin/books/booksManager">go back</a></p>
	</body>
	
</html>