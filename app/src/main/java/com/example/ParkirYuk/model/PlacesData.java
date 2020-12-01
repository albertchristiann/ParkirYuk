package com.example.ParkirYuk.model;

import android.util.Log;

public class PlacesData {
    private static final String TAG = "tag";
    private String mplaces, link, id, userID, address;
    private int mcurrent, mmax;

    public PlacesData(){
    }

    public PlacesData(String places, int current, int max, String link, String id, String userID, String address){
        this.mplaces = places;
        this.mcurrent = current;
        this.mmax = max;
        this.link = link;
        this.id = id;
        this.userID = userID;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
