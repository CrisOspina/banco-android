<?php 
 include 'conexion.php';		
 
 $ident=$_REQUEST['ident'];

	$registros = $cnx->query("SELECT cuenta.nrocuenta FROM cuenta WHERE cuenta.ident = '$ident'");

	/* 
		http://localhost:8089/web-services-banco/cuentasUsuarioId.php/?ident=id
	*/
	
	//En este arreglo se guardará la informacion para pasarla a JSON
	$json = array();

	foreach ($registros as $fila) {
		$json['datos'][]=$fila;
	}

	//pasar los datos del array a JSON con informacion o vacío
	echo json_encode($json);
	
 ?>