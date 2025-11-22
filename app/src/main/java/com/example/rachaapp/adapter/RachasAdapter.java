package com.example.rachaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rachapp.app.R;
import com.rachapp.app.model.Racha;

import java.util.List;

public class RachasAdapter extends RecyclerView.Adapter<RachasAdapter.ViewHolder> {

    private List<Racha> rachas;

    public RachasAdapter(List<Racha> rachas) {
        this.rachas = rachas;
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
        holder.nome.setText(rachas.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return rachas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNomeRacha);
        }
    }
}
