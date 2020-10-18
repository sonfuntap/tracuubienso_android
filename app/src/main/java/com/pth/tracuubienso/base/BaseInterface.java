package com.pth.tracuubienso.base;

import com.pth.tracuubienso.models.User;

public interface BaseInterface {
    void getCurrentUserSuccess(User user);
    void getCurrentUserFailed(String message);
}
