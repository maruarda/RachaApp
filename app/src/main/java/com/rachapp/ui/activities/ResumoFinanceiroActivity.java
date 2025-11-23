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
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.ResumoDTO;
import com.rachapp.data.model.ResumoItemDTO;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.ResumoFinanceiroAdapter;
import com.rachapp.ui.dialogs.DetalhesConsumoDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumoFinanceiroActivity extends AppCompatActivity {

    private long currentUserId;
    private String currentUserName;
    private int currentUserAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_financeiro);

        currentUserName = getIntent().getStringExtra("USER_NAME");
        currentUserAvatar = getIntent().getIntExtra("USER_AVATAR", 1);
        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        setupHeader(currentUserName, currentUserAvatar);

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
            tvTotalReceber.setText("R$ 0,00");
            tvTotalPagar.setText("R$ 0,00");
            return;
        }

        List<ResumoFinanceiroAdapter.FinancialItem> itemsReceber = convertDtoToAdapter(data.getListaReceber());
        ResumoFinanceiroAdapter adapterReceber = new ResumoFinanceiroAdapter(itemsReceber, item -> {
            openDetailDialog(item, true);
        });
        rvReceber.setAdapter(adapterReceber);

        List<ResumoFinanceiroAdapter.FinancialItem> itemsPagar = convertDtoToAdapter(data.getListaPagar());
        ResumoFinanceiroAdapter adapterPagar = new ResumoFinanceiroAdapter(itemsPagar, item -> {
            openDetailDialog(item, false);
        });
        rvPagar.setAdapter(adapterPagar);

        tvTotalReceber.setText(String.format("R$ %.2f", data.getTotalA_Receber()));
        tvTotalPagar.setText(String.format("R$ %.2f", data.getTotalA_Pagar()));
    }

    private void openDetailDialog(ResumoFinanceiroAdapter.FinancialItem item, boolean isReceiving) {
        Usuario payerUser = new Usuario();
        if (isReceiving) {
            payerUser.setNome(currentUserName + " (Eu)");
            payerUser.setAvatarId(currentUserAvatar);
        } else {
            payerUser.setNome(item.personName);
            payerUser.setAvatarId(item.avatarId);
        }

        ItemRacha displayItem = new ItemRacha(item.description, item.rawValue, item.rachaId, payerUser);
        List<ItemRacha> displayList = Collections.singletonList(displayItem);

        Long myId = currentUserId;
        Long otherId = item.userId;

        new DetalhesConsumoDialog(
                this,
                item.personName,
                item.avatarId,
                displayList,
                item.rawValue,
                myId,
                otherId,
                item.rachaId,
                isReceiving,
                this::carregarDadosFinanceiros // PASSING CALLBACK TO REFRESH
        ).show();
    }

    private List<ResumoFinanceiroAdapter.FinancialItem> convertDtoToAdapter(List<ResumoItemDTO> dtos) {
        List<ResumoFinanceiroAdapter.FinancialItem> items = new ArrayList<>();
        if (dtos != null) {
            for (ResumoItemDTO dto : dtos) {
                items.add(new ResumoFinanceiroAdapter.FinancialItem(
                        dto.getNomePessoa(),
                        "Ref: " + dto.getNomeRacha(),
                        String.format("R$ %.2f", dto.getValor()),
                        dto.getValor(),
                        dto.getAvatarId() != null ? dto.getAvatarId() : 1,
                        dto.getUserId(),
                        dto.getRachaId()
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