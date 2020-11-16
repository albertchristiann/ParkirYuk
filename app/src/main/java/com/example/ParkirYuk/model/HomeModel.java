package com.example.ParkirYuk.model;

public class HomeModel {
    private String place;

    public HomeModel(String places){
        this.place = places;
    }

    public HomeModel(){
    }

    public void setPlaces(String places) {
        this.place = places;
    }

    public String getPlaces() {
        return place;
    }
}
