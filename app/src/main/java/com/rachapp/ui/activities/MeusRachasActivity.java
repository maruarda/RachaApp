package com.rachapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.Racha;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.RachasAdapter;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeusRachasActivity extends AppCompatActivity {

    private RecyclerView rvRachas;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_rachas);

        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        rvRachas = findViewById(R.id.rvRachasMain);
        rvRachas.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnCriarRacha).setOnClickListener(v -> {
            Intent intent = new Intent(this, CriarRachaActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRachas();
    }

    private void carregarRachas() {
        // UPDATED: Call getMeusRachas instead of getTodosRachas
        Call<List<Racha>> call = RetrofitClient.getInstance().getApi().getMeusRachas(currentUserId);

        call.enqueue(new Callback<List<Racha>>() {
            @Override
            public void onResponse(Call<List<Racha>> call, Response<List<Racha>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Racha> lista = response.body();
                    RachasAdapter adapter = new RachasAdapter(lista, rachaId -> {
                        Intent intent = new Intent(MeusRachasActivity.this, RachaDetailActivity.class);
                        intent.putExtra("RACHA_ID", rachaId);
                        intent.putExtra("USER_ID", currentUserId);
                        startActivity(intent);
                    });
                    rvRachas.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Racha>> call, Throwable t) {
                Toast.makeText(MeusRachasActivity.this, "Erro ao carregar rachas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}