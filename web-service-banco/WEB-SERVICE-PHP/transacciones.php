<?php
  if (isset($_REQUEST['nrocuentaorigen']) && 
    isset($_REQUEST['nrocuentadestino']) && isset($_REQUEST['valor'])) {
		$nrocuentaorigen = $_POST['nrocuentaorigen'];
		$nrocuentadestino = $_POST['nrocuentadestino'];
		$valor = $_POST['valor'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");
    
    // validar si la cuenta tiene saldo y la cuenta destino coincida con cuenta existente
    $saldoCuenta = mysqli_query($cnx, "select saldo, nrocuenta from cuenta where saldo <= '$valor' and nrocuenta = '$nrocuentadestino'");

		if (mysqli_num_rows($saldoCuenta) > 0 ) {
			mysqli_query($cnx,"INSERT INTO transaccion (nrocuentaorigen,nrocuentadestino,valor) VALUES ('$nrocuentaorigen','$nrocuentadestino','$valor')");	
			mysqli_query($cnx, "DELETE FROM cuenta WHERE saldo = '$valor'");
		} else {
			echo "Usuario existente ...";
		}
		mysqli_close($cnx);
	}
	else
	{
		echo "Debe especificar nrocuentaorigen, nrocuentadestino y valor...";
	}
?>