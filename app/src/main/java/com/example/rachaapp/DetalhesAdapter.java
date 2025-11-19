package com.example.rachaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DetalhesAdapter extends RecyclerView.Adapter<DetalhesAdapter.ViewHolder> {

    private List<ItemDivida> itens;

    public DetalhesAdapter(List<ItemDivida> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalhe_divida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDivida item = itens.get(position);
        holder.tvNome.setText(item.getNome());
        holder.tvValor.setText(String.format("R$ %.2f", item.getValorParcela()));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeItemDetalhe);
            tvValor = itemView.findViewById(R.id.tvValorItemDetalhe);
        }
    }
}