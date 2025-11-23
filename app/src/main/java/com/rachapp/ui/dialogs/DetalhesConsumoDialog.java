package com.rachapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.R;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.ui.adapters.DetalheDividaAdapter;

import java.util.List;

public class DetalhesConsumoDialog extends Dialog {

    private final String nomePessoa;
    private final int avatarId; // NEW: Store avatar ID
    private final List<ItemRacha> itens;
    private final double total;

    // Updated Constructor
    public DetalhesConsumoDialog(@NonNull Context context, String nomePessoa, int avatarId, List<ItemRacha> itens, double total) {
        super(context);
        this.nomePessoa = nomePessoa;
        this.avatarId = avatarId;
        this.itens = itens;
        this.total = total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_detalhe_divida_dialog);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvNome = findViewById(R.id.tvNomeParticipanteDetalhe);
        TextView tvTotal = findViewById(R.id.tvTotalDetalhe);
        ImageView imgAvatar = findViewById(R.id.imgAvatarDetalhe); // Find the ImageView
        RecyclerView rv = findViewById(R.id.rvItensDetalhe);

        // Set Text
        tvNome.setText(nomePessoa);
        tvTotal.setText(String.format("R$ %.2f", total));

        // NEW: Set Avatar Image
        int safeAvatarId = avatarId > 0 ? avatarId : 1;
        String drawableName = "avatar_" + safeAvatarId;
        int resId = getContext().getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName());

        if (resId != 0) {
            imgAvatar.setImageResource(resId);
        } else {
            imgAvatar.setImageResource(R.drawable.avatar_1);
        }

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new DetalheDividaAdapter(itens));

        findViewById(R.id.btnFecharDetalhe).setOnClickListener(v -> dismiss());
    }
}