package com.pth.tracuubienso.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;


import com.pth.tracuubienso.R;

import java.util.Objects;




public class ProgressDialog extends Dialog {
    private ProgressBar progressBar;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(0));

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_progress, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}
