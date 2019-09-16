<?php 
 include 'conexion.php';
	if (isset($_REQUEST['email'])) {
		$email=$_REQUEST['email'];
	
    $registros = $cnx->query("select email from cliente where email = '$email'");
    
    // Validar si la consulta si contiene data
		if($registros->num_rows){
			$row = $registros->fetch_object();
			$emailUser =  $row->email;
    }

    // Validación del email
    function is_valid_email($str) {
      return (false !== filter_var($str, FILTER_VALIDATE_EMAIL));
    }
   
    if(is_valid_email($emailUser)) { 
      echo "El email es valido"; 
    } else { 
      echo "El email NO es valido"; 
    }
  }
  else {
		echo "Email requerido";
	}
 ?>