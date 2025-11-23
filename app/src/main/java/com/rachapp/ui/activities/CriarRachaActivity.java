package com.rachapp.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import com.rachapp.R;
import com.rachapp.data.model.Racha;
import com.rachapp.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarRachaActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private Double currentLat = null;
    private Double currentLon = null;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_racha);

        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            checkLocationPermissionAndGetLocation();
        } catch (Exception e) {
            Toast.makeText(this, "Serviço de localização indisponível", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.btnCriarRachaFinal).setOnClickListener(v -> criarRacha());
    }

    // ... existing permission code ...
    private void checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLon = location.getLongitude();
                        Toast.makeText(this, "GPS Localizado!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }
    // ...

    private void criarRacha() {
        EditText etNome = findViewById(R.id.etNomeRacha);
        EditText etLocal = findViewById(R.id.etLocalRacha);

        String nome = etNome.getText().toString().trim();
        String localNome = etLocal.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Dê um nome para o racha!", Toast.LENGTH_SHORT).show();
            return;
        }

        // FIXED: Pass currentUserId as ownerId
        Racha novoRacha = new Racha(nome, localNome, currentLat, currentLon, currentUserId);

        Call<Racha> call = RetrofitClient.getInstance().getApi().criarRacha(novoRacha);
        call.enqueue(new Callback<Racha>() {
            @Override
            public void onResponse(Call<Racha> call, Response<Racha> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CriarRachaActivity.this, "Racha Criado!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CriarRachaActivity.this, RachaDetailActivity.class);
                    intent.putExtra("RACHA_ID", response.body().getIdRacha());
                    intent.putExtra("USER_ID", currentUserId);
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(CriarRachaActivity.this, "Erro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Racha> call, Throwable t) {
                Toast.makeText(CriarRachaActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}