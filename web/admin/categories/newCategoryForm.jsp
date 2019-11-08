<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8"/>
		<title>New Category Form</title>
	</head>
	<body>
		<form id="insertForm" action="<%=request.getContextPath() + "/fc/admin/categories/insertCategory"%>" method="post">
			<fieldset>
				<legend>New Category</legend>
				<p>
					<label for="category_name">Name:</label>
					<input type="text" name="category_name"/>
				</p>
				<p>
					<label for="parent_category_id">Nested under:</label>
					<select name="parent_category_id">
						<c:forEach var="category" items="${applicationScope.categoryServer.indentedCategories}">
							<option value="${category.category_id}">${category.name}</option>
						</c:forEach>
					</select>
				</p>
				<p>
					<input type="submit" value="Insert"/>
				</p>
			</fieldset>
		</form>
		<p><a href="/firstmarket/fc/admin/categories/categoriesManager">go back</a></p>
	</body>
</html>