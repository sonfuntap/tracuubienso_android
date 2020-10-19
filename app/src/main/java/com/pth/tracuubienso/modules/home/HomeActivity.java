package com.pth.tracuubienso.modules.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.base.BaseInterface;
import com.pth.tracuubienso.models.User;

public class HomeActivity extends BaseActivity implements BaseInterface, HomeAdapter.OnClickItemListener {

    RecyclerView recyclerView;
    TextInputEditText  et_search;
    FloatingActionButton floatingActionButton;
    User currentUser;

    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUserCurrent(this);

    }


    @Override
    protected void initView() {
        recyclerView= findViewById(R.id.rcv);
        et_search= findViewById(R.id.et_search);
        floatingActionButton= findViewById(R.id.btnFloatingButton);
    }

    @Override
    protected void getUserCurrent(BaseInterface baseInterface) {
        super.getUserCurrent(baseInterface);
    }

    @Override
    public void getCurrentUserSuccess(User user) {
        currentUser= user;
        Toast.makeText(this, currentUser.getEmail(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCurrentUserFailed(String message) {
        Toast.makeText(this, "error user",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDeleteClick(View view) {

    }

    @Override
    public void onEditClick(View view) {

    }
}