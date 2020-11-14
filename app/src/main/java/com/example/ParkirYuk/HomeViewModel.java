package com.example.ParkirYuk;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ParkirYuk.PlacesData;
import com.example.ParkirYuk.Repository;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<PlacesData>> liveData;
    private Repository dataRepo;

    public void init(){
        if (liveData != null){
            return;
        }
        dataRepo = Repository.getInstance();
        liveData = dataRepo.getData();
    }

    public LiveData<ArrayList<PlacesData>> getData(){
        return liveData;
    }
}