package com.pth.tracuubienso.modules.register;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.dialog.DialogUtils;
import com.pth.tracuubienso.models.User;
import com.pth.tracuubienso.modules.home.HomeActivity;

public class RegisterActivity extends BaseActivity {

    EditText etEmail;
    EditText etPhone;
    EditText etPassword;
    EditText etRePassword;
    MaterialButton btnRegister;
    private String strEmail, strPhone, strPassword, strRePassword;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = ViewModelProviders.of(this).get(User.class);
        init();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valid()) {
                    DialogUtils.showProgress(RegisterActivity.this);
                    registerWithEmailAndPassword(strEmail, strPassword);
                }
            }
        });
    }

    private void init() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pwd);
        etRePassword = findViewById(R.id.et_repwd);
        btnRegister = findViewById(R.id.btn_register);
        etPhone = findViewById(R.id.et_phone);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constant.TBL_USER);
    }


    private boolean valid() {
        strEmail = etEmail.getText().toString();
        strPhone = etPhone.getText().toString();
        strPassword = etPassword.getText().toString();
        strRePassword = etRePassword.getText().toString();
        if ("".equals(strEmail)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_email));
            etEmail.findFocus();
            return true;
        }
        if ("".equals(strPhone)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_phone));
            etEmail.findFocus();
            return true;
        }
        if ("".equals(strPassword)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_password));
            etEmail.findFocus();
            return true;
        }
        if (strPassword.length() < 6) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_password_lenght));
            etEmail.findFocus();
            return true;
        }
        if (!strPassword.equals(strRePassword)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_password_again));
            etEmail.findFocus();
            return true;
        }


        return false;
    }

    @Override
    protected void showAlertDialog(Context context, String title, String message) {
        super.showAlertDialog(context, title, message);
    }

    private void registerWithEmailAndPassword(String strEmail, String strPassword) {
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnSuccessListener(authResult -> {

                    updateDataToServer();
                    DialogUtils.hideProgress();

                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();


                }).addOnFailureListener(e -> {
            showAlertDialog(this, getString(R.string.error), e.getMessage());
            DialogUtils.hideProgress();
        });
    }

    private void updateDataToServer() {
        user.setAdmin(false);
        user.setEmail(etEmail.getText().toString());
        user.setPhone(etPhone.getText().toString());
        user.setPass(etPassword.getText().toString());

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Cập nhật  thông tin thành công !", Toast.LENGTH_SHORT).show();
                    user.setUserCurrent(user);

                }).addOnFailureListener(e -> {

            Toast.makeText(RegisterActivity.this, "Cập nhật  thông tin thất bại !", Toast.LENGTH_SHORT).show();
        });
    }
}