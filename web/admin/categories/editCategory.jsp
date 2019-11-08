<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8"/>
		<title>Category Edition Page</title>
	</head>
	
	<body>
		<h1>Category Edition Page</h1>
		<form id="updateForm" action="<%=request.getContextPath() + "/fc/admin/categories/updateCategory"%>">
			<fieldset>
				<legend>Edit Category</legend>
				<p>
					<label for="category_name">Name:</label>
					<input type="text" name="category_name" value="${requestScope.categories[0].name}"/>
					<input type="hidden" name="category_id" value="${requestScope.categories[0].category_id}"/>
				</p>
				<p>
					<label for="parent_category_id">Nested under:</label>
					<select name="parent_category_id">
						<c:set var="descendants" value="${requestScope.categories[0].descendants}"/>
						<c:forEach var="category" items="${applicationScope.categoryServer.indentedCategories}">
							<c:set var="selector" value="0"/>
							<c:forEach var="descendant" items="${descendants}">
								<c:if test="${category.category_id == descendant.category_id}">
									<c:set var="selector" value="1"/>
								</c:if>
							</c:forEach>
							<c:if test="${category.category_id == requestScope.categories[0].parent.category_id}">
								<c:set var="selector" value="2"/>
							</c:if>
							<c:choose>
								<c:when test="${selector == 0}"><!-- normal -->
									<option value="${category.category_id}">${category.name}</option>
								</c:when>
								<c:when test="${selector == 1}"><!-- descendants are disabled (itself included) -->
									<option value="${category.category_id}" disabled>${category.name}</option>
								</c:when>
								<c:when test="${selector == 2}"><!-- parent is pre-selected -->
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
		
		
		<!--  
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
		
		
		<form id="deleteForm" action="<%=request.getContextPath() + "/fc/admin/categories/algo"%>">
				<fieldset>
					<legend>Delete Category</legend>
					<p>
						<input type="hidden" name="name" value=""/>
					</p>
					<p>
						<input type="submit" value="Delete"/>
					</p>
				</fieldset>
			</form>
		
		
		
		-->
		
		<p><a href="/firstmarket/fc/admin/categories/categoriesManager">go back</a></p>
	</body>
	
</html>