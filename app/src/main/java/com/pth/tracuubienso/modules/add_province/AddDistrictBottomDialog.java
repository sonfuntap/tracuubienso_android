package com.pth.tracuubienso.modules.add_province;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;

import java.util.ArrayList;
import java.util.List;

public class AddDistrictBottomDialog extends BottomSheetDialogFragment {
    EditText et_name_district;
    EditText et_code_district;
    MaterialButton btnOK;
    MaterialButton btnCancel;
    Province province;
    Province.District district;
    List<Province.District> districts;
    IAddProvince iAddProvince;
    int index = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AddDistrictBottomDialog(Province province, IAddProvince iAddProvince) {
        this.province = province;
        this.iAddProvince = iAddProvince;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_district_dialog, container, false);
        et_code_district = view.findViewById(R.id.et_code_district);
        et_name_district = view.findViewById(R.id.et_name_district);
        btnOK = view.findViewById(R.id.btnOK);
        btnCancel = view.findViewById(R.id.btnCancel);
        district = new Province.District();
        districts = new ArrayList<>();

        btnOK.setOnClickListener(v -> {
            String code = et_code_district.getText().toString();
            String name = et_name_district.getText().toString();
            district.setCodeDistrict(code);
            district.setNameDistrict(name);
            if (!code.equals("") && !name.equals("")) {
                province.addDistrict(district);
                if (getArguments() != null) {
                    updateProvince(district);
                } else {
                    addProvince(province);
                }

            } else
                Toast.makeText(getContext(), "Tên huyện và code không được để trống !", Toast.LENGTH_SHORT).show();

        });
        btnCancel.setOnClickListener(v -> dismiss());

        if (getArguments() != null) {
            index = getArguments().getInt("index");
            district = (Province.District) getArguments().getSerializable(Constant.DISTRICT_OBJ);
            if (district != null) {
                et_code_district.setText(district.getCodeDistrict());
                et_name_district.setText(district.getNameDistrict());
            }

        }

        return view;
    }

    private void addProvince(Province province) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
        databaseReference.child(province.getNameProvince())
                .setValue(province)
                .addOnSuccessListener(aVoid -> {
                    iAddProvince.updateDistrictList(province);
                    Toast.makeText(getContext(), "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
                    dismiss();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),
                    "Lỗi: " + e.getMessage() + "\n Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateProvince(Province.District district) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
        databaseReference.child(province.getNameProvince())
                .child("districts")
                .child(String.valueOf(index))
                .setValue(district)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
                    dismiss();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),
                    "Lỗi: " + e.getMessage() + "\n Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        });
    }

}
