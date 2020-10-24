package com.pth.tracuubienso.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.models.User;
import com.pth.tracuubienso.modules.add_province.AddProvinceActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends BaseActivity implements HomeAdapter.OnClickItemListener {

    RecyclerView recyclerView;
    TextInputEditText et_search;
    FloatingActionButton floatingActionButton;
    User currentUser;

    HomeAdapter homeAdapter;
    List<Province> provinces;
    List<Province> provincesSearch;

    DatabaseReference databaseReferenceProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }


    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rcv);
        et_search = findViewById(R.id.et_search);
        floatingActionButton = findViewById(R.id.btnFloatingButton);
        provinces = new ArrayList<>();
        provincesSearch = new ArrayList<>();
        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);

        if (currentUser.isAdmin()) {
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }


        floatingActionButton.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AddProvinceActivity.class)));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(HomeActivity.class.getSimpleName(), "afterTextChanged: " + s.toString());
                if (s.toString().equals("")) {
                    setDataAdapter(provinces);
                } else if (homeAdapter != null) {
                    if (!homeAdapter.searchProvinces(s.toString(), provinces).isEmpty()) {
                        provincesSearch = homeAdapter.searchProvinces(s.toString(), provinces);
                    } else if (!homeAdapter.searchProvincesByCode(s.toString(), provinces).isEmpty()) {
                        provincesSearch = homeAdapter.searchProvincesByCode(s.toString(), provinces);
                    }

                    setDataAdapterSearch(provincesSearch);
                } else Log.e(HomeActivity.class.getSimpleName(), "Home Adapter is null");

            }
        });
    }

    private void getUserCurrent() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.TBL_USER);
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        initView();
                        getDataProvinces(databaseReferenceProvince);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(HomeActivity.class.getSimpleName(), databaseError.getMessage());
                    }
                });
    }

    void getDataProvinces(DatabaseReference databaseReference) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    provinces.add(postSnapshot.getValue(Province.class));
                    setDataAdapter(provinces);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void setDataAdapter(List<Province> provinces) {
        if (homeAdapter == null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            homeAdapter = new HomeAdapter(provinces, currentUser.isAdmin(), this);
            recyclerView.setAdapter(homeAdapter);
        } else {
            homeAdapter = new HomeAdapter(provinces, currentUser.isAdmin(), this);
            recyclerView.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
        }
    }

    void setDataAdapterSearch(List<Province> provinces) {
        if (homeAdapter == null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            homeAdapter = new HomeAdapter(provinces, currentUser.isAdmin(), this, true);
            recyclerView.setAdapter(homeAdapter);
        } else {
            homeAdapter = new HomeAdapter(provinces, currentUser.isAdmin(), this, true);
            recyclerView.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
        }
    }

    public void deleteProvince(Province province) {
        databaseReferenceProvince.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Province province1 = postSnapshot.getValue(Province.class);
                    if (province.getNameProvince().toLowerCase().equals(Objects.requireNonNull(province1).getNameProvince().toLowerCase())) {
                        postSnapshot.getRef().removeValue();
                    }
                }
                if (!provinces.isEmpty()) {
                    Iterator<Province> iterator = provinces.iterator();
                    while (iterator.hasNext()) {
                        Province p = iterator.next();
                        if (p.getNameProvince().equals(province.getNameProvince())) {
                            iterator.remove();
                        }
                    }
                }

                if (!provincesSearch.isEmpty()) {
                    Iterator<Province> iterator2 = provincesSearch.iterator();
                    while (iterator2.hasNext()) {
                        Province p = iterator2.next();
                        if (p.getNameProvince().equals(province.getNameProvince())) {
                            iterator2.remove();

                        }
                    }
                }

                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showAlertDialog(HomeActivity.this, "Thông báo", error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeAdapter != null) {
            homeAdapter.notifyDataSetChanged();
        } else getUserCurrent();
    }

    @Override
    public void onClick(View view, Province province) {
        Toast.makeText(this, province.getNameProvince(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(View view, Province province) {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Thông báo")
                .setMessage("Bạn có chắc chắn muốn xóa " + province.getNameProvince())
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteProvince(province))
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onEditClick(View view, Province province) {

        Toast.makeText(this, "Edit" + province.getNameProvince(), Toast.LENGTH_SHORT).show();
    }
}