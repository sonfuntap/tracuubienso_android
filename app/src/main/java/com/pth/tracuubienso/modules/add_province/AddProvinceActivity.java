package com.pth.tracuubienso.modules.add_province;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;

public class AddProvinceActivity extends BaseActivity {
    MaterialButton btn_add_district;
    RecyclerView rcv_district;
    EditText etName;
    EditText etCode;
    ImageButton btnBack;
    ImageButton btnDone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add);
    }

    @Override
    protected void initView() {
        btn_add_district= findViewById(R.id.btn_add_district);
        rcv_district= findViewById(R.id.rcv_district);
        etName= findViewById(R.id.etName);
        etCode= findViewById(R.id.etCode);
        btnBack= findViewById(R.id.btnBack);
        btnDone= findViewById(R.id.btnDone);
    }
}
