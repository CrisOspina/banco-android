<?php
	if (isset($_REQUEST['ident']) && isset($_REQUEST['nrocuenta'])) {
		$ident    = $_POST['ident'];
		$nrocuenta = $_POST['nrocuenta'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");

		$result = mysqli_query($cnx,"select nrocuenta from cuenta where nrocuenta = '$nrocuenta'");

		if (mysqli_num_rows($result) == 0) {
			mysqli_query($cnx,"INSERT INTO cuenta (ident, nrocuenta) VALUES ('$ident','$nrocuenta')");
		} else {
			echo "Cuenta existente ....";
		}
		mysqli_close($cnx);
	}
	else
	{
		echo "Debe especificar ident y nrocuenta, respectivamente...";
	}
?>
