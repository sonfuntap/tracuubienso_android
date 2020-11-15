package com.pth.tracuubienso.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.pth.tracuubienso.dialog.DialogUtils;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.models.User;
import com.pth.tracuubienso.modules.HistoryActivity;
import com.pth.tracuubienso.modules.add_province.AddProvinceActivity;
import com.pth.tracuubienso.modules.login.LoginActivity;
import com.pth.tracuubienso.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends BaseActivity implements HomeAdapter.OnClickItemListener {

    RecyclerView recyclerView;
    TextInputEditText et_search;
    FloatingActionButton floatingActionButton;
    User currentUser;
    ImageButton btn_menu;

    HomeAdapter homeAdapter;
    List<Province> provinces;
    List<Province> provincesSearch;
    List<Province> provincesHistory;

    DatabaseReference databaseReferenceProvince;
    LinearLayout layoutSearch;
    boolean isShowSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hideKeyboard(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rcv);
        et_search = findViewById(R.id.et_search);
        btn_menu = findViewById(R.id.btn_menu);
        layoutSearch = findViewById(R.id.layout);


        layoutSearch.setVisibility(View.GONE);

        floatingActionButton = findViewById(R.id.btnFloatingButton);
        provinces = new ArrayList<>();
        provincesSearch = new ArrayList<>();
        provincesHistory = new ArrayList<>();
        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);


        if(getIntent()!=null){
            if(getIntent().hasExtra("User")){
                currentUser= (User) getIntent().getSerializableExtra("User");
            }
        }


        if(currentUser!=null){
            if (currentUser.isAdmin()) {
                floatingActionButton.setVisibility(View.VISIBLE);
            } else {
                floatingActionButton.setVisibility(View.GONE);
            }
        }


        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddProvinceActivity.class);
            intent.putExtra(Constant.TYPE_INTENT, Constant.TYPE_INTENT_ADD);
            intent.putExtra(Constant.IS_ADMIN, currentUser.isAdmin());
            startActivity(intent);
        });

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                isShowSearch = !isShowSearch;
                if (isShowSearch) {
                    layoutSearch.setVisibility(View.VISIBLE);
                } else layoutSearch.setVisibility(View.GONE);
                return true;
            case R.id.action_history:
                showHistory();
                return true;
            case R.id.action_log_out:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showHistory() {
        startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    private void getUserCurrent() {
        DialogUtils.showProgress(this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.TBL_USER);
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        initView();
                        getDataProvinces(databaseReferenceProvince);
                        DialogUtils.hideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(HomeActivity.class.getSimpleName(), databaseError.getMessage());
                        DialogUtils.hideProgress();
                    }
                });
    }

    void getDataProvinces(DatabaseReference databaseReference) {
        provinces.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    provinces.add(postSnapshot.getValue(Province.class));
                    setDataAdapter(provinces);
                }
                DialogUtils.hideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DialogUtils.hideProgress();
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
            getDataProvinces(databaseReferenceProvince);
            homeAdapter.notifyDataSetChanged();
        } else getUserCurrent();
    }

    @Override
    public void onClick(View view, Province province) {
        provincesHistory.add(province);
        PreferenceHelper.getIns().setProvinceList(this, "prosvinces", provincesHistory);
        Intent intent = new Intent(HomeActivity.this, AddProvinceActivity.class);
        intent.putExtra(Constant.TYPE_INTENT, Constant.TYPE_INTENT_EDIT);
        intent.putExtra(Constant.PROVINCE_OBJ, province);
        intent.putExtra(Constant.IS_ADMIN, currentUser.isAdmin());
        startActivity(intent);

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
}