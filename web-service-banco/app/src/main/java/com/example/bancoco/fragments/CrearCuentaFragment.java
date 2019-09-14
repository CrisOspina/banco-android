package com.example.bancoco.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrearCuentaFragment extends Fragment {

    // Recibir id de user logueado de la actividad MenuActivity.
    public final static String ident = "ident";

    // Componentes del layout
    private Button btnCrearCuenta;
    private EditText cedula, numeroCuenta;

    // Objetos para la conexión.
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_crear_cuenta,container,false);
        cedula = vista.findViewById(R.id.etCedula);
        numeroCuenta = vista.findViewById(R.id.etNumeroCuenta);
        btnCrearCuenta = vista.findViewById(R.id.btnCrearCuenta);

        mQueue = Volley.newRequestQueue(getContext());

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarIngreso();
            }
        });
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            cedula.setText(args.getString(ident));
        }
    }

    // -- Métodos secundarios

    // Verificación con la BD para saber si existe la cuenta
    private void validarIngreso(){
        final String numero = numeroCuenta.getText().toString();
        String url = "http://192.168.1.74:8089/web-services-banco/validarNuevaCuenta.php/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(numero.isEmpty()){
                    Toast.makeText(getContext(), "Obligatory", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = response.getJSONArray("datos");

                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            final String nroCuentaBD = data.getString("nrocuenta");

                            if (numero.equals(nroCuentaBD)) {
                                Toast.makeText(getContext(), "Account already exists, verify", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                                return;
                            } else {
                                registrarCuenta();
                                limpiarCampos();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    // Crear cuenta nueva
    private void registrarCuenta() {
        final String id = cedula.getText().toString();
        final String numero = numeroCuenta.getText().toString();

        String url = "http://192.168.1.74:8089/web-services-banco/registrocuenta.php";
        //String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/registrocuenta.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Successful registered account", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Registration failed, please check again" + error, Toast.LENGTH_SHORT).show();
                Log.d("", "error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("ident", id);
                parametros.put("nrocuenta", numero);
                return parametros;
            }
        };

        mQueue.add(stringRequest);
    }

    private void  limpiarCampos(){
        numeroCuenta.setText("");
    }
}
