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

    public static class FinancialItem {
        public String personName;
        public String rachaContext;
        public String value;
        public int avatarId;

        public FinancialItem(String personName, String rachaContext, String value, int avatarId) {
            this.personName = personName;
            this.rachaContext = rachaContext;
            this.value = value;
            this.avatarId = avatarId;
        }
    }

    private final List<FinancialItem> items;
    private Context context;

    public ResumoFinanceiroAdapter(List<FinancialItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_resumo_financeiro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FinancialItem item = items.get(position);

        holder.tvPerson.setText(item.personName);
        holder.tvContext.setText(item.rachaContext);
        holder.tvValue.setText(item.value);

        String drawableName = "avatar_" + item.avatarId;
        int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

        if (resId != 0) {
            holder.imgAvatar.setImageResource(resId);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.avatar_1);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPerson, tvContext, tvValue;
        ImageView imgAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPerson = itemView.findViewById(R.id.tvResumoPessoa);
            tvContext = itemView.findViewById(R.id.tvResumoRacha);
            tvValue = itemView.findViewById(R.id.tvResumoValor);
            imgAvatar = itemView.findViewById(R.id.imgResumoAvatar);
        }
    }
}