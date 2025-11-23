package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.Devedor;
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

public class EditarItemDialog extends Dialog {

    private final ItemRacha item;
    private final long currentUserId; // Needed to fetch friends list
    private final Runnable onSuccessCallback;
    private ParticipantesAdapter adapter;
    private final List<Usuario> allParticipants = new ArrayList<>();

    public EditarItemDialog(@NonNull Context context, ItemRacha item, long currentUserId, Runnable onSuccessCallback) {
        super(context);
        this.item = item;
        this.currentUserId = currentUserId;
        this.onSuccessCallback = onSuccessCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Reusing the layout from 'NovoItemDialog' because it has the list
        setContentView(R.layout.dialog_novo_item);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvTitle = findViewById(R.id.tvDialogTitle);
        tvTitle.setText("Editar Item");

        EditText etNome = findViewById(R.id.etNomeItem);
        EditText etValor = findViewById(R.id.etValorItem);
        Button btnConfirm = findViewById(R.id.btnConfirmarDivisao);

        etNome.setText(item.getNome());
        etValor.setText(String.valueOf(item.getPreco()));
        btnConfirm.setText("Salvar Alterações");

        // Show list elements (they were hidden in previous version)
        findViewById(R.id.lblDividir).setVisibility(View.VISIBLE);
        findViewById(R.id.rvParticipantesDialog).setVisibility(View.VISIBLE);
        findViewById(R.id.containerCalculo).setVisibility(View.VISIBLE);

        // Hide Payer selection for Edit (Simplification: don't change payer on edit)
        findViewById(R.id.lblQuemPagou).setVisibility(View.GONE);
        findViewById(R.id.spinnerPayer).setVisibility(View.GONE);

        setupRecyclerView();
        setupLiveCalculation(etValor);

        btnConfirm.setOnClickListener(v -> salvarAlteracoes(etNome, etValor));
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvParticipantesDialog);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. Fetch Friends + Me (Participants Pool)
        fetchParticipants(rv);
    }

    private void fetchParticipants(RecyclerView rv) {
        // Fetch Me
        RetrofitClient.getInstance().getApi().getUsuarioById(currentUserId).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario me = response.body();
                    me.setNome(me.getNome() + " (Eu)");
                    allParticipants.add(me);
                }
                // Then Fetch Friends
                fetchFriendsAndDebtors(rv);
            }
            @Override public void onFailure(Call<Usuario> call, Throwable t) { fetchFriendsAndDebtors(rv); }
        });
    }

    private void fetchFriendsAndDebtors(RecyclerView rv) {
        RetrofitClient.getInstance().getApi().getAmigos(currentUserId).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allParticipants.addAll(response.body());
                }
                // Finally, fetch who is ALREADY paying for this item to check boxes
                fetchExistingDebtors(rv);
            }
            @Override public void onFailure(Call<List<Usuario>> call, Throwable t) { fetchExistingDebtors(rv); }
        });
    }

    private void fetchExistingDebtors(RecyclerView rv) {
        RetrofitClient.getInstance().getApi().getDevedoresByItem(item.getIdItemRacha()).enqueue(new Callback<List<Devedor>>() {
            @Override
            public void onResponse(Call<List<Devedor>> call, Response<List<Devedor>> response) {
                List<Long> preSelectedIds = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    for (Devedor d : response.body()) {
                        preSelectedIds.add(d.getIdUsuario());
                    }
                }

                // Initialize Adapter with Pre-Selection logic
                adapter = new ParticipantesAdapter(getContext(), allParticipants, () -> calcularDivisao());

                // Update selection
                adapter.setSelection(preSelectedIds);

                rv.setAdapter(adapter);
                calcularDivisao(); // Update math based on selection
            }
            @Override public void onFailure(Call<List<Devedor>> call, Throwable t) {}
        });
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
                double perPerson = total / count;
                tvResultado.setText(String.format("R$ %.2f", perPerson));
            } else {
                tvResultado.setText("Selecione alguém");
            }
        } catch (NumberFormatException e) { tvResultado.setText("R$ 0,00"); }
    }

    private void setupLiveCalculation(EditText etValor) {
        etValor.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calcularDivisao(); }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void salvarAlteracoes(EditText etNome, EditText etValor) {
        String nome = etNome.getText().toString().trim();
        String valorStr = etValor.getText().toString().trim().replace(",", ".");

        if (nome.isEmpty() || valorStr.isEmpty()) return;

        double valor;
        try { valor = Double.parseDouble(valorStr); } catch (NumberFormatException e) { return; }

        List<Long> selectedIds = adapter != null ? adapter.getSelectedIds() : new ArrayList<>();

        if (selectedIds.isEmpty()) {
            Toast.makeText(getContext(), "Selecione quem vai dividir a conta!", Toast.LENGTH_SHORT).show();
            return;
        }

        // FIXED: Added null for payerId (4th argument) to match the 5-arg constructor
        // Args: nome, preco, rachaId, payerId, participantesIds
        ItemCreationDTO updateDto = new ItemCreationDTO(nome, valor, null, null, selectedIds);

        Call<ItemRacha> call = RetrofitClient.getInstance().getApi().updateItem(item.getIdItemRacha(), updateDto);

        call.enqueue(new Callback<ItemRacha>() {
            @Override
            public void onResponse(Call<ItemRacha> call, Response<ItemRacha> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Item atualizado!", Toast.LENGTH_SHORT).show();
                    if (onSuccessCallback != null) onSuccessCallback.run();
                    dismiss();
                }
            }
            @Override public void onFailure(Call<ItemRacha> call, Throwable t) {}
        });
    }
}