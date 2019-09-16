<?php 
 include 'conexion.php';
  // $ident=$_REQUEST['ident'];

  $registro = $cnx->query("select ident from cliente ");
 
  $json = array();

  foreach ($registro as $fila) {
    $json['datos'][]=$fila;
  }

  //pasar los datos del array a JSON con informacion o vacío
  echo json_encode($json);
  
 ?>