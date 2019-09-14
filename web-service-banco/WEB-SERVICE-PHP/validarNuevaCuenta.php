<?php 
 include 'conexion.php';
 
   $numeroNuevoCuenta = $cnx->query("select nrocuenta from cuenta");
 
   $json = array();
 
   foreach ($numeroNuevoCuenta as $fila) {
     $json['datos'][]=$fila;
   }
 
   //pasar los datos del array a JSON con informacion o vacío
   echo json_encode($json);
	
 ?>