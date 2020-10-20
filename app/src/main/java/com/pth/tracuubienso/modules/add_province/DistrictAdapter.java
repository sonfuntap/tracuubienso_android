package com.pth.tracuubienso.modules.add_province;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pth.tracuubienso.R;
import com.pth.tracuubienso.models.Province;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DViewHolder> {
    List<Province.District> districts;

    public DistrictAdapter(List<Province.District> districts) {
        this.districts = districts;
    }

    @NonNull
    @Override
    public DViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_district, parent, false);

        return new DViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DViewHolder holder, int position) {
        holder.tv_code_district.setText(districts.get(position).getCodeDistrict());
        holder.tv_name_district.setText(districts.get(position).getNameDistrict());
    }

    @Override
    public int getItemCount() {
        return districts.isEmpty() ? 0 : districts.size();
    }

    public static class DViewHolder extends RecyclerView.ViewHolder {
        TextView tv_code_district;
        TextView tv_name_district;

        public DViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_code_district = itemView.findViewById(R.id.tv_code_district);
            tv_name_district = itemView.findViewById(R.id.tv_name_district);
        }
    }

}
