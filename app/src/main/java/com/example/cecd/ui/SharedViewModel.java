package com.example.cecd.ui;

import android.graphics.Bitmap;

import com.example.cecd.Data;

import org.json.JSONObject;

import java.util.ArrayList;

public class SharedViewModel {
    private static JSONObject label_data;
    private static String path ="";
    public static ArrayList<Data> objects;
    public static String image_file_dir;
    public static Bitmap bitmap;

    public static void select(JSONObject json_object){
        label_data = json_object;
    }
    public static JSONObject getSelected(){
        return label_data;
    }
    public static void setPath(String path_){ path = path_;}
    public static String getPath(){return path;}
    public static void setObjects(ArrayList<Data> objects_){objects = objects_;}
    public static ArrayList<Data> getObjects(){return objects;}
}
