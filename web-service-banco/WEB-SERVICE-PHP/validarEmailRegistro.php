<?php 
 include 'conexion.php';
    // $email=$_REQUEST['email'];

    $registro = $cnx->query("select email from cliente");

    $json = array();

    foreach ($registro as $fila) {
      $json['datos'][]=$fila;
    }
  
    //pasar los datos del array a JSON con informacion o vacío
    echo json_encode($json);
 ?>