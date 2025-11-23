package com.rachapp.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.BalanceDTO;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.Racha;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.ItemRachaAdapter;
import com.rachapp.ui.adapters.SaldoAdapter;
import com.rachapp.ui.dialogs.DetalhesConsumoDialog;
import com.rachapp.ui.dialogs.EditarItemDialog;
import com.rachapp.ui.dialogs.NovoItemDialog;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RachaDetailActivity extends AppCompatActivity {

    private long rachaId;
    private long currentUserId;
    private RecyclerView rvItens;
    private TextView tvTotal, tvStatus;
    private View btnAdd;
    private Racha currentRacha;

    private boolean showingBalances = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_racha_detail);

        rachaId = getIntent().getLongExtra("RACHA_ID", -1);
        currentUserId = getIntent().getLongExtra("USER_ID", -1);

        TextView tvTitle = findViewById(R.id.tvDetalheTitulo);
        tvStatus = findViewById(R.id.tvDetalheStatus);
        tvTotal = findViewById(R.id.tvTotalConta);
        rvItens = findViewById(R.id.rvDetalhesParticipantes);
        btnAdd = findViewById(R.id.btnAdicionarItem);

        rvItens.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(v -> {
            new NovoItemDialog(this, rachaId, currentUserId, this::carregarDetalhes).show();
        });

        ImageView btnOptions = findViewById(R.id.btnRachaOptions);
        btnOptions.setOnClickListener(this::showOptionsMenu);

        if (rachaId != -1) {
            carregarDetalhes();
        }
    }

    private void showOptionsMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        if (showingBalances) {
            popup.getMenu().add("Ver Itens");
        } else {
            popup.getMenu().add("Ver Saldos / Participantes");
        }

        popup.getMenu().add("Fechar Racha");
        popup.getMenu().add("Excluir Racha");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            if (title.equals("Ver Saldos / Participantes")) {
                showingBalances = true;
                carregarSaldos();
            } else if (title.equals("Ver Itens")) {
                showingBalances = false;
                carregarDetalhes();
            } else if (title.equals("Fechar Racha")) {
                fecharRacha();
            } else if (title.equals("Excluir Racha")) {
                confirmarExclusao();
            }
            return true;
        });
        popup.show();
    }

    private void carregarDetalhes() {
        showingBalances = false;
        Call<Racha> call = RetrofitClient.getInstance().getApi().getRachaDetalhes(rachaId);

        call.enqueue(new Callback<Racha>() {
            @Override
            public void onResponse(Call<Racha> call, Response<Racha> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentRacha = response.body();

                    TextView tvTitle = findViewById(R.id.tvDetalheTitulo);
                    tvTitle.setText(currentRacha.getNome());

                    if ("FECHADO".equals(currentRacha.getStatus())) {
                        tvStatus.setText("FECHADO");
                        tvStatus.setTextColor(0xFFFF0000);
                        btnAdd.setVisibility(View.GONE);
                    } else {
                        tvStatus.setText("ABERTO");
                        tvStatus.setTextColor(0xFF4CAF50);
                        btnAdd.setVisibility(View.VISIBLE);
                    }

                    if (currentRacha.getItens() != null) {
                        ItemRachaAdapter adapter = new ItemRachaAdapter(currentRacha.getItens(), (item, view) -> showItemOptions(item, view));
                        rvItens.setAdapter(adapter);
                        calculateTotal(currentRacha.getItens());
                    } else {
                        rvItens.setAdapter(new ItemRachaAdapter(new ArrayList<>(), null));
                        tvTotal.setText("R$ 0,00");
                    }
                }
            }
            @Override public void onFailure(Call<Racha> call, Throwable t) {
                Toast.makeText(RachaDetailActivity.this, "Erro ao carregar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarSaldos() {
        Call<List<BalanceDTO>> call = RetrofitClient.getInstance().getApi().getRachaBalances(rachaId);
        call.enqueue(new Callback<List<BalanceDTO>>() {
            @Override
            public void onResponse(Call<List<BalanceDTO>> call, Response<List<BalanceDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SaldoAdapter adapter = new SaldoAdapter(response.body(), balance -> {

                        boolean isReceiving = balance.getSaldo() < 0 && balance.getUserId() != currentUserId;
                        double amount = Math.abs(balance.getSaldo());

                        // FIXED: Use RachaDetailActivity.this::carregarSaldos to fix scope error
                        new DetalhesConsumoDialog(
                                RachaDetailActivity.this,
                                balance.getNome(),
                                balance.getAvatarId() != null ? balance.getAvatarId() : 1,
                                balance.getItensConsumidos(),
                                amount,
                                currentUserId,
                                balance.getUserId(),
                                rachaId,
                                isReceiving,
                                RachaDetailActivity.this::carregarSaldos // Explicit scope fixes "cannot find symbol"
                        ).show();
                    });
                    rvItens.setAdapter(adapter);

                    btnAdd.setVisibility(View.GONE);
                    tvStatus.setText("SALDOS");
                }
            }
            @Override public void onFailure(Call<List<BalanceDTO>> call, Throwable t) {}
        });
    }

    private void showItemOptions(ItemRacha item, View view) {
        if (currentRacha != null && "FECHADO".equals(currentRacha.getStatus())) {
            Toast.makeText(this, "Racha fechado.", Toast.LENGTH_SHORT).show();
            return;
        }
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add("Editar");
        popup.getMenu().add("Excluir Item");
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Editar")) {
                new EditarItemDialog(this, item, currentUserId, this::carregarDetalhes).show();
            } else if (menuItem.getTitle().equals("Excluir Item")) {
                deleteItem(item.getIdItemRacha());
            }
            return true;
        });
        popup.show();
    }

    private void deleteItem(Long itemId) {
        Call<Void> call = RetrofitClient.getInstance().getApi().deleteItem(itemId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RachaDetailActivity.this, "Item removido", Toast.LENGTH_SHORT).show();
                    carregarDetalhes();
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private void fecharRacha() {
        Call<Racha> call = RetrofitClient.getInstance().getApi().fecharRacha(rachaId);
        call.enqueue(new Callback<Racha>() {
            @Override public void onResponse(Call<Racha> call, Response<Racha> response) { if(response.isSuccessful()) { Toast.makeText(RachaDetailActivity.this, "Racha Fechado!", Toast.LENGTH_SHORT).show(); carregarDetalhes(); } }
            @Override public void onFailure(Call<Racha> call, Throwable t) {}
        });
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this).setTitle("Excluir Racha?").setMessage("Isso apagará tudo.").setPositiveButton("Excluir", (d, w) -> deleteRacha()).setNegativeButton("Cancelar", null).show();
    }

    private void deleteRacha() {
        Call<Void> call = RetrofitClient.getInstance().getApi().deleteRacha(rachaId);
        call.enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) { if(response.isSuccessful()) { Toast.makeText(RachaDetailActivity.this, "Excluído", Toast.LENGTH_SHORT).show(); finish(); } }
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private void calculateTotal(List<ItemRacha> itens) {
        double total = 0.0;
        for (ItemRacha item : itens) {
            if (item.getPreco() != null) {
                total += item.getPreco();
            }
        }
        tvTotal.setText(String.format("R$ %.2f", total));
    }
}