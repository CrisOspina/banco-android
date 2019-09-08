<?php
	if (isset($_REQUEST['ident']) && isset($_REQUEST['email']) && isset($_REQUEST['nombres']) && isset($_REQUEST['clave'])) {
		$ident = $_POST['ident'];
		$email = $_POST['email'];
		$nombres = $_POST['nombres'];
		$clave = $_POST['clave'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");

		$result = mysqli_query($cnx,"select email from cliente where email = '$email'");

		if (mysqli_num_rows($result) == 0) {
			mysqli_query($cnx,"INSERT INTO cliente (ident,nombres,email,clave) VALUES ('$ident','$nombres','$email','$clave')");	
		} else {
			echo "Usuario existente ....";
		}
		mysqli_close($cnx);
	}
	else
	{
		echo "Debe especificar ident, clave, nombres y email, respectivamente...";
	}
?>
