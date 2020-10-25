package com.pth.tracuubienso.modules.add_province;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pth.tracuubienso.R;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.modules.home.HomeAdapter;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DViewHolder> {
    List<Province.District> districts;
    boolean isAdmin;
    OnClickItemListener onClickItemListener;


    public DistrictAdapter(List<Province.District> districts, boolean isAdmin, OnClickItemListener onClickItemListener) {
        this.districts = districts;
        this.isAdmin = isAdmin;
        this.onClickItemListener = onClickItemListener;
    }


    @NonNull
    @Override
    public DViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_district, parent, false);

        return new DViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DViewHolder holder, int position) {
        if(districts.get(position)!=null){
            holder.tv_code_district.setText(districts.get(position).getCodeDistrict());
            holder.tv_name_district.setText(districts.get(position).getNameDistrict());

            if(isAdmin){
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.edit.setOnClickListener(v -> onClickItemListener.onEditClick(v,districts.get(position)));
                holder.delete.setOnClickListener(v -> onClickItemListener.onDeleteClick(v, districts.get(position)));
            }else holder.linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return districts.isEmpty() ? 0 : districts.size();
    }

    public static class DViewHolder extends RecyclerView.ViewHolder {
        TextView tv_code_district;
        TextView tv_name_district;
        LinearLayout linearLayout;
        TextView edit;
        TextView delete;

        public DViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_code_district = itemView.findViewById(R.id.tv_code_district);
            tv_name_district = itemView.findViewById(R.id.tv_name_district);
            linearLayout = itemView.findViewById(R.id.layout_district_edit);
            edit = itemView.findViewById(R.id.tv_edit_province);
            delete = itemView.findViewById(R.id.tv_delete_province);
        }
    }

    interface OnClickItemListener {

        void onDeleteClick(View view, Province.District district);

        void onEditClick(View view,  Province.District district);

    }
}
