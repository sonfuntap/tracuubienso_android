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

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    List<Province> provinces;
    OnClickItemListener onClickItemListener;
    boolean isAdmin;

    List<Province> provinceSearchList;


    public HomeAdapter(List<Province> provinces, boolean isAdmin, OnClickItemListener onClickItemListener) {
        this.provinces = provinces;
        this.isAdmin = isAdmin;
        this.onClickItemListener = onClickItemListener;
    }

    public HomeAdapter(List<Province> provincesSearchList, boolean isAdmin, OnClickItemListener onClickItemListener, boolean search) {
        this.provinceSearchList = provincesSearchList;
        this.isAdmin = isAdmin;
        this.onClickItemListener = onClickItemListener;
    }


    public List<Province> searchProvinces(String name, List<Province> listToSearch) {
        List<Province> provinceSearch = new ArrayList<>();
        for (Province province : listToSearch) {
            if (province.getNameProvince().toLowerCase().contains(name.toLowerCase())) {
                provinceSearch.add(province);
            }
        }
        return provinceSearch;
    }

    public List<Province> searchProvincesByCode(String code, List<Province> listToSearch) {
        List<Province> provinceArrayList = new ArrayList<>();
        for (Province province : listToSearch) {
            for (String s : province.getCodeProvinces()) {
                if (s.toLowerCase().contains(code.toLowerCase())) {
                    provinceArrayList.add(province);
                }
            }
        }
        return provinceArrayList;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new HomeViewHolder(view);
    }

    String getCode(List<String> strings) {
        StringBuilder code = new StringBuilder();

        for (String s : strings) {
            code.append(s).append(", ");
        }

        return code.substring(0, code.length() - 2);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Province province = new Province();
        if (provinces != null) {
            province = provinces.get(position);
        } else if (provinceSearchList != null) {
            province = provinceSearchList.get(position);
        }


        holder.name.setText(province.getNameProvince());
        holder.code.setText(getCode(province.getCodeProvinces()));
        Province finalProvince = province;
        holder.linearLayout.setOnClickListener(v -> onClickItemListener.onClick(v, finalProvince));
        holder.btnEdit.setOnClickListener(v -> onClickItemListener.onEditClick(v, finalProvince));
        holder.btnDelete.setOnClickListener(v -> onClickItemListener.onDeleteClick(v, finalProvince));

        if (isAdmin) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (provinces != null) {
            return provinces.isEmpty() ? 0 : provinces.size();
        } else if (provinceSearchList != null) {
            return provinceSearchList.isEmpty() ? 0 : provinceSearchList.size();
        }
        return 0;

    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView code;
        TextView name;
        LinearLayout linearLayout;
        MaterialButton btnDelete, btnEdit;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            code = itemView.findViewById(R.id.tvCode);
            name = itemView.findViewById(R.id.tvName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    interface OnClickItemListener {
        void onClick(View view, Province province);

        void onDeleteClick(View view, Province province);

        void onEditClick(View view, Province province);

    }
}
