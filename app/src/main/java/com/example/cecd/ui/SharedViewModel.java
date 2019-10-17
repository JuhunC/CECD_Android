package com.example.cecd.ui;

import org.json.JSONObject;

public class SharedViewModel {
    private static JSONObject label_data;
    public void select(JSONObject json_object){
        label_data = json_object;
    }
    public JSONObject getSelected(){
        return label_data;
    }
}
