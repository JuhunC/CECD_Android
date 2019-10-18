package com.example.cecd.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Rectangle;
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
    private static ImageView img, bit_Img;
    private static ListView object_list;
    private static ArrayList<Data> objects;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Parse JSON data into Data object
        objects = parse(svm.getSelected());

        // Add List of Objects to xml(object_list)
        object_list = root.findViewById(R.id.object_list);
        object_list.setVisibility(View.VISIBLE);

        obj adapter=new obj(objects,getContext());

        object_list.setAdapter(adapter);

        img = root.findViewById(R.id.img_dashboard);

        // Draw Rectangle on the image using JSON data
        this.update();

//        // Show Image
//        File imgFile = new  File(svm.getPath());
//        if(imgFile.exists()){
//            Log.e("path",svm.getPath());
//
//            Bitmap bmp=BitmapFactory.decodeFile(svm.getPath()).copy(Bitmap.Config.RGB_565, true);//(img.getHeight(),img.getWidth(),Bitmap.Config.RGB_565);
//
//            Canvas canvas = new Canvas(bmp);
//            for(int i =0;i<objects.size();i++){
//                Data data = objects.get(i);
//                if(data.isChecked() == true){
//                    Paint paint = new Paint();
//                    paint.setStyle(Paint.Style.STROKE);
//                    paint.setStrokeWidth(R.dimen.rectangle_stroke_thickness);
//                    paint.setColor(Color.RED);
//                    paint.setAntiAlias(true);
//
//                    // left, top, right, bottom
//                    Rect rect = new Rect(
//                            data.getX(),
//                            data.getY(),
//                            data.getWidth() + data.getX(),
//                            data.getHeight() + data.getY()
//                        );
//
//                    canvas.drawRect(rect, paint);
//                }
//            }
//
//
//
//
//
//            img.setImageBitmap(bmp);
//        }else { // If there is no image
//            getFragmentManager().popBackStack();
//        }

        return root;
    }


    public static ArrayList<Data> parse(JSONObject json){
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

    // Update Picture with Rectangles
    public static void update(){
        Log.e("path",svm.getPath());

        Bitmap bmp=BitmapFactory.decodeFile(svm.getPath()).copy(Bitmap.Config.RGB_565, true);//(img.getHeight(),img.getWidth(),Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bmp);
        for(int i =0;i<objects.size();i++){
            Data data = objects.get(i);
            if(data.isChecked() == true){
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10); // set rectangle stroke thickness
                paint.setColor(Color.RED);
                paint.setAntiAlias(true);

                // left, top, right, bottom
                Rect rect = new Rect(
                        data.getX(),
                        data.getY(),
                        data.getWidth() + data.getX(),
                        data.getHeight() + data.getY()
                );

                canvas.drawRect(rect, paint);
            }
        }

//            bit_Img = root.findViewById(R.id.bit_img_dashboard);
        img.setImageBitmap(bmp);
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
