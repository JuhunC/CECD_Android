package com.example.cecd.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.cecd.R;
import com.example.cecd.obj;
import com.example.cecd.ui.SharedViewModel;
import com.example.cecd.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DashboardFragment extends Fragment implements View.OnClickListener {
    private static SharedViewModel svm = new SharedViewModel();
    private DashboardViewModel dashboardViewModel;
    private static ImageView img, bit_Img;
    private static ListView object_list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Parse JSON data into Data object
        svm.setObjects(parse(svm.getSelected()));

        // Add List of Objects to xml(object_list)
        object_list = root.findViewById(R.id.object_list);
        object_list.setVisibility(View.VISIBLE);

        obj adapter=new obj(svm.objects,getContext());

        object_list.setAdapter(adapter);

        img = root.findViewById(R.id.img_dashboard);

        // Draw Rectangle on the image
        this.update();

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
        Bitmap bmp=BitmapFactory.decodeFile(svm.getPath()).copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bmp);
        for(int i =0;i<svm.getObjects().size();i++){
            Data data = svm.objects.get(i);
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
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.add_object_btn:



                break;
            case R.id.regenerate_btn:
                // TODO : send data to server for regeneration

                Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_navigation_notifications);
                break;
            default:
                Log.e("Unknown OnClick Event", view.getId()+"");
        }
    }
}