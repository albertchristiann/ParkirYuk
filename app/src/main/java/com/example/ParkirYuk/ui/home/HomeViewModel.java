package com.example.ParkirYuk.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ParkirYuk.model.HomeModel;
import com.example.ParkirYuk.model.PlacesData;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private MutableLiveData<ArrayList<HomeModel>> liveData;
    private Repository dataRepo;

    public void init(){
        if (liveData != null){
            return;
        }
        dataRepo = Repository.getInstance();
        liveData = dataRepo.getData();
    }

    public MutableLiveData<ArrayList<HomeModel>> getData(){
        return liveData;
    }
}