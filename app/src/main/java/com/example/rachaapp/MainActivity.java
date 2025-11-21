package com.example.rachaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rachaapp.adapter.AmigosAdapter;
import com.example.rachaapp.adapter.RachasAdapter;
import com.example.rachaapp.model.AmigoResponse;
import com.example.rachaapp.model.Racha;
import com.example.rachaapp.network.RachaApi;
import com.example.rachaapp.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AmigosAdapter amigosAdapter;
    private RachasAdapter rachasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        var rvAmigos = findViewById(R.id.rvAmigosMain);
        var rvRachas = findViewById(R.id.rvRachasMain);
        var fab = findViewById(R.id.btnCriarRacha);

        rvAmigos.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvRachas.setLayoutManager(new LinearLayoutManager(this));

        amigosAdapter = new AmigosAdapter(new ArrayList<>());
        rachasAdapter = new RachasAdapter(new ArrayList<>());

        rvAmigos.setAdapter(amigosAdapter);
        rvRachas.setAdapter(rachasAdapter);

        loadAmigos(1L);        // ← change later for logged user
        loadRachas();

        fab.setOnClickListener(v ->
                Toast.makeText(this, "TODO: go to create racha screen", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadAmigos(Long idUsuario) {
        RachaApi api = RetrofitClient.getInstance().create(RachaApi.class);
        api.getAmigos(idUsuario).enqueue(new Callback<List<AmigoResponse>>() {
            @Override
            public void onResponse(Call<List<AmigoResponse>> call, Response<List<AmigoResponse>> response) {
                if (!response.isSuccessful()) return;
                amigosAdapter = new AmigosAdapter(response.body());
                ((androidx.recyclerview.widget.RecyclerView) findViewById(R.id.rvAmigosMain))
                        .setAdapter(amigosAdapter);
            }

            @Override
            public void onFailure(Call<List<AmigoResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro ao carregar amigos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRachas() {
        RachaApi api = RetrofitClient.getInstance().create(RachaApi.class);
        api.getAllRachas().enqueue(new Callback<List<Racha>>() {
            @Override
            public void onResponse(Call<List<Racha>> call, Response<List<Racha>> response) {
                if (!response.isSuccessful()) return;
                rachasAdapter = new RachasAdapter(response.body());
                ((androidx.recyclerview.widget.RecyclerView) findViewById(R.id.rvRachasMain))
                        .setAdapter(rachasAdapter);
            }

            @Override
            public void onFailure(Call<List<Racha>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro ao carregar rachas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
