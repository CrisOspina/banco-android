<?php 
 include 'conexion.php';		
 
 $nrocuenta=$_REQUEST['nrocuenta'];

	$registros = $cnx->query("SELECT cuenta.saldo FROM cuenta WHERE cuenta.nrocuenta = '$nrocuenta'");

	/* 
		http://localhost:8089/web-services-banco/cuentasUsuarioId.php/?nrocuenta=nrocuenta
	*/
	
	//En este arreglo se guardará la informacion para pasarla a JSON
	$json = array();

	foreach ($registros as $fila) {
		$json['datos'][]=$fila;
	}

	//pasar los datos del array a JSON con informacion o vacío
	echo json_encode($json);
	
 ?>