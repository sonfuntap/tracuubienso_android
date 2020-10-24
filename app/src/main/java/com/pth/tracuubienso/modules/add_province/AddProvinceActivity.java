package com.pth.tracuubienso.modules.add_province;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;

import java.util.List;

public class AddProvinceActivity extends BaseActivity implements IAddProvince {
    MaterialButton btn_add_district;
    RecyclerView rcv_district;
    EditText etName;
    EditText etCode;
    ImageButton btnBack;
    ImageButton btnDone;

    DatabaseReference databaseReferenceProvince;

    Province province;

    DistrictAdapter districtAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
    }

    @Override
    protected void initView() {
        btn_add_district = findViewById(R.id.btn_add_district);
        rcv_district = findViewById(R.id.rcv_district);
        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        btnBack = findViewById(R.id.btnBack);
        btnDone = findViewById(R.id.btnDone);

        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
        province = new Province();

        btnDone.setOnClickListener(v -> {

            if (!etCode.getText().toString().isEmpty()) {
                province.addCode(etCode.getText().toString());
            }
            if (!etName.getText().toString().isEmpty()) {
                province.setNameProvince(etName.getText().toString());
            }

            if (province != null) {
                addProvince(province);
            } else
                Toast.makeText(AddProvinceActivity.this, "Kiểm tra lại thông tin tỉnh !", Toast.LENGTH_SHORT).show();

        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btn_add_district.setOnClickListener(v -> {
            AddDistrictBottomDialog addDistrictBottomDialog = new AddDistrictBottomDialog(province, this::initRcv);
            addDistrictBottomDialog.show(getSupportFragmentManager(), AddDistrictBottomDialog.class.getSimpleName());
        });
    }


    private void addProvince(Province province) {
        databaseReferenceProvince.child(province.getNameProvince())
                .setValue(province)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddProvinceActivity.this, "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddProvinceActivity.this, HomeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
                }).addOnFailureListener(e -> {
            Toast.makeText(AddProvinceActivity.this,
                    "Lỗi: " + e.getMessage() + "\n Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        });
    }

    void initRcv(List<Province.District> districts) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_district.setLayoutManager(layoutManager);
        districtAdapter = new DistrictAdapter(districts);
        rcv_district.setAdapter(districtAdapter);
        districtAdapter.notifyDataSetChanged();

    }

    void getDataDistrict(Province province) {
        databaseReferenceProvince.child(province.getNameProvince())
                .child("districts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Province.District> districts= snapshot.getValue(Province.District);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void passData(List<Province.District> districts) {
        initRcv(districts);
    }
}
