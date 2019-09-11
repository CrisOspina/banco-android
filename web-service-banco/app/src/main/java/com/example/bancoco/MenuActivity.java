package com.example.bancoco;

import android.content.Intent;
import android.os.Bundle;

import com.example.bancoco.fragments.ActualizarSaldoCuentaFragment;
import com.example.bancoco.fragments.CrearCuentaFragment;
import com.example.bancoco.fragments.MostrarDataUserFragment;
import com.example.bancoco.fragments.TransaccionesFragment;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // id de usuario logueado.
    public static final String ident = "ident";
    public static final String nombres = "nombres";
    public static final String emall = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_home) {
            miFragment = new MostrarDataUserFragment();
            Bundle bundle = new Bundle();
            String idUserLogIn = getIntent().getStringExtra("ident");
            String nameUserLogin = getIntent().getStringExtra("nombres");
            String emailUserLogin = getIntent().getStringExtra("email");
            bundle.putString(MostrarDataUserFragment.ident, idUserLogIn);
            bundle.putString(MostrarDataUserFragment.nombres, nameUserLogin);
            bundle.putString(MostrarDataUserFragment.email, emailUserLogin);
            miFragment.setArguments(bundle);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_crear_cuenta) {
            miFragment = new CrearCuentaFragment();
            Bundle bundle = new Bundle();
            String idUserLogIn = getIntent().getStringExtra("ident");
            bundle.putString(CrearCuentaFragment.ident, idUserLogIn);
            miFragment.setArguments(bundle);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_transacciones) {
            miFragment = new TransaccionesFragment();
            Bundle bundle = new Bundle();
            String idUserLogIn = getIntent().getStringExtra("ident");
            bundle.putString(TransaccionesFragment.ident, idUserLogIn);
            miFragment.setArguments(bundle);
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_saldo){
            miFragment = new ActualizarSaldoCuentaFragment();
            Bundle bundle = new Bundle();
            String idUserLogIn = getIntent().getStringExtra("ident");
            bundle.putString(ActualizarSaldoCuentaFragment.ident, idUserLogIn);
            miFragment.setArguments(bundle);
            fragmentSeleccionado = true;
        }

        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.escenario, miFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
