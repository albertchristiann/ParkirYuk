package com.example.ParkirYuk;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FeedbackFragment extends Fragment {
    private static final String TAG = "FeedbackFragment";
    Button submit;
    EditText email, feedback;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        email =(EditText) v.findViewById(R.id.emailEditText);
        feedback =(EditText) v.findViewById(R.id.editTextFeedback);
        submit = (Button) v.findViewById(R.id.submitFeedback);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFeedback();
            }
        });

        return v;
    }

    private boolean validateEmail() {
        String emailString = email.getText().toString();

        if(emailString.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    private boolean validateFeedback() {
        String description = feedback.getText().toString();

        if(description.isEmpty()){
            feedback.setError("Field cannot be empty");
            return false;
        }else{
            feedback.setError(null);
            return true;
        }
    }

    public void createNewFeedback(){
        if (!validateEmail() | !validateFeedback()){
            return;
        }

        String emailString = email.getText().toString();
        String description = feedback.getText().toString();
        uploadData(emailString, description);
        email.setText("");
        feedback.setText("");
    }

    public void uploadData(String email, String description){
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
//        doc.put("id", id);
        doc.put("Email", email);
        doc.put("Feedback", description);

        fStore.collection("feedback").add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Feedback succesfull", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Feedback Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}