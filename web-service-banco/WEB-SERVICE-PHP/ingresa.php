<?php 
	if(isset($_REQUEST['ident']) && isset($_REQUEST['clave']) && isset($_REQUEST['nombres']) &&
	isset($_REQUEST['email'])) {
		$ident=$_REQUEST['ident'];
		$clave=$_REQUEST['clave'];
		$nombres=$_REQUEST['nombres'];
		$email=$_REQUEST['email'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");

		$result = mysqli_query($cnx,"select ident from cliente where ident = '$ident'");

		if (mysqli_num_rows($result) == 0) {
			mysqli_query($cnx,"INSERT INTO cliente (ident,nombres,email,clave) VALUES ('$ident','$nombres','$email','$clave')");	
		}
		else {
			echo "Usuario ya existe....";
		}
		mysqli_close($cnx);
	} else {
		echo "Ingrese todos los datos....";
	}
?>
