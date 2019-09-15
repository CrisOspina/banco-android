<?php 
 include 'conexion.php';

  $nrocuenta=$_REQUEST['nrocuenta'];

  $numeroNuevoCuenta = $cnx->query("select saldo, nrocuenta from cuenta where nrocuenta = '$nrocuenta'");

  $json = array();

  foreach ($numeroNuevoCuenta as $fila) {
    $json['datos'][]=$fila;
  }

  //pasar los datos del array a JSON con informacion o vacío
  echo json_encode($json);
	
?>