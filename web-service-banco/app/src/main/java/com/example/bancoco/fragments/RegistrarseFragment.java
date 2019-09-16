package com.example.bancoco.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.MainActivity;
import com.example.bancoco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrarseFragment extends Fragment {

	// Componentes del layout
	private EditText ident, email, nombres, clave;
	private Button registrar;
	private TextView success;

	private RequestQueue mQueue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View vista = inflater.inflate(R.layout.fragment_registrarse,container,false);
		ident = vista.findViewById(R.id.etIdent);
		clave = vista.findViewById(R.id.etClave);
		nombres = vista.findViewById(R.id.etNombres);
		email = vista.findViewById(R.id.etEmail);
		success = vista.findViewById(R.id.tvSuccess);
		registrar = vista.findViewById(R.id.btnRegistrarse);

		mQueue = Volley.newRequestQueue(getContext());

		registrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validarIngreso();
			}
		});
		return vista;
	}

	// -- Métodos secundarios

	private void validarIngreso(){
		final String pass = clave.getText().toString();
		final String names = nombres.getText().toString();
		final String correo = email.getText().toString();
		final String id = ident.getText().toString();

		if(pass.isEmpty() || names.isEmpty() || correo.isEmpty() || id.isEmpty()){
			Toast.makeText(getContext(), "All mandatory fields", Toast.LENGTH_SHORT).show();
			return;
		}

		if(!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
			email.setError("Invalid email address");
			//Toast.makeText(getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
		} else {
			validarIdentificacion();
		}
	}

	// Validación del formato email y si ya existe en la bd
	private void validarEmail() {
		final String correo = email.getText().toString();

		String url = "http://192.168.1.74:8089/web-services-banco/validarEmailRegistro.php/";
		final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray jsonArray = response.getJSONArray("datos");

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject data = jsonArray.getJSONObject(i);
						String emailDB = data.getString("email");

						if (correo.equals(emailDB)) {
							email.setError("EMAIL already exists");
							break;
						} else {
							registrarUsuario();
							navegarIniciarSesion();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		mQueue.add(request);
	}

	// Validar identificación
	private void validarIdentificacion(){
		final String id = ident.getText().toString();

		String url = "http://192.168.1.74:8089/web-services-banco/validarIdentificacion.php/";
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray jsonArray = response.getJSONArray("datos");

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject data = jsonArray.getJSONObject(i);
						String identBD = data.getString("ident");

						if(id.equals(identBD)) {
							ident.setError("ID already exists");
							//Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
							return;
						} else {
							validarEmail();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
			}
		});

		mQueue.add(request);
	}

	private void registrarUsuario(){
		final String id = ident.getText().toString();
		final String pass  = clave.getText().toString();
		final String names = nombres.getText().toString();
		final String correo = email.getText().toString();

		String url = "http://192.168.1.74:8089/web-services-banco/registro.php";
		//String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/registro.php";
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				//success.setText("Successful");
				//Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getContext(), "Registration failed, please check again" + error, Toast.LENGTH_SHORT).show();
				Log.d("","error" + error);
			}
		})
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> parametros = new HashMap<String, String>();
				parametros.put("ident", id);
				parametros.put("nombres", names);
				parametros.put("email", correo);
				parametros.put("clave", pass);
				return parametros;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(getContext());
		requestQueue.add(stringRequest);
	}

	private void  limpiarCampos(){
		ident.setText("");
		clave.setText("");
		nombres.setText("");
		email.setText("");
	}

	private void navegarIniciarSesion(){
		Intent intent = new Intent(getContext(), MainActivity.class);
		startActivity(intent);
	}
}
