package com.pth.tracuubienso.modules.add_province;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddProvinceActivity extends BaseActivity implements IAddProvince, DistrictAdapter.OnClickItemListener {
    MaterialButton btn_add_district;
    RecyclerView rcv_district;
    EditText etName;
    EditText etCode;
    ImageButton btnBack;
    ImageButton btnDone;

    MaterialButton btnUpdateProVince;
    MaterialButton btnAddBXS;
    MaterialButton btnRemoveBXS;
    TextView tvTitle;
    TextView tvDistrictList;

    TextInputEditText etInfoProvince;
    TextView tvInfoProvince;

    List<Province.District> districts;

    DatabaseReference databaseReferenceProvince;

    Province province;

    Province provinceUpdate;

    DistrictAdapter districtAdapter;

    String typeView = "";

    boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        hideKeyboard(this);
        initView();
        getPassData();
    }

    void getPassData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(Constant.IS_ADMIN)) {
                isAdmin = getIntent().getBooleanExtra(Constant.IS_ADMIN, false);
            }

            if (getIntent().hasExtra(Constant.TYPE_INTENT)) {
                typeView = getIntent().getStringExtra(Constant.TYPE_INTENT);
            }
            if (getIntent().hasExtra(Constant.PROVINCE_OBJ)) {
                province = (Province) getIntent().getSerializableExtra(Constant.PROVINCE_OBJ);

            }

            districts = Objects.requireNonNull(province).getDistricts();
            setDataView(province, typeView);
        }
    }

    String getCode(List<String> strings) {
        StringBuilder code = new StringBuilder();

        for (String s : strings) {
            code.append(s).append(", ");
        }

        return code.substring(0, code.length() - 2);
    }

    void setDataView(Province province, String typeView) {
        if (isAdmin) {
            btn_add_district.setVisibility(View.VISIBLE);
            btnUpdateProVince.setVisibility(View.VISIBLE);
            btnUpdateProVince.setEnabled(true);
            btnDone.setVisibility(View.GONE);


            if (typeView.equals(Constant.TYPE_INTENT_EDIT)) {
                etName.setEnabled(true);
                etCode.setEnabled(true);
                etInfoProvince.setEnabled(true);
                rcv_district.setEnabled(true);
                btnUpdateProVince.setVisibility(View.GONE);
                btnAddBXS.setVisibility(View.VISIBLE);
                btnRemoveBXS.setVisibility(View.VISIBLE);
                tvTitle.setText(province.getNameProvince());
                etName.setText(province.getNameProvince());
                etCode.setText(getCode(province.getCodeProvinces()));
                initRcv(districts);
            } else if (typeView.equals(Constant.TYPE_INTENT_ADD)) {
                etName.setEnabled(true);
                etCode.setEnabled(true);
                etInfoProvince.setEnabled(true);
                rcv_district.setEnabled(true);
                etName.setText("");
                etCode.setText("");
                btnUpdateProVince.setVisibility(View.VISIBLE);
                btnAddBXS.setVisibility(View.GONE);
                btnRemoveBXS.setVisibility(View.GONE);
                initRcv(new ArrayList<>());
            }

        } else {
            btn_add_district.setVisibility(View.GONE);
            btnUpdateProVince.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            btnAddBXS.setVisibility(View.GONE);
            btnRemoveBXS.setVisibility(View.GONE);

            etName.setEnabled(false);
            etCode.setEnabled(false);
            etInfoProvince.setEnabled(false);

            tvTitle.setText(province.getNameProvince());
            rcv_district.setEnabled(false);
            etName.setText(province.getNameProvince());
            etCode.setText(getCode(province.getCodeProvinces()));
            initRcv(districts);
        }


    }

    @Override
    protected void initView() {
        btn_add_district = findViewById(R.id.btn_add_district);
        rcv_district = findViewById(R.id.rcv_district);
        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        btnBack = findViewById(R.id.btnBack);
        btnDone = findViewById(R.id.btnDone);
        btnUpdateProVince = findViewById(R.id.updateProvince);
        tvTitle = findViewById(R.id.tvTitle);
        tvDistrictList = findViewById(R.id.tvListDistrict);
        btnAddBXS = findViewById(R.id.add);
        btnRemoveBXS = findViewById(R.id.remove);
        tvInfoProvince= findViewById(R.id.tvInfoProvince);
        etInfoProvince= findViewById(R.id.etInfoProvince);


        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
        province = new Province();
        districts = new ArrayList<>();

        btnDone.setOnClickListener(v -> {

        });

        btnUpdateProVince.setOnClickListener(v -> {

            if (!etCode.getText().toString().equals("")) {
                province.addCode(etCode.getText().toString());
            }
            if (!etName.getText().toString().equals("")) {
                province.setNameProvince(etName.getText().toString());
            }

            if(!etInfoProvince.getText().toString().equals("")){
                province.setNameProvince(etInfoProvince.getText().toString());
            }

            if (province != null) {
                addProvince(province);
            } else
                Toast.makeText(AddProvinceActivity.this, "Kiểm tra lại thông tin tỉnh !", Toast.LENGTH_SHORT).show();

        });

        btnAddBXS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddBxsDilalog(province).show(getSupportFragmentManager(), AddBxsDilalog.class.getSimpleName());
            }
        });

        btnRemoveBXS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoveBxsDilalog(province, new IAddProvince() {
                    @Override
                    public void updateDistrictList(Province province) {

                    }

                    @Override
                    public void updateProvince(Province province) {
                        getDataProvince(databaseReferenceProvince);
                    }
                }).show(getSupportFragmentManager(), RemoveBxsDilalog.class.getSimpleName());
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btn_add_district.setOnClickListener(v -> {
            AddDistrictBottomDialog addDistrictBottomDialog = new AddDistrictBottomDialog(province, this);
            addDistrictBottomDialog.show(getSupportFragmentManager(), AddDistrictBottomDialog.class.getSimpleName());
        });
    }


    private void addProvince(Province province) {
        databaseReferenceProvince.child(province.getNameProvince())
                .setValue(province)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddProvinceActivity.this, "Cập  nhật thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(AddProvinceActivity.this,
                    "Lỗi: " + e.getMessage() + "\n Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        });
    }

    void initRcv(List<Province.District> districts) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_district.setLayoutManager(layoutManager);
        districtAdapter = new DistrictAdapter(districts, isAdmin, this);
        rcv_district.setAdapter(districtAdapter);
    }

    public void deleteDistrict(Province.District district) {
        databaseReferenceProvince.child(province.getNameProvince())
                .child("districts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Province.District d = postSnapshot.getValue(Province.District.class);
                            if (district.getNameDistrict().toLowerCase().equals(Objects.requireNonNull(d).getNameDistrict().toLowerCase())) {
                                postSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showAlertDialog(AddProvinceActivity.this, "Thông báo", error.getMessage());
                    }
                });
    }

    @Override
    public void updateDistrictList(Province provinceResponse) {
        getDataDistrict(provinceResponse, databaseReferenceProvince);
    }

    @Override
    public void updateProvince(Province province) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(AddProvinceActivity.class.getSimpleName(), "on Resume");

        getDataDistrict(province, databaseReferenceProvince);
    }

    @Override
    public void onDeleteClick(View view, Province.District district) {


        new AlertDialog.Builder(AddProvinceActivity.this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc chắn muốn xóa " + district.getNameDistrict())
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteDistrict(district))
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onEditClick(View view, Province.District district) {
        editDistrict(district);
    }

    private void editDistrict(Province.District district) {
        AddDistrictBottomDialog addDistrictBottomDialog = new AddDistrictBottomDialog(province, this);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DISTRICT_OBJ, district);
        bundle.putInt("index", districts.indexOf(district));
        addDistrictBottomDialog.setArguments(bundle);
        addDistrictBottomDialog.show(getSupportFragmentManager(), AddDistrictBottomDialog.class.getSimpleName());
    }


    void getDataProvince(DatabaseReference databaseReference) {

        databaseReference.child(province.getNameProvince())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        provinceUpdate = new Province();
                        provinceUpdate = snapshot.getValue(Province.class);
                        etCode.setText(getCode(provinceUpdate.getCodeProvinces()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    void getDataDistrict(Province province, DatabaseReference databaseReference) {
        if (province.getNameProvince() != null) {
            databaseReference.child(province.getNameProvince())
                    .child("districts")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            districts.clear();
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                districts.add(postSnapshot.getValue(Province.District.class));
                            }
                            initRcv(districts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }
}
