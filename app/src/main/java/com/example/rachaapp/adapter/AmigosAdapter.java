package com.example.rachaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rachaapp.R;
import com.example.rachaapp.model.Usuario;
import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder> {

    private final List<Usuario> listaAmigos;
    private final Context context;

    public AmigosAdapter(Context context, List<Usuario> listaAmigos) {
        this.context = context;
        this.listaAmigos = listaAmigos;
    }

    @NonNull
    @Override
    public AmigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_amigo_horizontal, parent, false);
        return new AmigoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigoViewHolder holder, int position) {
        Usuario amigo = listaAmigos.get(position);

        // Set Name (First name only)
        String[] nomeParts = amigo.getNome().split(" ");
        if (nomeParts.length > 0) {
            holder.tvNome.setText(nomeParts[0]);
        } else {
            holder.tvNome.setText(amigo.getNome());
        }

        // Set Avatar
        int avatarId = amigo.getAvatarId();
        if (avatarId < 1) avatarId = 1;

        String drawableName = "avatar_" + avatarId;
        int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

        if (resId != 0) {
            holder.imgAvatar.setImageResource(resId);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.avatar_1);
        }
    }

    @Override
    public int getItemCount() {
        return listaAmigos.size();
    }

    static class AmigoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNome;

        public AmigoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAmigoAvatar);
            tvNome = itemView.findViewById(R.id.tvAmigoNome);
        }
    }
}