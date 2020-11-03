package com.example.ParkirYuk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class DetailsFragment extends Fragment {
    private static final String TAG = "tag";
    private TextView place, max, current;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        place = v.findViewById(R.id.place);
        max = v.findViewById(R.id.max);
        current = v.findViewById(R.id.current);
        fStore = FirebaseFirestore.getInstance();

//        Bundle bundle = getArguments();
//        String placeName = bundle.getString("PlaceName");

        fStore.collection("places").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                            place.setText(snapshot.getString("place"));
//                            max.setText(snapshot.getString("max"));
                        }
                    }
                });

        return v;
    }
}