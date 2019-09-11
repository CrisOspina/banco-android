<?php 
 include 'conexion.php';			

	$registros = $cnx->query("SELECT cliente.ident, cliente.email, cliente.nombres, cuenta.nrocuenta, cuenta.saldo  FROM cliente INNER JOIN cuenta WHERE cliente.ident = cuenta.ident");

	/*
		http://localhost:8089/web-services-banco/data.php/
	*/
	
	//En este arreglo se guardará la informacion para pasarla a JSON
	$json = array();

	foreach ($registros as $fila) {
		$json['datos'][]=$fila;
	}

	//pasar los datos del array a JSON con informacion o vacío
	echo json_encode($json);
	
 ?>