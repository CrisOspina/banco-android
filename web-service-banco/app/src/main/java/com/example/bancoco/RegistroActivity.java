package com.example.bancoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.bancoco.fragments.RegistrarseFragment;

public class RegistroActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);

		// El fragment se inflará en el layout con id escenario.
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.escenario,new RegistrarseFragment()).commit();
	}
}
