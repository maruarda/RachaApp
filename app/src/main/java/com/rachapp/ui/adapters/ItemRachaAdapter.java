package com.rachapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rachapp.R;
import com.rachapp.data.model.ItemRacha;
import java.util.List;

public class ItemRachaAdapter extends RecyclerView.Adapter<ItemRachaAdapter.ViewHolder> {

    private final List<ItemRacha> items;
    private final OnItemClickListener listener; // New Listener

    public interface OnItemClickListener {
        void onItemClick(ItemRacha item, View view); // Pass View for PopupMenu anchor
    }

    public ItemRachaAdapter(List<ItemRacha> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemRacha item = items.get(position);
        holder.text1.setText(item.getNome());
        holder.text2.setText(String.format("R$ %.2f", item.getPreco()));

        // Set Long Click Listener
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemClick(item, v);
            return true; // Consume event
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}