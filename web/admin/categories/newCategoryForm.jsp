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
					<label for="name">Name:</label>
					<input type="text" name="name" value=""/>
				</p>
				<p>
					<label for="parent">Nested under:</label>
					<select name="cars">
						<option value="volvo" selected>Volvo</option>
						<option value="saab">Saab</option>
						
						<option value="fiat">Fiat</option>
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