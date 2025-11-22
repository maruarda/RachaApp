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
import com.rachapp.data.model.Usuario;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchViewHolder> {

    private final List<Usuario> listaUsuarios;
    private final Context context;
    private final OnAddClickListener addClickListener;

    public interface OnAddClickListener {
        void onAddClick(Usuario userToAdd);
    }

    public SearchUserAdapter(Context context, List<Usuario> listaUsuarios, OnAddClickListener listener) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
        this.addClickListener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Usuario user = listaUsuarios.get(position);
        holder.tvName.setText(user.getNome());
        holder.tvEmail.setText(user.getEmail());

        int avatarId = user.getAvatarId() > 0 ? user.getAvatarId() : 1;
        String drawableName = "avatar_" + avatarId;
        int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        if (resId != 0) holder.imgAvatar.setImageResource(resId);

        holder.btnAdd.setOnClickListener(v -> addClickListener.onAddClick(user));
    }

    @Override
    public int getItemCount() { return listaUsuarios.size(); }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, btnAdd;
        TextView tvName, tvEmail;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgSearchAvatar);
            btnAdd = itemView.findViewById(R.id.btnAddUser);
            tvName = itemView.findViewById(R.id.tvSearchName);
            tvEmail = itemView.findViewById(R.id.tvSearchEmail);
        }
    }
}