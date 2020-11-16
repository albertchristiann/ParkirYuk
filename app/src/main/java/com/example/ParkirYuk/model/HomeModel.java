package com.example.ParkirYuk.model;

public class HomeModel {
    private String places;

    public HomeModel(String places){
        this.places = places;
    }

    public HomeModel(){
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getPlaces() {
        return places;
    }
}
