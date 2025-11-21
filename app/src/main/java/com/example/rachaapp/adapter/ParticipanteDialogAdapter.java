package com.example.rachaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rachaapp.model.Participante;
import com.example.rachaapp.R;

import java.util.List;

public class ParticipanteDialogAdapter extends RecyclerView.Adapter<ParticipanteDialogAdapter.ViewHolder> {

    private List<Participante> lista;
    private OnSelectionChangeListener listener;

    // Interface para avisar a Activity que houve mudança na seleção (checkbox)
    public interface OnSelectionChangeListener {
        void onSelectionChanged();
    }

    public ParticipanteDialogAdapter(List<Participante> lista, OnSelectionChangeListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout de item com checkbox (item_dialog_participante.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog_participante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participante p = lista.get(position);
        holder.tvNome.setText(p.getNome());

        // Remove o listener antes de setar o estado para evitar loops infinitos
        holder.cb.setOnCheckedChangeListener(null);
        holder.cb.setChecked(p.isSelecionado());

        // Adiciona o listener de clique no checkbox
        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            p.setSelecionado(isChecked);
            // Avisa a Activity para recalcular o valor da divisão
            if (listener != null) {
                listener.onSelectionChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Método auxiliar para contar quantos estão marcados
    public int getContagemSelecionados() {
        int count = 0;
        for (Participante p : lista) {
            if (p.isSelecionado()) count++;
        }
        return count;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;
        TextView tvNome;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cbParticipante);
            tvNome = itemView.findViewById(R.id.tvNomeDialog);
            imgAvatar = itemView.findViewById(R.id.imgAvatarDetalhe);
        }
    }
}