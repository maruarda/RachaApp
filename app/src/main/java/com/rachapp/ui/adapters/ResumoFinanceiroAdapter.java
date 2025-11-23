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
import java.util.List;

public class ResumoFinanceiroAdapter extends RecyclerView.Adapter<ResumoFinanceiroAdapter.ViewHolder> {

    // Updated Model to hold IDs for payment
    public static class FinancialItem {
        public String personName;
        public String description; // "Churrasco - Picanha"
        public String valueStr;
        public double rawValue;    // For the dialog logic
        public int avatarId;
        public Long userId;        // Debtor/Creditor ID
        public Long rachaId;

        public FinancialItem(String personName, String description, String valueStr, double rawValue, int avatarId, Long userId, Long rachaId) {
            this.personName = personName;
            this.description = description;
            this.valueStr = valueStr;
            this.rawValue = rawValue;
            this.avatarId = avatarId;
            this.userId = userId;
            this.rachaId = rachaId;
        }
    }

    private final List<FinancialItem> items;
    private final OnItemClickListener listener;
    private Context context;

    // Interface for clicks
    public interface OnItemClickListener {
        void onItemClick(FinancialItem item);
    }

    public ResumoFinanceiroAdapter(List<FinancialItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_resumo_financeiro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FinancialItem item = items.get(position);

        holder.tvPerson.setText(item.personName);
        holder.tvDesc.setText(item.description);
        holder.tvValue.setText(item.valueStr);

        // Set Avatar
        int safeAvatarId = item.avatarId > 0 ? item.avatarId : 1;
        int resId = context.getResources().getIdentifier("avatar_" + safeAvatarId, "drawable", context.getPackageName());
        if (resId != 0) holder.imgAvatar.setImageResource(resId);

        // Click
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPerson, tvDesc, tvValue;
        ImageView imgAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPerson = itemView.findViewById(R.id.tvResumoPessoa);
            tvDesc = itemView.findViewById(R.id.tvResumoRacha);
            tvValue = itemView.findViewById(R.id.tvResumoValor);
            imgAvatar = itemView.findViewById(R.id.imgResumoAvatar);
        }
    }
}