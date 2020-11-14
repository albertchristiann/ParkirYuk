package com.example.ParkirYuk;

public class PlacesData {
    private String places;
    private int current;
    private int max;

    public PlacesData(String places, int current, int max){
        this.places = places;
        this.current = current;
        this.max = max;
    }

    public PlacesData(){
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
}
