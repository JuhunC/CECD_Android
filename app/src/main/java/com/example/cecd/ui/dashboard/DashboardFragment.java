package com.example.cecd.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cecd.R;
import com.example.cecd.obj;
import com.example.cecd.ui.SharedViewModel;
import com.example.cecd.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class DashboardFragment extends Fragment {
    private static SharedViewModel svm = new SharedViewModel();
    private DashboardViewModel dashboardViewModel;
    private static ImageView img;
    private static ListView object_list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Show Image
        File imgFile = new  File(svm.getPath());
        if(imgFile.exists()){
            Log.e("path",svm.getPath());
            Bitmap myBitmap = BitmapFactory.decodeFile(svm.getPath());
            img = root.findViewById(R.id.img_dashboard);
            img.setImageBitmap(myBitmap);
        }else { // If there is no image
            getFragmentManager().popBackStack();
        }

        ArrayList<Data> objects = parse(svm.getSelected());

        object_list = root.findViewById(R.id.object_list);
        object_list.setVisibility(View.VISIBLE);

        obj adapter=new obj(objects,getContext());

        object_list.setAdapter(adapter);



        return root;
    }


    public ArrayList<Data> parse(JSONObject json){
        ArrayList<Data> res = new ArrayList<>();
        Iterator<String> keys = json.keys();
        try {
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray va = json.getJSONArray(key);
                Data tmp = new Data(key,va.getDouble(4),
                        va.getInt(0),
                        va.getInt(1),
                        va.getInt(2),
                        va.getInt(3));
                res.add(tmp);
            }// end of while
        }catch(JSONException e){
            e.printStackTrace();
        }
        return res;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.object_checkbox) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
