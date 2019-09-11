package com.example.bancoco.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MostrarDataUserFragment extends Fragment {

    private RequestQueue mQueue;
    private TextView id, emailInfo, nombresInfo, saldo;
    private Spinner cuentasUsuario;

    // Recibir data de user logueado de la actividad MenuActivity.
    public final static String ident = "ident";
    public final static String nombres = "nombres";
    public final static String email = "email";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_mostrar_data_user,container,false);

        id = vista.findViewById(R.id.tvIdUsuario);
        nombresInfo = vista.findViewById(R.id.tvNameInfo);
        emailInfo = vista.findViewById(R.id.tvEmailInfo);
        cuentasUsuario = vista.findViewById(R.id.spCuentasEnd);
        saldo = vista.findViewById(R.id.tvMoneyInfo);

        mQueue = Volley.newRequestQueue(getContext());

        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            id.setText(args.getString(ident));
            nombresInfo.setText(args.getString(nombres));
            emailInfo.setText(args.getString(email));
        }

        // Spinner con las cuentas asociadas al usuario.
        cuentasUsuarios();
    }

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
                        cuentasUsuario.setAdapter(lo_adp_tipos);
                    }

                    //Identificar cuando es presionado en alguno de los elementos.
                    cuentasUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //Marca posición sobre que elemento hemos seleccionado.
                            String lo_tipos = (String) cuentasUsuario.getAdapter().getItem(position);

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

    private void saldoUsuarios(){
        String cuenta = (String)cuentasUsuario.getSelectedItem();
        String url = "http://192.168.1.74:8089/web-services-banco/saldoUsuarios.php/?nrocuenta="+cuenta;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos");

                    for(int i = 0; i < jsonArray.length(); i++){
                        saldo.setText(" ");
                        JSONObject data = jsonArray.getJSONObject(i);
                        String saldoCuenta = data.getString("saldo");

                        // Cuentas asociadas al id del usuario logueado.
                        saldo.append(saldoCuenta);
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
}
