package com.pth.tracuubienso.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.pth.tracuubienso.R;

public class AddDistrictBottomDialog extends BottomSheetDialogFragment {
    EditText et_name_district;
    EditText et_code_district;
    MaterialButton btnOK;
    MaterialButton btnCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_district_dialog, container, false);
        et_code_district= view.findViewById(R.id.et_code_district);
        et_name_district= view.findViewById(R.id.et_name_district);
        btnOK= view.findViewById(R.id.btnOK);
        btnCancel= view.findViewById(R.id.btnCancel);

        btnOK.setOnClickListener(v -> addDistrict());
        btnCancel.setOnClickListener(v -> dismiss());
        return view;
    }

    private void addDistrict() {

    }
}
