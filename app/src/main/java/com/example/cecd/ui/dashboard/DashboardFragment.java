package com.example.cecd.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cecd.R;
import com.example.cecd.ui.SharedViewModel;

import java.io.File;

public class DashboardFragment extends Fragment {
    private static SharedViewModel svm = new SharedViewModel();
    private DashboardViewModel dashboardViewModel;
    private static ImageView img;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        File imgFile = new  File(svm.getPath());

        if(imgFile.exists()){
            Log.e("path",svm.getPath());

            Bitmap myBitmap = BitmapFactory.decodeFile(svm.getPath());

            img = root.findViewById(R.id.img_dashboard);

            img.setImageBitmap(myBitmap);

        }else {
            getFragmentManager().popBackStack();
        }

        return root;
    }
}