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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.MainActivity;
import com.example.bancoco.R;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseFragment extends Fragment {

	private EditText ident, email, nombres, clave;
	private Button registrar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View vista = inflater.inflate(R.layout.fragment_registrarse,container,false);
		ident = vista.findViewById(R.id.etIdent);
		clave = vista.findViewById(R.id.etClave);
		nombres = vista.findViewById(R.id.etNombres);
		email = vista.findViewById(R.id.etEmail);
		registrar = vista.findViewById(R.id.btnRegistrarse);

		registrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validarIngreso();
			}
		});
		return vista;
	}

	private void validarIngreso(){
		final String id = ident.getText().toString();
		final String pass = clave.getText().toString();
		final String names = nombres.getText().toString();
		final String correo = email.getText().toString();

		if(id.isEmpty() || pass.isEmpty() || names.isEmpty() || correo.isEmpty()){
			Toast.makeText(getContext(), "All mandatory fields", Toast.LENGTH_SHORT).show();
		} else {
			registrarUsuario();
			limpiarCampos();
			navegarIniciarSesion();
		}
	}

	private  void registrarUsuario(){
		final String id = ident.getText().toString();
		final String pass  = clave.getText().toString();
		final String names = nombres.getText().toString();
		final String correo = email.getText().toString();

		String url = "http://192.168.1.74:8089/web-services-banco/registro.php";
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Toast.makeText(getContext(), "Successful registration", Toast.LENGTH_SHORT).show();
				limpiarCampos();
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
