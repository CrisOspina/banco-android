<?php 
 include 'conexion.php';
	if (isset($_REQUEST['email']) && isset($_REQUEST['clave'])) {
		$email=$_REQUEST['email'];
		$clave=$_REQUEST['clave'];
		$registros = $cnx->query("select ident,email,nombres,clave from cliente where email = '$email' and clave = '$clave'");
		//si existe el usuario la variable res queda en 1 y sino en 0
		//En este arreglo se guardará la informacion para pasarla a JSON
		$json = array();

		foreach ($registros as $fila) {
			$json['datos'][]=$fila;
		}
		//pasar los datos del array a JSON con informacion o vacío
		echo json_encode($json);
	} else {
		echo "El usuario y la clave son obligatorios";
	}
 ?>