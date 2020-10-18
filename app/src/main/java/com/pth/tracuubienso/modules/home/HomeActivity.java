package com.pth.tracuubienso.modules.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.base.BaseInterface;
import com.pth.tracuubienso.models.User;

public class HomeActivity extends BaseActivity implements BaseInterface {

    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUserCurrent(this);
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
}