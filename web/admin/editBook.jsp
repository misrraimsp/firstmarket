<!DOCTYPE html>
<html>

	<head>
		<link rel="stylesheet" type="text/css" href="css/formato.css"/>
		<script type="text/javascript" src="js/validacion.js"></script>
		<meta charset="UTF-8"/>
		<title>Gesti�n de Libros</title>
	</head>
	
	<body>
		<form id="insertForm" action="<%=request.getContextPath() + "/fc/admin/updateBook"%>" method="post" onsubmit="validacionDOM()">
			<fieldset>
				<legend>Book Edition Form</legend>
				<p>
					<label for="isbn">ISBN:</label>
					<input id="isbn" type="text" name="isbn" value="${requestScope.books[0].isbn}"/>
				</p>
				<p>
					<label for="title">T�tulo:</label>
					<input id="title" type="text" name="title" value="${requestScope.books[0].title}"/>
				</p>
				<!--  
				<p>
					<label for="category">Categor�a:</label>
					<input id="category" type="text" name="category"/>
				</p>
				-->
				<p>
					<input type="submit" value="Update"/>
				</p>
			</fieldset>
		</form>
	</body>
	
</html>