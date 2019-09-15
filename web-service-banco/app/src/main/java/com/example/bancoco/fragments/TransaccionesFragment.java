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
import android.widget.Spinner;
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
import com.example.bancoco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransaccionesFragment extends Fragment {

    // Recibir id de user logueado de la actividad MenuActivity.
    public final static String ident = "ident";

    // Componentes del layout
    private Button btnTransaccion;
    private EditText id, destino, valor;
    private RequestQueue mQueue;
    private Spinner spOrigen;
    private TextView tvMoney;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_transacciones,container,false);
        id = vista.findViewById(R.id.etIdUserLogin);
        destino = vista.findViewById(R.id.etDestino);
        valor = vista.findViewById(R.id.etValor);
        spOrigen = vista.findViewById(R.id.spOrigen);
        tvMoney = vista.findViewById(R.id.tvMoneyTransacciones);
        btnTransaccion = vista.findViewById(R.id.btnTransaccion);

        mQueue = Volley.newRequestQueue(getContext());

        btnTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarIngresoCuenta();
            }
        });
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            id.setText(args.getString(ident));
        }

        // Spinner con las cuentas asociadas al usuario.
        cuentasUsuarios();
    }

    // -- Métodos secundarios

    // Obtener en un spinner las cuentas de usuario
    private void cuentasUsuarios(){
        final String ident = id.getText().toString();
        String url = "http://192.168.1.74:8089/web-services-banco/cuentasUsuarioId.php/?ident="+ident;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<String> cuentasUser = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONArray("datos");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String nrocuenta = data.getString("nrocuenta");

                        // Cuentas asociadas al id del usuario logueado.
                        cuentasUser.add(nrocuenta);

                        //Se crea adaptador para recorrer cuentas
                        ArrayAdapter lo_adp_tipos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cuentasUser);
                        spOrigen.setAdapter(lo_adp_tipos);
                    }

                    // Identificar cuando es presionado en alguno de los elementos.
                    spOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Marca posición sobre que elemento hemos seleccionado.
                            String lo_tipos = (String) spOrigen.getAdapter().getItem(position);

                            // Saldo de cuentas del usuario logueado
                            saldoUsuarios();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } catch (JSONException e){
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

    // Validar el ingreso de cuenta
	private void validarIngresoCuenta(){
        final String cDestino = destino.getText().toString();
        final String cValor = valor.getText().toString();

        String url = "http://192.168.1.74:8089/web-services-banco/validarNuevaCuenta.php/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            ArrayList<String> cuentasUser = new ArrayList<>();
            @Override
            public void onResponse(JSONObject response) {

                if(cDestino.isEmpty() || cValor.isEmpty()){
                    Toast.makeText(getContext(), "Obligatory", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = response.getJSONArray("datos");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String nroCuentaBD = data.getString("nrocuenta");

                            //cuentasUser.add(nroCuentaBD);

                            if (cDestino.equals(nroCuentaBD)) {
                                validarSaldoDeCuenta();
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

    // Validar el saldo
    private void validarSaldoDeCuenta(){
        final String cOrigen = spOrigen.getSelectedItem().toString();
        final String cValor = valor.getText().toString();

        String url = "http://192.168.1.74:8089/web-services-banco/validarSaldo.php/?nrocuenta="+cOrigen;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            ArrayList<String> cuentasUser = new ArrayList<>();
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        final String saldoBD = data.getString("saldo");

                        int valorIngresado = Integer.parseInt(cValor);
                        int valorDB = Integer.parseInt(saldoBD);

                        if(valorIngresado < valorDB){
                            registrarTransaccion();
                            return;
                        } else {
                            Toast.makeText(getContext(), "Insufficient account balance", Toast.LENGTH_SHORT).show();
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

    // Relizar transacción entre cuentas existentes
    private  void registrarTransaccion(){
        final String cOrigen = spOrigen.getSelectedItem().toString();
        final String cDestino = destino.getText().toString();
        final String cValor = valor.getText().toString();

        String url = "http://192.168.1.74:8089/web-services-banco/transacciones.php";
		//String url = "http://172.16.22.6:8082/banco-php-android/web-service-banco/WEB-SERVICE-PHP/transacciones.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Succesful transaction", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Transaction failed, please check again" + error, Toast.LENGTH_SHORT).show();
                Log.d("","error" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nrocuentaorigen", cOrigen);
                parametros.put("nrocuentadestino", cDestino);
                parametros.put("valor", cValor);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    // Impresión de saldo de las cuentas del usuario logueado
    private void saldoUsuarios(){
        String cuenta = (String)spOrigen.getSelectedItem();
        String url = "http://192.168.1.74:8089/web-services-banco/saldoUsuarios.php/?nrocuenta="+cuenta;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos");

                    for(int i = 0; i < jsonArray.length(); i++){
                        tvMoney.setText(" ");
                        JSONObject data = jsonArray.getJSONObject(i);
                        String saldoCuenta = data.getString("saldo");

                        // Cuentas asociadas al id del usuario.
                        tvMoney.append(saldoCuenta);
                    }
                } catch (JSONException e){
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

    private void  limpiarCampos(){
        destino.setText("");
        valor.setText("");
    }
}
