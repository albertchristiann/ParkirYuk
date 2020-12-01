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
    String adminID, sPlace, sCurrent, sMax;
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

        CustomAdminOne();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+sCurrent);
                Integer curr = Integer.parseInt(sCurrent);
                curr = curr++;
                db.collection("users")
                        .document(adminID)
                        .collection("places")
                        .document("taman surya")
                        .update("current", FieldValue.increment(1));
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment,new InputFragment()).addToBackStack(null).commit();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return v;
    }

    private void CustomAdminOne(){
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
                                sCurrent = String.valueOf(document.get("current"));
                                sMax = String.valueOf(document.get("max"));
                                place.setText(sPlace);
                                current.setText(sCurrent);
                                max.setText(sMax);
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