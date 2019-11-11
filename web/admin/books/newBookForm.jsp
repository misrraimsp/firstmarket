<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

	<head>
		<link rel="stylesheet" type="text/css" href="css/formato.css"/>
		<script type="text/javascript" src="js/validacion.js"></script>
		<meta charset="UTF-8"/>
		<title>Gestión de Libros</title>
	</head>
	
	<body>
		<form id="insertForm" action="<%=request.getContextPath() + "/fc/admin/books/insertBook"%>" method="post" onsubmit="validacionDOM()">
			<fieldset>
				<legend>New Book Form</legend>
				<p>
					<label for="isbn">ISBN:</label>
					<input id="isbn" type="text" name="isbn"/>
				</p>
				<p>
					<label for="title">Title:</label>
					<input id="title" type="text" name="title"/>
				</p>
				<p>
					<label for="category">Category:</label>
					<select name="category_id">
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
		<p><a href="/firstmarket/fc/admin/books/booksManager">go back</a></p>
	</body>
	
</html>