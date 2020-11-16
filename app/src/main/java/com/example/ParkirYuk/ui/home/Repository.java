package com.example.ParkirYuk.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ParkirYuk.model.HomeModel;
import com.example.ParkirYuk.model.PlacesData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final String TAG = "Repository";
    private static Repository instance;
    private ArrayList<HomeModel> dataSet = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

//    private static OnDataAdded onDataAdded;
    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
//        onDataAdded = (OnDataAdded) context;
        return instance;
    }

    public MutableLiveData<ArrayList<HomeModel>> getData(){
        loadData();

        MutableLiveData<ArrayList<HomeModel>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        Log.d(TAG, "getData: added to view");
        return data;
    }

    private void loadData() {
        db.collection("places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot documentSnapshot : list){
                        dataSet.add(documentSnapshot.toObject(HomeModel.class));
                    }
                    Log.v(TAG, "onSuccess: added database");
//                    onDataAdded.added();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }
}
