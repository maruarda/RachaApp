package com.rachapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.ui.adapters.AmigosAdapter;
import com.rachapp.data.model.ResumoDTO;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.dialogs.SearchFriendDialog;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvAmigos;
    private long currentUserId = -1;
    private String currentUserName;
    private int currentUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 1. Capture Data (Done once)
        currentUserName = getIntent().getStringExtra("USER_NAME");
        currentUserAvatar = getIntent().getIntExtra("USER_AVATAR", 1);
        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        setupHeader(currentUserName, currentUserAvatar);

        rvAmigos = findViewById(R.id.rvAmigosHome);
        rvAmigos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 4. Button Listeners
        findViewById(R.id.btnAddAmigo).setOnClickListener(v -> {
            if (currentUserId != -1) {
                // Passing carregarAmigos as callback so list refreshes immediately after adding
                new SearchFriendDialog(this, currentUserId, this::carregarAmigos).show();
            }
        });

        findViewById(R.id.imgPerfilHome).setOnClickListener(v -> openProfile());

        findViewById(R.id.btnCriarNovoRacha).setOnClickListener(v -> {
            if (currentUserId != -1) {
                Intent intent = new Intent(this, CriarRachaActivity.class);
                intent.putExtra("USER_ID", currentUserId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Erro: Usuário não identificado", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnMeusRachas).setOnClickListener(v -> {
            Intent intent = new Intent(this, MeusRachasActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }

    // FIXED: Load data here so it refreshes every time you return to this screen
    @Override
    protected void onResume() {
        super.onResume();
        if (currentUserId != -1) {
            carregarAmigos();
            carregarResumo();
        }
    }

    private void openProfile() {
        Intent intent = new Intent(this, ResumoFinanceiroActivity.class);
        intent.putExtra("USER_NAME", currentUserName);
        intent.putExtra("USER_AVATAR", currentUserAvatar);
        intent.putExtra("USER_ID", currentUserId);
        startActivity(intent);
    }

    private void setupHeader(String name, int avatarId) {
        TextView tvNome = findViewById(R.id.tvNomeUsuarioHome);
        ImageView imgPerfil = findViewById(R.id.imgPerfilHome);

        if (name != null) {
            tvNome.setText(name.split(" ")[0]);
        }

        String drawableName = "avatar_" + avatarId;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (resId != 0) imgPerfil.setImageResource(resId);
    }

    private void carregarAmigos() {
        Call<List<Usuario>> call = RetrofitClient.getInstance().getApi().getAmigos(currentUserId);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AmigosAdapter adapter = new AmigosAdapter(HomeActivity.this, response.body());
                    rvAmigos.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                // Fail silently
            }
        });
    }

    private void carregarResumo() {
        Call<ResumoDTO> call = RetrofitClient.getInstance().getApi().getResumoFinanceiro(currentUserId);
        call.enqueue(new Callback<ResumoDTO>() {
            @Override
            public void onResponse(Call<ResumoDTO> call, Response<ResumoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResumoDTO resumo = response.body();

                    TextView tvReceber = findViewById(R.id.tvTotalReceber);
                    TextView tvPagar = findViewById(R.id.tvTotalPagar);

                    double valReceber = resumo.getTotalA_Receber() != null ? resumo.getTotalA_Receber() : 0.0;
                    double valPagar = resumo.getTotalA_Pagar() != null ? resumo.getTotalA_Pagar() : 0.0;

                    tvReceber.setText(String.format("R$ %.2f", valReceber));
                    tvPagar.setText(String.format("R$ %.2f", valPagar));
                }
            }

            @Override
            public void onFailure(Call<ResumoDTO> call, Throwable t) {
                // Fail silently
            }
        });
    }
}