package com.pth.tracuubienso.modules.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pth.tracuubienso.R;
import com.pth.tracuubienso.models.Province;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    List<Province> provinces;
    OnClickItemListener onClickItemListener;

    public HomeAdapter(List<Province> provinces, OnClickItemListener onClickItemListener) {
        this.provinces = provinces;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Province province = provinces.get(position);
        holder.name.setText(province.getNameProvince());
        holder.code.setText(province.getCodeProvince());
        holder.linearLayout.setOnClickListener(v -> onClickItemListener.onClick(v));
    }

    @Override
    public int getItemCount() {
        return provinces.isEmpty() ? 0 : provinces.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView code;
        TextView name;
        LinearLayout linearLayout;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            code = itemView.findViewById(R.id.tvCode);
            name = itemView.findViewById(R.id.tvName);
        }
    }

    interface OnClickItemListener {
        void onClick(View view);
    }
}
