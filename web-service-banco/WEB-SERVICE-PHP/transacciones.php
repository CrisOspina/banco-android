<?php
  if (isset($_REQUEST['nrocuentaorigen']) && 
    isset($_REQUEST['nrocuentadestino']) && isset($_REQUEST['valor'])) {
		$nrocuentaorigen = $_POST['nrocuentaorigen'];
		$nrocuentadestino = $_POST['nrocuentadestino'];
		$valor = $_POST['valor'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");

		//$saldoCuenta = mysqli_query($cnx, "select saldo, nrocuenta from cuenta where saldo <= '$valor' and nrocuenta = '$nrocuentadestino'");


    
    // validar si la cuenta tiene saldo y la cuenta destino coincida con cuenta existente
		$saldoOrigen = mysqli_query($cnx, "select saldo from cuenta where saldo <= '$valor' AND nrocuenta = '$nrocuentaorigen'");

		$saldoDestino = mysqli_query($cnx, "select saldo from cuenta where nrocuenta = '$nrocuentadestino'");

		$totalCuentaOrigen = $saldoDestino - $saldoOrigen;

		//$totalCuentaDestino = $saldoOrigen + $saldoDestino;

		if (mysqli_num_rows($totalCuentaOrigen) > 0 ) {
			mysqli_query($cnx, "UPDATE cuenta set saldo = '$totalCuentaOrigen' where nrocuenta = '$nrocuentaorigen'");
			//mysqli_query($cnx,"INSERT INTO transaccion (nrocuentaorigen,nrocuentadestino,valor) VALUES ('$nrocuentaorigen','$nrocuentadestino','$totalCuentaOrigen')");	
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