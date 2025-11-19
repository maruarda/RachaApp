package com.example.rachaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResumoAdapter extends RecyclerView.Adapter<ResumoAdapter.ViewHolder> {

    private List<ResumoItem> lista;

    public ResumoAdapter(List<ResumoItem> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resumo_financeiro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumoItem item = lista.get(position);
        holder.tvNome.setText(item.getNomePessoa());
        holder.tvRacha.setText("Referente a: " + item.getNomeRacha());
        holder.tvValor.setText(String.format("R$ %.2f", item.getValor()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvRacha, tvValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvResumoPessoa);
            tvRacha = itemView.findViewById(R.id.tvResumoRacha);
            tvValor = itemView.findViewById(R.id.tvResumoValor);
        }
    }
}