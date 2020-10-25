package com.pth.tracuubienso.modules;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.pth.tracuubienso.R;
import com.pth.tracuubienso.models.Province;
import com.pth.tracuubienso.modules.home.HomeAdapter;
import com.pth.tracuubienso.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    List<Province> provinces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = findViewById(R.id.rcv);
        provinces = new ArrayList<>();
        provinces = PreferenceHelper.getIns().getProvinceList(this);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());


        Collections.reverse(provinces);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(provinces, false, new HomeAdapter.OnClickItemListener() {
            @Override
            public void onClick(View view, Province province) {

            }

            @Override
            public void onDeleteClick(View view, Province province) {

            }
        });
        recyclerView.setAdapter(homeAdapter);

    }
}