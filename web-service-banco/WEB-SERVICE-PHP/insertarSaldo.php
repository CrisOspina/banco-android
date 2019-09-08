<?php
	if (isset($_REQUEST['saldo']) && isset($_REQUEST['nrocuenta'])) {
		$saldoActual = $_POST['saldo'];
		$nrocuenta = $_POST['nrocuenta'];

		$cnx =  mysqli_connect("localhost","root","","banco-co") or die("Ha sucedido un error inexperado en la conexion de la base de datos");

    $result = mysqli_query($cnx,"select saldo from cuenta where nrocuenta = '$nrocuenta'");				

    // Validar si la consulta si contiene data
		if($result->num_rows){
			$row = $result->fetch_object();
      $saldoOrigen =  $row->saldo;      
    }
    
    // Ingreso permitido para aumentar el saldo de la cuenta.
		if($saldoActual > 0){
			$updateSaldo = $saldoOrigen + $saldoActual;
		} else {
			echo 'La recarga debe ser superior a 0';
    }

    mysqli_query($cnx, "UPDATE cuenta SET saldo = '$updateSaldo' WHERE nrocuenta = '$nrocuenta'");

		mysqli_close($cnx);
	}
	else {
		echo "Debe especificar saldo y nrocuenta, respectivamente...";
	}
?>
