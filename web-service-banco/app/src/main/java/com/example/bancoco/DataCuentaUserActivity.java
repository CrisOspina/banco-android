package com.example.bancoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bancoco.fragments.DataUsuarioFragment;

public class DataCuentaUserActivity extends AppCompatActivity {

    public static final String nombre = "nombres";
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_cuenta_user);

        //flecha atras.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.tvNombre);

        Fragment fragment = new DataUsuarioFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.escenarioDataCuenta, fragment).commit();

        String nombre = getIntent().getStringExtra("nombres");
        name.setText(nombre);
    }
}
