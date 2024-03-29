package com.example.cecd;

/**
 * Data Class - for saving location of one object
 */
public class Data {
    private String label;
    private double accuracy;            // [0,1]
    private int x,y, width, height;
    private boolean checked = true;     // Selection for Regeneration
    public Data(String label, double accuracy, int start_x, int start_y, int end_x, int end_y){
        this.label = label;
        this.accuracy = accuracy;
        x = start_x;    y = start_y;
        width = end_x - start_x;
        height = end_y - start_y;
    }
    public String getLabel(){ return label;}
    public double getAccuracy(){return accuracy;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public void setChecked(boolean check){this.checked = check;}
    public boolean isChecked(){return checked;}
}
