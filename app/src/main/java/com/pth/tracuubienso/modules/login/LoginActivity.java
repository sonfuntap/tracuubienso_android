package com.pth.tracuubienso.modules.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.base.BaseActivity;
import com.pth.tracuubienso.dialog.DialogUtils;
import com.pth.tracuubienso.modules.home.HomeActivity;
import com.pth.tracuubienso.modules.register.RegisterActivity;
import com.pth.tracuubienso.utils.AccountHelper;

public class LoginActivity extends BaseActivity {
    EditText et_email;
    EditText et_pwd;
    MaterialButton btnLoginWithEmail;
    MaterialButton btnRegister;
    MaterialButton btnForgotPwd;

    private String strEmail, strPassword;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


//        if (AccountHelper.getIns().getInfoAccount(this) != null) {
//
//            Log.d("LoginFragment", "account exist");
//            HashMap<String, String> hashMap = AccountHelper.getIns().getInfoAccount(this);
//            et_email.setText(hashMap.get("email"));
//            et_pwd.setText(hashMap.get("password"));
//            if (!valid()) {
//                DialogUtils.showProgress(LoginActivity.this);
//                loginWithEmail(et_email.getText().toString(), et_pwd.getText().toString());
//                AccountHelper.getIns().saveInfoAccount(LoginActivity.this, et_email.getText().toString(), et_pwd.getText().toString());
//            }
//
//
//        } else Log.d("LoginFragment", "account is null");

        btnLoginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valid()) {
                    DialogUtils.showProgress(LoginActivity.this);
                    loginWithEmail(et_email.getText().toString(), et_pwd.getText().toString());
                    AccountHelper.getIns().saveInfoAccount(LoginActivity.this, et_email.getText().toString(), et_pwd.getText().toString());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    void init() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        btnLoginWithEmail = findViewById(R.id.btnLoginWithEmail);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgotPwd = findViewById(R.id.btnForgotPwd);

        mAuth = FirebaseAuth.getInstance();

    }

    private boolean valid() {
        strEmail = et_email.getText().toString();
        strPassword = et_pwd.getText().toString();
        if ("".equals(strEmail)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_email));
            et_email.findFocus();
            return true;
        }
        if ("".equals(strPassword)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_password));
            et_pwd.findFocus();
            return true;
        }

        return false;
    }

    public void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    DialogUtils.hideProgress();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener((Exception e) -> {
                    DialogUtils.hideProgress();
                    String errorCode = ((FirebaseAuthException) e).getErrorCode();
                    showAlertDialog(this, getString(R.string.error), errorCode);
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void showAlertDialog(Context context, String title, String message) {
        super.showAlertDialog(context, title, message);
    }


}