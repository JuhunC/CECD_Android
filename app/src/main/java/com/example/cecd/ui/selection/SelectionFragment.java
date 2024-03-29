package com.example.cecd.ui.selection;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.cecd.DeleteAPIs;
import com.example.cecd.NetworkClient;
import com.example.cecd.R;
import com.example.cecd.obj;
import com.example.cecd.ui.SharedViewModel;
import com.example.cecd.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectionFragment extends Fragment{
    private SelectionViewModel selectionViewModel;
    private static ImageView img;
    private static ListView object_list;
    private static RelativeLayout loading_circle;
    private static Button regenerate_btn, add_object_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        selectionViewModel =
                ViewModelProviders.of(this).get(SelectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_selection, container, false);

        // Parse JSON data into Data object
        if(SharedViewModel.getSelected() != null) {
            SharedViewModel.setObjects(this.parse(SharedViewModel.getSelected()));
        }
        // Add List of Objects to xml(object_list)
        object_list = root.findViewById(R.id.object_list);
        object_list.setVisibility(View.VISIBLE);

        loading_circle = root.findViewById(R.id.loadingPanel);
        loading_circle.setVisibility(View.INVISIBLE);

        regenerate_btn = root.findViewById(R.id.regenerate_btn);
        add_object_btn = root.findViewById(R.id.regenerate_btn);


        regenerate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Path",SharedViewModel.getPath());
                uploadToServerForRegeneration(SharedViewModel.objects);
            }

        });
        if(SharedViewModel.objects != null) {
            obj adapter = new obj(SharedViewModel.objects, getContext());
            object_list.setAdapter(adapter);
        }
        img = root.findViewById(R.id.img_dashboard);

        // Draw Rectangle on the image
        this.update();

        return root;
    }

    public static ArrayList<Data> parse(JSONObject json){
        Log.e("JSON", json.toString());
        ArrayList<Data> res = new ArrayList<>();
        Iterator<String> keys = json.keys();
        try {

            while (keys.hasNext()) {
                String key = keys.next();
                if(key.equals("image_file_dir")) {
                    Log.e("KEY", json.get(key).toString());
                    SharedViewModel.image_file_dir = json.get(key).toString();
                }else {

                    JSONArray va = json.getJSONArray(key);
                    Data tmp = new Data(key, va.getDouble(4),
                            va.getInt(0),
                            va.getInt(1),
                            va.getInt(2),
                            va.getInt(3));
                    res.add(tmp);
                }
            }// end of while
        }catch(JSONException e){
            e.printStackTrace();
        }
        return res;
    }

    // Update Picture with Rectangles
    public static void update(){
        if(SharedViewModel.getObjects() == null) return;
        Bitmap bmp=BitmapFactory.decodeFile(SharedViewModel.getPath()).copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bmp);
        for(int i =0;i<SharedViewModel.getObjects().size();i++){
            Data data = SharedViewModel.objects.get(i);
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
    private void uploadToServerForRegeneration(ArrayList<Data> objects){
        JSONObject paramObj = new JSONObject();
        try {
            for (Data data : objects) {
                if (data.isChecked() == true) {
                    JSONArray array = new JSONArray();
                    array.put(data.getX());
                    array.put(data.getY());
                    array.put(data.getWidth());
                    array.put(data.getHeight());
                    array.put(data.getAccuracy());
                    paramObj.put(data.getLabel(),  array);
                }
            }
            JSONArray arr = new JSONArray();
            arr.put(SharedViewModel.image_file_dir);
            paramObj.put("image_file_dir", arr);
            Log.d("paramObj",paramObj.toString());
        }catch(JSONException e){
            e.printStackTrace();
        }
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        DeleteAPIs deleteAPIs = retrofit.create(DeleteAPIs.class);
        //Create request body with text description and text media type
        loading_circle.setVisibility(View.VISIBLE);
        // Receive Object Coordinates from the Server as an JSON Object
        //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramObj.toString());
        String str = paramObj.toString();
        Log.e("str",str);
        Call<ResponseBody> call = deleteAPIs.deleteObjectImage(str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        SharedViewModel.bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        loading_circle.setVisibility(View.GONE);
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_selection_to_navigation_result);
                    } else {
                        Log.e("No Response Body","");
                    }
                } else {
                    Log.e("Response Unsuccessful", "");
                }
                loading_circle.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                loading_circle.setVisibility(View.GONE);
                Log.e("Failed", t.getMessage());
                t.printStackTrace();
            }
        });
    }

}