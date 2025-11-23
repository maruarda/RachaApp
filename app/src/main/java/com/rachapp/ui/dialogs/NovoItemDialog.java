package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.ItemCreationDTO;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.Usuario;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.ParticipantesAdapter;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovoItemDialog extends Dialog {

    private final long rachaId;
    private final long currentUserId;
    private final Runnable onItemAddedCallback;
    private ParticipantesAdapter adapter;
    private final List<Usuario> allParticipants = new ArrayList<>();

    public NovoItemDialog(@NonNull Context context, long rachaId, long currentUserId, Runnable onItemAddedCallback) {
        super(context);
        this.rachaId = rachaId;
        this.currentUserId = currentUserId;
        this.onItemAddedCallback = onItemAddedCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_novo_item);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        setupRecyclerView();
        setupLiveCalculation();

        findViewById(R.id.btnConfirmarDivisao).setOnClickListener(v -> adicionarItem());
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvParticipantesDialog);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        RetrofitClient.getInstance().getApi().getUsuarioById(currentUserId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario me = response.body();
                    me.setNome(me.getNome() + " (Eu)");
                    allParticipants.add(0, me);
                    fetchFriendsAndRefresh(rv);
                }
            }
            @Override public void onFailure(Call<Usuario> call, Throwable t) { fetchFriendsAndRefresh(rv); }
        });
    }

    private void fetchFriendsAndRefresh(RecyclerView rv) {
        RetrofitClient.getInstance().getApi().getAmigos(currentUserId).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allParticipants.addAll(response.body());
                }
                updateUI(rv);
            }
            @Override public void onFailure(Call<List<Usuario>> call, Throwable t) { updateUI(rv); }
        });
    }

    private void updateUI(RecyclerView rv) {
        // 1. Setup List (Who splits)
        adapter = new ParticipantesAdapter(getContext(), allParticipants, () -> calcularDivisao());
        rv.setAdapter(adapter);

        // 2. Setup Spinner (Who paid)
        Spinner spinner = findViewById(R.id.spinnerPayer);
        List<String> names = new ArrayList<>();
        for (Usuario u : allParticipants) names.add(u.getNome());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(spinnerAdapter);
    }

    private void calcularDivisao() {
        if (adapter == null) return;
        EditText etValor = findViewById(R.id.etValorItem);
        TextView tvResultado = findViewById(R.id.tvValorPorPessoa);
        String valorStr = etValor.getText().toString().replace(",", ".");
        if (valorStr.isEmpty()) { tvResultado.setText("R$ 0,00"); return; }

        try {
            double total = Double.parseDouble(valorStr);
            int count = adapter.getSelectedIds().size();
            if (count > 0) {
                tvResultado.setText(String.format("R$ %.2f", total / count));
            } else {
                tvResultado.setText("Selecione alguém");
            }
        } catch (NumberFormatException e) { tvResultado.setText("R$ 0,00"); }
    }

    private void setupLiveCalculation() {
        EditText etValor = findViewById(R.id.etValorItem);
        etValor.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calcularDivisao(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void adicionarItem() {
        EditText etNome = findViewById(R.id.etNomeItem);
        EditText etValor = findViewById(R.id.etValorItem);
        Spinner spinner = findViewById(R.id.spinnerPayer);

        String nome = etNome.getText().toString().trim();
        String valorStr = etValor.getText().toString().replace(",", ".");

        if (nome.isEmpty() || valorStr.isEmpty()) return;

        double valor;
        try { valor = Double.parseDouble(valorStr); } catch (NumberFormatException e) { return; }

        List<Long> selectedIds = adapter != null ? adapter.getSelectedIds() : new ArrayList<>();
        if (selectedIds.isEmpty()) {
            Toast.makeText(getContext(), "Selecione quem vai dividir!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine Payer ID from Spinner Selection
        int payerPosition = spinner.getSelectedItemPosition();
        Long payerId = allParticipants.get(payerPosition).getIdUsuario();

        ItemCreationDTO newItem = new ItemCreationDTO(nome, valor, rachaId, payerId, selectedIds);

        Call<ItemRacha> call = RetrofitClient.getInstance().getApi().adicionarItem(newItem);
        call.enqueue(new Callback<ItemRacha>() {
            @Override
            public void onResponse(Call<ItemRacha> call, Response<ItemRacha> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item adicionado!", Toast.LENGTH_SHORT).show();
                    if (onItemAddedCallback != null) onItemAddedCallback.run();
                    dismiss();
                }
            }
            @Override public void onFailure(Call<ItemRacha> call, Throwable t) {}
        });
    }
}