package com.pth.tracuubienso.dialog;

import android.content.Context;


/**
 * Created by ducpv on 3/25/18.
 */

public class DialogUtils {
    private static ProgressDialog mProgressDialog;
    private static int mProgressCounter;

    public static void showProgress(Context context) {

        if (mProgressDialog == null) {
            mProgressCounter = 0;
            mProgressDialog = new ProgressDialog(context);
        } else {
            mProgressCounter++;
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public static void hideProgress() {
        if (mProgressDialog != null) {
            mProgressCounter--;
            if (mProgressCounter <= 0) {
                mProgressCounter = 0;
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }
    }
}
