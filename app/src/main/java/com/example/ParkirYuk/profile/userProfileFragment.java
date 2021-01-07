package com.example.ParkirYuk.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class userProfileFragment extends Fragment {
    private static final String TAG = "userProfileFragment";
    private TextView username,email, place, countNumber, timestamp, changePassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private ArrayList<String> name = new ArrayList<>(), strDate = new ArrayList<>();
    private String userID;
    private ListView listView;
    private int count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        username = v.findViewById(R.id.getUsername);
        email = v.findViewById(R.id.getEmail);
        place = v.findViewById(R.id.namePlaceHistory);
        countNumber = v.findViewById(R.id.countHistory);
        timestamp = v.findViewById(R.id.timestamp);
        listView = v.findViewById(R.id.listView1);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        fetchProfile();
        fetchHistoryData();


        return v;
    }

    private void fetchProfile(){
        DocumentReference docRef = fStore.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null &&  document.exists()){
                        email.setText(document.getString("Email"));
                        username.setText(document.getString("Name"));
                    }else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void fetchHistoryData(){
        count = 0;
        fStore.collection("users").document(userID).collection("history")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, "onComplete: history " + document.getId() + " => " + document.getData());
//                                    countNumber.setText(String.valueOf(count));
                                    name.add(String.valueOf(document.getString("place")));
                                    Timestamp tstm = (Timestamp) document.getData().get("time");
                                    Date date = tstm.toDate();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                                    strDate.add(String.valueOf(dateFormat.format(date)));
//                                    timestamp.setText(strDate);
//                                    Log.d(TAG, "onComplete: history date "+strDate);
//                                    Log.d(TAG, "onComplete: history place name"+name);
                                    count = count+1;
                                    Log.d(TAG, "onComplete: count "+count);
                                    MyAdapter adapter = new MyAdapter(getActivity(), name, strDate, count);
                                    listView.setAdapter(adapter);
                                }
                            }
                        } else {
                            Log.w(TAG, "No such document history");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: history");
            }
        });

    }
}