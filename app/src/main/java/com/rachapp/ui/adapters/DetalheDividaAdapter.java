package com.rachapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rachapp.R;
import com.rachapp.data.model.ItemRacha;
import com.rachapp.data.model.Usuario;
import java.util.List;

public class DetalheDividaAdapter extends RecyclerView.Adapter<DetalheDividaAdapter.ViewHolder> {

    private final List<ItemRacha> items;
    private Context context;

    public DetalheDividaAdapter(List<ItemRacha> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_detalhe_divida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemRacha item = items.get(position);

        holder.tvNome.setText(item.getNome());
        holder.tvValor.setText(String.format("R$ %.2f", item.getPreco()));

        Usuario payer = item.getPayer();
        if (payer != null) {
            holder.tvPayer.setText("Pago por: " + payer.getNome());

            // NEW: Set Avatar Logic
            int avatarId = payer.getAvatarId() > 0 ? payer.getAvatarId() : 1;
            String drawableName = "avatar_" + avatarId;
            int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

            if (resId != 0) {
                holder.imgAvatar.setImageResource(resId);
            } else {
                holder.imgAvatar.setImageResource(R.drawable.avatar_1);
            }
        } else {
            holder.tvPayer.setText("Pago por: Organizador");
            holder.imgAvatar.setImageResource(R.drawable.avatar_1);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvValor, tvPayer;
        ImageView imgAvatar; // Added Image

        public ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeItemDetalhe);
            tvValor = itemView.findViewById(R.id.tvValorItemDetalhe);
            tvPayer = itemView.findViewById(R.id.tvQuemPagou);
            imgAvatar = itemView.findViewById(R.id.imgPayerAvatar); // Bound to new ID
        }
    }
}