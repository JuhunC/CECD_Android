package com.example.cecd.ui;

import org.json.JSONObject;

public class SharedViewModel {
    private static JSONObject label_data;
    private static String path ="";

    public void select(JSONObject json_object){
        label_data = json_object;
    }
    public JSONObject getSelected(){
        return label_data;
    }
    public void setPath(String path){ this.path = path;}
    public String getPath(){return path;}
}
