package com.rachapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rachapp.R;
import com.rachapp.data.model.Usuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParticipantesAdapter extends RecyclerView.Adapter<ParticipantesAdapter.ViewHolder> {

    private final List<Usuario> amigos;
    private final Set<Long> selectedIds = new HashSet<>();
    private final Context context;
    private final Runnable onSelectionChanged;

    public ParticipantesAdapter(Context context, List<Usuario> amigos, Runnable onSelectionChanged) {
        this.context = context;
        this.amigos = amigos;
        this.onSelectionChanged = onSelectionChanged;
    }

    // NEW: Method to pre-select IDs
    public void setSelection(List<Long> idsToSelect) {
        selectedIds.clear();
        selectedIds.addAll(idsToSelect);
        notifyDataSetChanged(); // Refresh UI
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_participante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario amigo = amigos.get(position);
        holder.tvNome.setText(amigo.getNome());

        int avatarId = amigo.getAvatarId() > 0 ? amigo.getAvatarId() : 1;
        String drawableName = "avatar_" + avatarId;
        int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        if (resId != 0) holder.imgAvatar.setImageResource(resId);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedIds.contains(amigo.getIdUsuario()));

        View.OnClickListener toggleListener = v -> {
            if (selectedIds.contains(amigo.getIdUsuario())) {
                selectedIds.remove(amigo.getIdUsuario());
                holder.checkBox.setChecked(false);
            } else {
                selectedIds.add(amigo.getIdUsuario());
                holder.checkBox.setChecked(true);
            }
            if (onSelectionChanged != null) onSelectionChanged.run();
        };

        holder.itemView.setOnClickListener(toggleListener);
        holder.checkBox.setOnClickListener(toggleListener);
    }

    @Override
    public int getItemCount() { return amigos.size(); }

    public List<Long> getSelectedIds() {
        return new ArrayList<>(selectedIds);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNome;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgPartAvatar);
            tvNome = itemView.findViewById(R.id.tvPartNome);
            checkBox = itemView.findViewById(R.id.cbPartSelect);
        }
    }
}