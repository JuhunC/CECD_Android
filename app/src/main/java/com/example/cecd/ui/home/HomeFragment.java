package com.example.cecd.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.CursorLoader;
import androidx.navigation.Navigation;

import com.example.cecd.NetworkClient;
import com.example.cecd.R;
import com.example.cecd.UploadAPIs;
import com.example.cecd.ui.SharedViewModel;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private static Button imgsel,upload;
    private static ImageView img;
    private static RelativeLayout loading_circle;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loading_circle = root.findViewById(R.id.loadingPanel);
        loading_circle.setVisibility(View.INVISIBLE);

        img = root.findViewById(R.id.img);
        Ion.getDefault(getContext()).configure().setLogging("ion-sample", Log.DEBUG);

        imgsel = root.findViewById(R.id.select_img);
        upload = root.findViewById(R.id.uploading);
        if(SharedViewModel.getPath()==""){
            upload.setVisibility(View.INVISIBLE);
        }else{
            Bitmap myBitmap = BitmapFactory.decodeFile(SharedViewModel.getPath());
            img.setImageBitmap(myBitmap);
            upload.setVisibility(View.VISIBLE);
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Path",SharedViewModel.getPath());
                uploadToServer(SharedViewModel.getPath()); // Upload to Server
            }

        });
        imgsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, null);
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    String path = getPathFromURI(data.getData());
                    SharedViewModel.setPath(path);
                    img.setImageURI(data.getData());
                    upload.setVisibility(View.VISIBLE);

                }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        loading_circle.setVisibility(View.VISIBLE);
        // Receive Object Coordinates from the Server as an JSON Object
        Call<String> call = uploadAPIs.uploadImage(part, description);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Log.e("ResBody", response.toString());
                try {
                    JSONObject mainObject = new JSONObject(response.body());
                    SharedViewModel.image_file_dir = mainObject.getString("image_file_dir");
                    SharedViewModel.select(mainObject);
                    Iterator<String> keys = SharedViewModel.getSelected().keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        Object val = SharedViewModel.getSelected().get(key);
                  //      Log.e(key,val.toString());
                    }// end of while
                }catch(JSONException e){
                    e.printStackTrace();
                }
                loading_circle.setVisibility(View.GONE);
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_dashboard);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t){
                loading_circle.setVisibility(View.GONE);
                Log.e("Failed", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}