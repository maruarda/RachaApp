package com.example.rachaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rachaapp.model.Participante;
import com.example.rachaapp.R;

import java.util.List;

public class ParticipantesAdapter extends RecyclerView.Adapter<ParticipantesAdapter.ViewHolder> {

    private List<Participante> lista;
    private OnItemClickListener listener;

    // Interface para lidar com cliques nos itens
    public interface OnItemClickListener {
        void onItemClick(Participante p);
    }

    // Construtor
    public ParticipantesAdapter(List<Participante> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item (certifique-se que o arquivo XML existe)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_participante_saldo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participante p = lista.get(position);

        holder.tvNome.setText(p.getNome());
        holder.tvValor.setText(String.format("R$ %.2f", p.getTotalPagar()));

        // Lógica da Estrela (Pagante)
        if (p.isPagante()) {
            if (holder.imgPagoIcon != null) {
                holder.imgPagoIcon.setVisibility(View.VISIBLE);
            }
            // Muda a cor do valor para verde se for pagante (opcional, mas recomendado)
            // holder.tvValor.setTextColor(0xFF4CAF50);
        } else {
            if (holder.imgPagoIcon != null) {
                holder.imgPagoIcon.setVisibility(View.GONE);
            }
            // Restaura a cor padrão se necessário
        }

        // Configura o clique no item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Classe interna ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvValor;
        ImageView imgPagoIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Certifique-se que estes IDs existem no seu XML 'item_participante_saldo.xml'
            tvNome = itemView.findViewById(R.id.tvNomeAmigo);
            tvValor = itemView.findViewById(R.id.tvValorPagar);
            imgPagoIcon = itemView.findViewById(R.id.imgPagoIcon);
        }
    }
}