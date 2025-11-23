package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.PagamentoDTO;
import com.rachapp.data.network.RetrofitClient;
import com.rachapp.ui.adapters.DetalheDividaAdapter;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesConsumoDialog extends Dialog {

    private final String nomePessoa;
    private final int avatarId;
    private final List<ItemRacha> itens;
    private final double total;
    private final Long myId;
    private final Long otherId;
    private final Long rachaId;
    private final boolean isReceiving;

    // NEW: Callback to refresh parent activity
    private final Runnable onPaymentSuccess;

    public DetalhesConsumoDialog(@NonNull Context context, String nomePessoa, int avatarId,
                                 List<ItemRacha> itens, double total,
                                 Long myId, Long otherId, Long rachaId, boolean isReceiving,
                                 Runnable onPaymentSuccess) { // Added parameter
        super(context);
        this.nomePessoa = nomePessoa;
        this.avatarId = avatarId;
        this.itens = itens;
        this.total = total;
        this.myId = myId;
        this.otherId = otherId;
        this.rachaId = rachaId;
        this.isReceiving = isReceiving;
        this.onPaymentSuccess = onPaymentSuccess;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detalhes_consumo);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        // UI Binding
        TextView tvNome = findViewById(R.id.tvNomeParticipanteDetalhe);
        TextView tvTotal = findViewById(R.id.tvTotalDetalhe);
        ImageView imgAvatar = findViewById(R.id.imgAvatarDetalhe);
        RecyclerView rv = findViewById(R.id.rvItensDetalhe);
        Button btnConfirm = findViewById(R.id.btnConfirmarRecebimento);

        tvNome.setText(nomePessoa);
        tvTotal.setText(String.format("R$ %.2f", total));

        int safeAvatarId = avatarId > 0 ? avatarId : 1;
        String drawableName = "avatar_" + safeAvatarId;
        int resId = getContext().getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName());
        if (resId != 0) imgAvatar.setImageResource(resId);
        else imgAvatar.setImageResource(R.drawable.avatar_1);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new DetalheDividaAdapter(itens));

        if (isReceiving && total > 0) {
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setOnClickListener(v -> confirmarPagamento());
        } else {
            btnConfirm.setVisibility(View.GONE);
        }

        findViewById(R.id.btnFecharDetalhe).setOnClickListener(v -> dismiss());
    }

    private void confirmarPagamento() {
        PagamentoDTO pagamento = new PagamentoDTO(total, rachaId, otherId, myId);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().confirmarPagamento(pagamento);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Pagamento confirmado!", Toast.LENGTH_SHORT).show();

                    // TRIGGER REFRESH
                    if (onPaymentSuccess != null) onPaymentSuccess.run();

                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Erro ao confirmar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}