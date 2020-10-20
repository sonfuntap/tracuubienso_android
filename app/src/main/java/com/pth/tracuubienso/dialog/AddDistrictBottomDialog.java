package com.pth.tracuubienso.dialog;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.modules.add_province.DistrictAdapter;
import com.pth.tracuubienso.modules.add_province.IAddProvince;

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

    public AddDistrictBottomDialog(Province province, IAddProvince iAddProvince) {
        this.province = province;
        this.iAddProvince= iAddProvince;
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

            if (!et_code_district.getText().toString().isEmpty()) {
                district.setCodeDistrict(et_code_district.getText().toString());
            }
            if (!et_name_district.getText().toString().isEmpty()) {
                district.setNameDistrict(et_name_district.getText().toString());
            }

            province.addDistrict(district);

            addProvince(province);
        });
        btnCancel.setOnClickListener(v -> dismiss());
        return view;
    }

    private void addProvince(Province province) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
        databaseReference.child(province.getNameProvince())
                .setValue(province)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
                    dismiss();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),
                    "Lỗi: " + e.getMessage() + "\n Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        });

        getDataDistrict(province, databaseReference);
    }

    void getDataDistrict(Province province, DatabaseReference databaseReference) {
        databaseReference.child(province.getNameProvince())
                .child("districts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            districts.add(postSnapshot.getValue(Province.District.class));
                        }
                        iAddProvince.passData(districts);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
