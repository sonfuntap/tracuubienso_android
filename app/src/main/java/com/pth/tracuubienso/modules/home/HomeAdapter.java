package com.pth.tracuubienso.modules.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pth.tracuubienso.R;

import com.pth.tracuubienso.models.Province;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    List<Province> provinces;
    OnClickItemListener onClickItemListener;
    boolean isAdmin;

    public HomeAdapter(List<Province> provinces,boolean isAdmin, OnClickItemListener onClickItemListener) {
        this.provinces = provinces;
        this.isAdmin=isAdmin;
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
        holder.code.setText(province.getCodeProvinces().get(0));
        holder.linearLayout.setOnClickListener(v -> onClickItemListener.onClick(v));
        holder.btnEdit.setOnClickListener(v -> onClickItemListener.onEditClick(v));
        holder.btnDelete.setOnClickListener(v -> onClickItemListener.onDeleteClick(v));

        if(isAdmin){
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
        }
        else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return provinces.isEmpty() ? 0 : provinces.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView code;
        TextView name;
        LinearLayout linearLayout;
        MaterialButton  btnDelete, btnEdit;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            code = itemView.findViewById(R.id.tvCode);
            name = itemView.findViewById(R.id.tvName);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnEdit=itemView.findViewById(R.id.btnEdit);
        }
    }

    interface OnClickItemListener {
        void onClick(View view);
        void onDeleteClick(View view);
        void onEditClick(View view);

    }
}
