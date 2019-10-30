<!DOCTYPE html>
<html>

	<head>
		<link rel="stylesheet" type="text/css" href="css/formato.css"/>
		<script type="text/javascript" src="js/validacion.js"></script>
		<meta charset="UTF-8"/>
		<title>Gestión de Libros</title>
	</head>
	
	<body>
		<form id="formulario" action="<%=request.getContextPath() + "/fc/insertBook"%>" method="post" onsubmit="validacionDOM()">
			<fieldset>
				<legend>Formulario alta libro</legend>
				<p>
					<label for="isbn">ISBN:</label>
					<input id="isbn" type="text" name="isbn"/>
				</p>
				<p>
					<label for="titulo">Título:</label>
					<input id="titulo" type="text" name="titulo"/>
				</p>
				<p>
					<label for="categoria">Categoría:</label>
					<input id="categoria" type="text" name="categoria"/>
				</p>
				<p>
					<input type="submit" value="Insertar"/>
				</p>
			</fieldset>
		</form>
	</body>
	
</html>