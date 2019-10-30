function validacion() {
	if (document.forms[0].isbn.value == "") alert("datos no validos");
	else document.forms[0].submit();
	}

function validacionDOM() {
	var isbn = document.getElementById("isbn");
	var formulario = document.getElementById("formulario");
	if (isbn.value == "") {
		alert("error: datos no validos (DOM)");
		return false;
		}
	else formulario.submit();
	}