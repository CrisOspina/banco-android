package com.example.bancoco.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.models.Cliente;
import com.example.bancoco.MenuActivity;
import com.example.bancoco.R;
import com.example.bancoco.RegistroActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IniciarSesionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

	private Button iniciar, registrar;
	private EditText email, clave;

	//objetos para la conexión.
	private RequestQueue rq;
	private JsonRequest jrq;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View vista = inflater.inflate(R.layout.fragment_iniciar_sesion,container,false);
		email = vista.findViewById(R.id.etEmailLogin);
		clave = vista.findViewById(R.id.etClave);
		iniciar = vista.findViewById(R.id.btnIniciarSesion);
		registrar = vista.findViewById(R.id.btnRegistrarse);

		//Requerimiento Volley
		rq = Volley.newRequestQueue(getContext());

		iniciar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iniciarSesion();
			}
		});

		registrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), RegistroActivity.class);
				startActivity(intent);
			}
		});

		return vista;
	}

	private void iniciarSesion() {
		String correo = email.getText().toString();
		String password = clave.getText().toString();

		String url = "http://192.168.1.74:8089/web-services-banco/sesion.php?email="+correo+"&clave="+password;
		//String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/sesion.php?email="+correo+"&clave="+password;
		jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
		rq.add(jrq);
	}

	private  void obtenerDatosDelUsuario(JSONObject response){
		//Se utiliza la clase usuario para tomar los campos del arreglo datos del archivo php
		Cliente cliente = new Cliente();

		//datos: arreglo que envia los datos en formato JSON, en el archivo php
		JSONArray jsonArray = response.optJSONArray("datos");

		JSONObject jsonObject = null;

		try {
			jsonObject = jsonArray.getJSONObject(0); //posición 0 del arreglo
			cliente.setIdent(jsonObject.optString("ident"));
			cliente.setClave(jsonObject.optString("clave"));
			cliente.setNombres(jsonObject.optString("nombres"));
			cliente.setEmail(jsonObject.optString("email"));
		} catch (JSONException e){
			e.printStackTrace();
		}

		Intent intent = new Intent(getContext(), MenuActivity.class);
		intent.putExtra(MenuActivity.ident, cliente.getIdent());
		intent.putExtra(MenuActivity.nombres, cliente.getNombres());
		intent.putExtra(MenuActivity.emall, cliente.getEmail());
		startActivity(intent);
	}

	@Override
	public void onResponse(JSONObject response) {
		Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
		obtenerDatosDelUsuario(response);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(getContext(), "User not found, please check again", Toast.LENGTH_SHORT).show();
	}
}
