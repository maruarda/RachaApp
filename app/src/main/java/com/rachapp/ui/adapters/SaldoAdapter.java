package com.rachapp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rachapp.R;
import com.rachapp.data.model.BalanceDTO;
import java.util.List;

public class SaldoAdapter extends RecyclerView.Adapter<SaldoAdapter.ViewHolder> {

    private final List<BalanceDTO> balances;
    private final OnBalanceClickListener listener;
    private Context context;

    public interface OnBalanceClickListener {
        void onBalanceClick(BalanceDTO balance);
    }

    public SaldoAdapter(List<BalanceDTO> balances, OnBalanceClickListener listener) {
        this.balances = balances;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Reusing the generic item layout, or creating a specific one
        // Let's reuse item_resumo_financeiro.xml as it has the structure we need
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo_financeiro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BalanceDTO item = balances.get(position);

        holder.tvName.setText(item.getNome());

        // Avatar
        int avatarId = item.getAvatarId() != null ? item.getAvatarId() : 1;
        int resId = context.getResources().getIdentifier("avatar_" + avatarId, "drawable", context.getPackageName());
        if (resId != 0) holder.imgAvatar.setImageResource(resId);

        // Balance Logic
        double saldo = item.getSaldo();
        if (saldo >= 0) {
            holder.tvContext.setText("Recebe de volta");
            holder.tvContext.setTextColor(Color.parseColor("#4CAF50")); // Green
            holder.tvValue.setText(String.format("R$ %.2f", saldo));
            holder.tvValue.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvContext.setText("Deve pagar");
            holder.tvContext.setTextColor(Color.parseColor("#B71C1C")); // Red
            holder.tvValue.setText(String.format("R$ %.2f", Math.abs(saldo)));
            holder.tvValue.setTextColor(Color.parseColor("#B71C1C"));
        }

        holder.itemView.setOnClickListener(v -> listener.onBalanceClick(item));
    }

    @Override
    public int getItemCount() { return balances.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContext, tvValue;
        ImageView imgAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvResumoPessoa);
            tvContext = itemView.findViewById(R.id.tvResumoRacha);
            tvValue = itemView.findViewById(R.id.tvResumoValor);
            imgAvatar = itemView.findViewById(R.id.imgResumoAvatar);
        }
    }
}