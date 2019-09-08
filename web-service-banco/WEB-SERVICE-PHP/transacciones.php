<?php
  if (isset($_REQUEST['nrocuentaorigen']) && 
			isset($_REQUEST['nrocuentadestino']) && 
			isset($_REQUEST['valor'])) {
	
		$nrocuentaorigen = $_POST['nrocuentaorigen'];
		$nrocuentadestino = $_POST['nrocuentadestino'];
		$valor = $_POST['valor'];

		// Conexión a la BD
		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");
		
		// Saldo actual de cuenta origen y destino
		$result = mysqli_query($cnx, "select saldo from cuenta where nrocuenta = '$nrocuentaorigen'");
		$resultDestino = mysqli_query($cnx, "select saldo from cuenta where nrocuenta = '$nrocuentadestino'");
		
		// Validar si la consulta si contiene data
		if($result->num_rows && $resultDestino->num_rows){
			$row = $result->fetch_object();
			$rowDestino = $resultDestino->fetch_object();
			$saldoOrigen =  $row->saldo;
			$saldoDestino = $rowDestino->saldo;
		}
		
		// Validación del saldo y actualización de la cuenta origen y cuenta destino.
		if($valor < $saldoOrigen){
			$updateSaldoOrigen = $saldoOrigen - $valor;
			$updateSaldoDestino = $saldoDestino + $valor;		
		} else {
			echo 'El valor excede el saldo origen de la cuenta';
		}

		// Actualización del saldo de la cuenta origen.
		mysqli_query($cnx, "UPDATE cuenta SET saldo = '$updateSaldoOrigen' WHERE nrocuenta = '$nrocuentaorigen'");

		// Actualización del saldo de la cuenta destino.
		mysqli_query($cnx, "UPDATE cuenta SET saldo = '$updateSaldoDestino' WHERE nrocuenta = '$nrocuentadestino'");

		// Insertar transacción a la tabla `transacciones`
		mysqli_query($cnx,"INSERT INTO transaccion (nrocuentaorigen,nrocuentadestino,valor) VALUES ('$nrocuentaorigen','$nrocuentadestino','$valor')");	

		mysqli_close($cnx);
	}
	else {
		echo "Debe especificar nrocuentaorigen, nrocuentadestino y valor...";
	}
?>