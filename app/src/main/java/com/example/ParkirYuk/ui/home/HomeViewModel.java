package com.example.ParkirYuk.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ParkirYuk.model.PlacesData;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private MutableLiveData<ArrayList<PlacesData>> liveData;
    private Repository dataRepo;

    public void init(){
        if (liveData != null){
            return;
        }
        dataRepo = Repository.getInstance();
        liveData = dataRepo.getData();
    }

    public MutableLiveData<ArrayList<PlacesData>> getData(){
        return liveData;
    }
}