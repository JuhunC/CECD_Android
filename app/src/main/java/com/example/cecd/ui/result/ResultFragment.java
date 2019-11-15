package com.example.cecd.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cecd.R;
import com.example.cecd.ui.SharedViewModel;

public class ResultFragment extends Fragment {

    private ResultViewModel resultViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        resultViewModel =
                ViewModelProviders.of(this).get(ResultViewModel.class);
        View root = inflater.inflate(R.layout.fragment_result, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        final ImageView imgView = root.findViewById(R.id.img_notification);
        imgView.setImageBitmap(SharedViewModel.bitmap);

        return root;
    }
}