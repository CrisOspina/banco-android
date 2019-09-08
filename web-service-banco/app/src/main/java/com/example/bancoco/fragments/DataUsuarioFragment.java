package com.example.bancoco.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.bancoco.Cliente;
import com.example.bancoco.DataCuentaUserActivity;
import com.example.bancoco.MenuActivity;
import com.example.bancoco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataUsuarioFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    //TextView nom;
    private RequestQueue rq;
    private JsonRequest jrq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_data_usuario,container,false);

        //Requerimiento Volley
        rq = Volley.newRequestQueue(getContext());

        String url = "http://192.168.1.74:8089/web-services-banco/data.php";
        jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);

        //nom = vista.findViewById(R.id.tvNombre);


        return vista;
    }


    private  void obtenerDatosDelUsuario(JSONObject response){
        //Se utiliza la clase usuario para tomar los campos del arreglo datos del archivo php
        Cliente cliente = new Cliente();

        //datos: arreglo que envia los datos en formato JSON, en el archivo php
        JSONArray jsonArray = response.optJSONArray("datos");

        JSONObject jsonObject = null;

        try {
            jsonObject = jsonArray.getJSONObject(0); //posición 0 del arreglo
            cliente.setNombres(jsonObject.optString("nombres"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        Intent intent = new Intent(getContext(), DataCuentaUserActivity.class);
        intent.putExtra(DataCuentaUserActivity.nombre, cliente.getNombres());
        startActivity(intent);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        obtenerDatosDelUsuario(response);
    }
}
