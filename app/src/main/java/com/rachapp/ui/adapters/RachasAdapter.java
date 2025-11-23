package com.rachapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rachapp.R;
import com.rachapp.data.model.Racha;
import java.util.List;

public class RachasAdapter extends RecyclerView.Adapter<RachasAdapter.ViewHolder> {

    private final List<Racha> rachas;
    private final OnRachaClickListener listener;

    public interface OnRachaClickListener {
        void onRachaClick(Long rachaId);
    }

    public RachasAdapter(List<Racha> rachas, OnRachaClickListener listener) {
        this.rachas = rachas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_racha_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Racha racha = rachas.get(position);
        holder.tvNome.setText(racha.getNome());
        holder.tvLocal.setText(racha.getLocalNome() != null ? racha.getLocalNome() : "Sem local");

        if ("FECHADO".equals(racha.getStatus())) {
            holder.tvStatus.setText("FECHADO");
            holder.tvStatus.setTextColor(0xFFB71C1C); // Red
            holder.tvStatus.setBackgroundColor(0xFFFFEBEE); // Light Red
        } else {
            holder.tvStatus.setText("ABERTO");
            holder.tvStatus.setTextColor(0xFF15492D); // Green
            holder.tvStatus.setBackgroundColor(0xFFE8F5E9); // Light Green
        }

        holder.itemView.setOnClickListener(v -> listener.onRachaClick(racha.getIdRacha()));
    }

    @Override
    public int getItemCount() { return rachas.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvLocal, tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvRachaNome);
            tvLocal = itemView.findViewById(R.id.tvRachaLocal);
            tvStatus = itemView.findViewById(R.id.tvRachaStatus);
        }
    }
}