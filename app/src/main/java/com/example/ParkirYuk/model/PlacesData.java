package com.example.ParkirYuk.model;

import android.util.Log;

public class PlacesData {
    private static final String TAG = "tag";
    private String mplaces;
    private int mcurrent;
    private int mmax;

    public PlacesData(String places, int current, int max){
        this.mplaces = places;
        this.mcurrent = current;
        this.mmax = max;
    }

    public PlacesData(){
    }

    public String getPlaces() {
        return mplaces;
    }

    public void setPlaces(String places) {
        this.mplaces = places;
    }

    public void setCurrent(int current) {
        this.mcurrent = current;
    }

    public int getCurrent() {
        return mcurrent;
    }

    public void setMax(int max) {
        this.mmax = max;
    }

    public int getMax() {
        return mmax;
    }
}
