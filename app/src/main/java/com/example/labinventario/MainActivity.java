package com.example.labinventario;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labinventario.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static String TAG = "msg-test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonIngresar.setOnClickListener(view -> {
            String codigo = binding.editTextCodigo.getText().toString().trim();

            if (codigo.isEmpty()) {
                binding.editTextCodigo.setError("Ingrese su código PUCP");
                return;
            }

            if (!tengoInternet()) {
                Toast.makeText(MainActivity.this,
                        "No hay conexión a Internet. No es posible continuar.",
                        Toast.LENGTH_LONG).show();
                Log.d(TAG, "Sin conexión a Internet");
                return;
            }

            Log.d(TAG, "Ingresando con código: " + codigo);
            Intent intent = new Intent(MainActivity.this, ListaEquiposActivity.class);
            startActivity(intent);
        });
    }

    public boolean tengoInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) return false;

        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (capabilities == null) return false;

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}
