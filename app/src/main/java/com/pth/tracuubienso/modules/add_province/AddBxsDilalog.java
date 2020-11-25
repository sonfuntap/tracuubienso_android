package com.pth.tracuubienso.modules.add_province;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.modules.add_province.AddProvinceActivity;

import java.util.Objects;

public class AddBxsDilalog extends DialogFragment {
    EditText etBXS;
    MaterialButton btnAdd;
    MaterialButton btnCancel;
    DatabaseReference databaseReferenceProvince;
    Province province;
    IAddProvince iAddProvince;


    public AddBxsDilalog(Province province, IAddProvince iAddProvince) {
        this.province = province;
        this.iAddProvince= iAddProvince;
    }


    @Override
    public void onStart() {
        super.onStart();
        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.8);
        setupDialog(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dialog, container, false);
        etBXS = view.findViewById(R.id.etBXS);
        btnAdd = view.findViewById(R.id.btnOK);
        btnCancel = view.findViewById(R.id.btnCancel);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!"".equals(etBXS.getText().toString())) {
                    addProvince(etBXS.getText().toString(), province);
                } else
                    Toast.makeText(getContext(), "Vui lòng nhập biển số xe!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    private void addProvince(String code, Province provinceOld) {
        provinceOld.addCode(code);
        databaseReferenceProvince.child(provinceOld.getNameProvince())
                .setValue(provinceOld)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
                    iAddProvince.updateProvince(provinceOld);
                    dismiss();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),
                    "Lỗi: " + e.getMessage() + "\n Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        });
    }

    protected void setupDialog(int width, int height) {
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        //set dialog size
        getDialog().getWindow().setLayout(width,
                height);
    }
}
