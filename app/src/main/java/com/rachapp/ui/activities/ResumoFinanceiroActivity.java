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
import com.rachapp.data.model.ResumoDTO;
import com.rachapp.data.model.ResumoItemDTO;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.ResumoFinanceiroAdapter;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumoFinanceiroActivity extends AppCompatActivity {

    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_financeiro);

        String userName = getIntent().getStringExtra("USER_NAME");
        int avatarId = getIntent().getIntExtra("USER_AVATAR", 1);
        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        setupHeader(userName, avatarId);

        // Initialize with empty data while loading
        setupLists(null);

        if (currentUserId != -1) {
            carregarDadosFinanceiros();
        }

        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());
        findViewById(R.id.btnEditarPerfil).setOnClickListener(v -> {
            Toast.makeText(this, "Editar Perfil em breve!", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupHeader(String name, int avatarId) {
        TextView tvNome = findViewById(R.id.tvNomeUsuarioLogado);
        ImageView imgPerfil = findViewById(R.id.imgPerfilResumo);

        if (name != null) tvNome.setText(name);

        String drawableName = "avatar_" + avatarId;
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (resId != 0) imgPerfil.setImageResource(resId);
    }

    private void carregarDadosFinanceiros() {
        Call<ResumoDTO> call = RetrofitClient.getInstance().getApi().getResumoFinanceiro(currentUserId);

        call.enqueue(new Callback<ResumoDTO>() {
            @Override
            public void onResponse(Call<ResumoDTO> call, Response<ResumoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupLists(response.body());
                } else {
                    Toast.makeText(ResumoFinanceiroActivity.this, "Erro ao carregar resumo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResumoDTO> call, Throwable t) {
                Toast.makeText(ResumoFinanceiroActivity.this, "Falha de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLists(ResumoDTO data) {
        RecyclerView rvReceber = findViewById(R.id.rvReceber);
        RecyclerView rvPagar = findViewById(R.id.rvPagar);
        TextView tvTotalReceber = findViewById(R.id.tvTotalReceber);
        TextView tvTotalPagar = findViewById(R.id.tvTotalPagar);

        rvReceber.setLayoutManager(new LinearLayoutManager(this));
        rvPagar.setLayoutManager(new LinearLayoutManager(this));

        if (data == null) {
            // Show empty state
            tvTotalReceber.setText("R$ 0,00");
            tvTotalPagar.setText("R$ 0,00");
            return;
        }

        // Convert Backend DTOs to Adapter Items
        List<ResumoFinanceiroAdapter.FinancialItem> itemsReceber = convertDtoToAdapter(data.getListaReceber());
        List<ResumoFinanceiroAdapter.FinancialItem> itemsPagar = convertDtoToAdapter(data.getListaPagar());

        rvReceber.setAdapter(new ResumoFinanceiroAdapter(itemsReceber));
        rvPagar.setAdapter(new ResumoFinanceiroAdapter(itemsPagar));

        // Format currency nicely
        tvTotalReceber.setText(String.format("R$ %.2f", data.getTotalA_Receber()));
        tvTotalPagar.setText(String.format("R$ %.2f", data.getTotalA_Pagar()));
    }

    private List<ResumoFinanceiroAdapter.FinancialItem> convertDtoToAdapter(List<ResumoItemDTO> dtos) {
        List<ResumoFinanceiroAdapter.FinancialItem> items = new ArrayList<>();
        if (dtos != null) {
            for (ResumoItemDTO dto : dtos) {
                items.add(new ResumoFinanceiroAdapter.FinancialItem(
                        dto.getNomePessoa(),
                        "Ref: " + dto.getNomeRacha(),
                        String.format("R$ %.2f", dto.getValor()),
                        dto.getAvatarId() != null ? dto.getAvatarId() : 1
                ));
            }
        }
        return items;
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}