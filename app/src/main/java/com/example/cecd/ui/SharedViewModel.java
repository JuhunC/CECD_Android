package com.example.cecd.ui;

import com.example.cecd.Data;

import org.json.JSONObject;

import java.util.ArrayList;

public class SharedViewModel {
    private static JSONObject label_data;
    private static String path ="";
    public static ArrayList<Data> objects;
    public static String image_file_dir;

    public void select(JSONObject json_object){
        label_data = json_object;
    }
    public JSONObject getSelected(){
        return label_data;
    }
    public void setPath(String path){ this.path = path;}
    public String getPath(){return path;}
    public void setObjects(ArrayList<Data> objects){this.objects = objects;}
    public ArrayList<Data> getObjects(){return objects;}
}
