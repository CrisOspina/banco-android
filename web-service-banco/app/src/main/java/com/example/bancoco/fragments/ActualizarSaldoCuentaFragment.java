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
import android.widget.TextView;
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

public class ActualizarSaldoCuentaFragment extends Fragment {

    private Button btnNuevoSaldo;
    private EditText numeroCuenta, saldoNuevo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_actualizar_saldo_cuenta,container,false);
        numeroCuenta = vista.findViewById(R.id.etNumeroCuenta);
        saldoNuevo = vista.findViewById(R.id.etValorNuevo);
        btnNuevoSaldo = vista.findViewById(R.id.btnValorNuevo);

        btnNuevoSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarIngreso();
            }
        });

        return vista;
    }

    private void validarIngreso(){
        final String saldo = saldoNuevo.getText().toString();
        final String numero = numeroCuenta.getText().toString();

        if(saldo.isEmpty() || numero.isEmpty()){
            Toast.makeText(getContext(), "Campos obligatorios", Toast.LENGTH_SHORT).show();
        } else {
            addMoney();
            limpiarCampos();
            //navegarIniciarSesion();
        }
    }

    private void addMoney(){
        final String saldo = saldoNuevo.getText().toString();
        final String numero = numeroCuenta.getText().toString();

        //String url = "http://192.168.1.74:8089/web-services-banco/insertarSaldo.php";
		String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/insertarSaldo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Failed, please check again" + error, Toast.LENGTH_SHORT).show();
                Log.d("","error" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("saldo", saldo);
                parametros.put("nrocuenta", numero);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void  limpiarCampos(){
        saldoNuevo.setText("");
        numeroCuenta.setText("");
    }

}
