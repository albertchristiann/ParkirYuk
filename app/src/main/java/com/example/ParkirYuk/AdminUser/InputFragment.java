package com.example.ParkirYuk.AdminUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.example.ParkirYuk.model.PlacesData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class InputFragment extends Fragment {
    private static final String TAG = "InputFragment";
    TextView current,max, place;
    ImageView plus,minus;
    String adminID, sPlace, placeID;
    Integer iCurr, iMax;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input, container, false);
        current = v.findViewById(R.id.current);
        max = v.findViewById(R.id.max);
        place = v.findViewById(R.id.placeName);
        plus = v.findViewById(R.id.add);
        minus = v.findViewById(R.id.remove);
        adminID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(adminID).collection("places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, "onComplete: " + document.getId() + " => " + document.getData());
                                    placeID = String.valueOf(document.get("id"));
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

        CustomAdmin();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iCurr<iMax) {
                    db.collection("users")
                            .document(adminID)
                            .collection("places")
                            .document(placeID)
                            .update("current", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                iCurr++;
                                Log.d(TAG, "onComplete: add success");
                                current.setText(String.valueOf(iCurr));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: add fail");
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Full", Toast.LENGTH_SHORT).show();
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iCurr>=0) {
                    db.collection("users")
                            .document(adminID)
                            .collection("places")
                            .document(placeID)
                            .update("current", FieldValue.increment(-1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                iCurr--;
                                Log.d(TAG, "onComplete: minus success");
                                current.setText(String.valueOf(iCurr));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: minus fail");
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Minimum Data", Toast.LENGTH_SHORT).show();
                }
                
            }
        });




        return v;
    }

    public void CustomAdmin(){
        db.collection("users")
                .document(adminID)
                .collection("places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                sPlace = String.valueOf(document.get("place"));
                                iCurr = Integer.parseInt(String.valueOf(document.get("current")));
                                iMax = Integer.parseInt(String.valueOf(document.get("max")));
                                place.setText(sPlace);
                                current.setText(String.valueOf(iCurr));
                                max.setText(String.valueOf(iMax));
                                Log.d(TAG, "onComplete: current number make "+iCurr);
                            }
                        }else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e);
                    }
                });
    }
}