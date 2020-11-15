package com.pth.tracuubienso.modules.add_province;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pth.tracuubienso.R;
import com.pth.tracuubienso.constant.Constant;
import com.pth.tracuubienso.models.Province;

public class RemoveBxsDilalog extends DialogFragment {
    ListView listView;
    MaterialButton btnCancel;
    DatabaseReference databaseReferenceProvince;
    Province province;
    IAddProvince iAddProvince;

    public RemoveBxsDilalog(Province province, IAddProvince iAddProvince) {
        this.province = province;
        this.iAddProvince= iAddProvince;
    }


    @Override
    public void onStart() {
        super.onStart();
        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.8);
        setupDialog(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReferenceProvince = FirebaseDatabase.getInstance().getReference(Constant.TBL_PROVINCE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remove_dialog, container, false);
        btnCancel = view.findViewById(R.id.btnCancel);
        listView = view.findViewById(R.id.listview);

        ArrayAdapter<String> baseAdapter = new ArrayAdapter<String>(getContext(), R.layout.row, R.id.textItem, province.getCodeProvinces()) ;
        listView.setAdapter(baseAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"Đã xóa: "+ province.getCodeProvinces().get(position), Toast.LENGTH_SHORT).show();
                province.getCodeProvinces().remove(position);
                removeProvince(province);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;


    }

    private void removeProvince(Province provinceOld) {
        databaseReferenceProvince.child(provinceOld.getNameProvince())
                .setValue(provinceOld)
                .addOnSuccessListener(aVoid -> {
                    iAddProvince.updateProvince(provinceOld);
                    dismiss();
                }).addOnFailureListener(e -> {
            Toast.makeText(getContext(),
                    "Lỗi: " + e.getMessage() + "\n Vui lòng thử lại", Toast.LENGTH_SHORT).show();
        });
    }

    protected void setupDialog(int width, int height) {
        if (getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        //set dialog size
        getDialog().getWindow().setLayout(width,
                height);
    }
}
