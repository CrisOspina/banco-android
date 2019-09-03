package com.example.bancoco.fragments;

import android.content.Context;
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
import com.example.bancoco.R;

import java.util.HashMap;
import java.util.Map;

public class CrearCuentaFragment extends Fragment {

    private Button btnCrearCuenta;
    private EditText cedula, numeroCuenta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_crear_cuenta,container,false);
        cedula = vista.findViewById(R.id.etCedula);
        numeroCuenta = vista.findViewById(R.id.etNumeroCuenta);

        btnCrearCuenta = vista.findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarIngreso();
            }
        });
        return vista;
    }

    private void validarIngreso(){
        final String id = cedula.getText().toString();
        final String numero = numeroCuenta.getText().toString();

        if(id.isEmpty() || numero.isEmpty()){
            Toast.makeText(getContext(), "Campos obligatorios", Toast.LENGTH_SHORT).show();
        } else {
            registrarCuenta();
            limpiarCampos();
            //navegarIniciarSesion();
        }
    }

    private  void registrarCuenta(){
        final String id = cedula.getText().toString();
        final String numero = numeroCuenta.getText().toString();

        String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/registrocuenta.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Cuenta Registrada", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Fallo el registro, por favor verifica nuevamente" + error, Toast.LENGTH_SHORT).show();
                Log.d("","error" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("ident", id);
                parametros.put("nrocuenta", numero);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void  limpiarCampos(){
        cedula.setText("");
        numeroCuenta.setText("");
    }

}
