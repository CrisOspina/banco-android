<?php 
	$correo=$_REQUEST['email'];
	$clave=$_REQUEST['clave'];
	$cnx =  mysqli_connect("localhost","root","","banco-co");

	//Ejecutar una sentencia SELECT y recibir una respuesta
	$res=$cnx->query("select * from cliente where email = '$email' and clave = '$clave'");

	//si existe el usuario la variable res queda en 1 y sino en 0
	//En este arreglo se guardará la informacion para pasarla a JSON
	$datos = array();
	foreach ($res as $row) {
		$json['datos'][]=$row;
	}

	//pasar los datos del array a JSON con informacion o vacío
	echo json_encode($datos);
 ?>