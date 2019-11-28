package com.example.cecd.ui.result;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cecd.R;
import com.example.cecd.ui.SharedViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

        if(isExternalStorageWritable()) {
            try {
                File file = new File(SharedViewModel.getPath().substring(0, SharedViewModel.getPath().length()-4)+"_out.png");
                file.createNewFile();
                file.setWritable(true);
                if (file.exists()) file.delete();
                FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
                SharedViewModel.bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                out.close();
                // PNG is a lossless format, the compression factor (100) is ignored
                Log.e("Bitmap Saved!!", file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                Log.e("Bitmap File Not Found Error: ", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Bitmap IO Error: ", e.getMessage());
                e.printStackTrace();
            }
        }else{
            Log.e("External Storage Unavailiable","");
        }

        return root;
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}