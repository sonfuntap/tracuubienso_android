package com.pth.tracuubienso.modules.register;

import androidx.appcompat.app.AppCompatActivity;

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
import com.pth.tracuubienso.modules.HomeActivity;
import com.pth.tracuubienso.modules.login.LoginActivity;

public class RegisterActivity extends BaseActivity {

    EditText etEmail;
    EditText etPassword;
    EditText etRePassword;
    MaterialButton btnRegister;
    private String strEmail, strPassword, strRePassword;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    private void init(){
        etEmail= findViewById(R.id.et_email);
        etPassword= findViewById(R.id.et_pwd);
        etRePassword= findViewById(R.id.et_repwd);
        btnRegister= findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Constant.TBL_USER);
    }


    private boolean valid() {
        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();
        strRePassword = etRePassword.getText().toString();
        if ("".equals(strEmail)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_email));
            etEmail.findFocus();
            return true;
        }
        if ("".equals(strPassword)) {
            showAlertDialog(this, getString(R.string.error), getString(R.string.check_password));
            etEmail.findFocus();
            return true;
        }
        if (strPassword.length()<6){
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
//                    Bundle bundle= new Bundle();
//                    bundle.putString(getString(R.string.key_email) , strEmail);
//                    bundle.putString(getString(R.string.key_password) , strPassword);
//                    bundle.putString( Constant.KEY_REGISTER, Constant.KEY_REGISTER);
                    DialogUtils.hideProgress();
                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();



                }).addOnFailureListener(e -> {
            showAlertDialog(this, getString(R.string.error), e.getMessage());
            DialogUtils.hideProgress();
        });
    }
}