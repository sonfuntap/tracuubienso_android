package com.pth.tracuubienso.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class User extends ViewModel implements Serializable {
    private String isdUser;
    private String email;
    private String pass;
    private String phone;
    private boolean isAdmin;

    private MutableLiveData<User> userMutableLiveData= new MutableLiveData<>();


    public MutableLiveData<User> getUser() {
        return this.userMutableLiveData;
    }

    public void setUserCurrent(User user) {
        this.userMutableLiveData.setValue(user);
    }


    public String getIsdUser() {
        return isdUser;
    }

    public void setIsdUser(String isdUser) {
        this.isdUser = isdUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
