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
						<c:forEach var="category" items="${applicationScope.categoryServer.indentedCategories}">
							<c:forEach var="descendant" items="${requestScope.categories[0].descendants}">
								<c:if test="${category.category_id == descendat.category_id}">
									<c:set var="descendant" value="true" />
								</c:if>
							</c:forEach>
							<!-- <c:if test="${category.category_id == requestScope.categories[0].category_id}" var="self"/> -->
							<c:if test="${category.category_id == requestScope.categories[0].parent.category_id}" var="pre_selected"/>
							<c:if test="${pre_selected}"><!-- parent is pre-selected -->
								<option value="${category.category_id}" selected>${category.name}</option>
							</c:if>
							<c:if test="${descendant or self}"><!-- descendants are disabled (itself included) -->
								<option value="${category.category_id}" disabled>${category.name}</option>
							</c:if>
							<c:if test="${not descendant and not pre_selected}"><!-- otherwise -->
								<option value="${category.category_id}">${category.name}</option>
							</c:if>
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