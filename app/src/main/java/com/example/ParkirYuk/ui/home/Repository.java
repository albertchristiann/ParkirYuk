package com.example.ParkirYuk.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ParkirYuk.model.PlacesData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Repository {
    private static final String TAG = "Repository";
    private static Repository instance;
    private static ArrayList<PlacesData> dataSet = new ArrayList<PlacesData>();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

//    private static OnDataAdded onDataAdded;
    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
//        onDataAdded = (OnDataAdded) context;
        return instance;
    }

    public MutableLiveData<ArrayList<PlacesData>> getData(){
        dataSet.clear();
        loadData();

        MutableLiveData<ArrayList<PlacesData>> data = new MutableLiveData<>();
        data.setValue(dataSet);
//        Log.d(TAG, "getData: added to view"+dataSet);
        return data;
    }

    private void loadData() {

        fStore.collectionGroup("places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, "onComplete: " + document.getId() + " => " + document.getData());
                                    PlacesData model = new PlacesData(String.valueOf(document.get("place"))
                                            ,Integer.parseInt(String.valueOf(document.get("current")))
                                            ,Integer.parseInt(String.valueOf(document.get("max")))
                                            ,String.valueOf(document.get("link"))
                                            ,String.valueOf(document.get("id"))
                                            ,String.valueOf(document.get("userID"))
                                            ,String.valueOf(document.get("address")));
                                    dataSet.add(model);
//                                    Log.d(TAG, "onComplete: "+document);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}
