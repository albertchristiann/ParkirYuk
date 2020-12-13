package com.example.ParkirYuk.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class changePasswordFragment extends Fragment {
    private static final String TAG = "changePasswordFragment";
    private EditText newPass, currentPass, confirmPass;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String userID = fAuth.getCurrentUser().getUid(), currentPassword, newPassword, confirmPassword, confirmCurrent;
    private Button Submit;
    private Boolean isDataValid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        newPass = v.findViewById(R.id.newPassword);
        currentPass = v.findViewById(R.id.currentPassword);
        confirmPass = v.findViewById(R.id.confirmPassword);
        Submit = v.findViewById(R.id.button_submit);

        DocumentReference docRef = fStore.collection("users").document(userID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        confirmCurrent = doc.getString("Password");
                    }
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(currentPass);
                validateData(newPass);
                validateData(currentPass);

                if(isDataValid) {
                    currentPassword = currentPass.getText().toString();
                    newPassword = newPass.getText().toString();
                    confirmPassword = confirmPass.getText().toString();

                    if (currentPassword.equals(confirmCurrent)) {
                        if (newPassword.equals(confirmPassword)) {
                            if (newPassword.equals(currentPassword)) {
                                newPass.setError("new password cannot be the same as current password !");
                            } else {
                                docRef.update("Password", newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Update Password Succesfull", Toast.LENGTH_SHORT).show();
                                        Fragment profile = new userProfileFragment();
                                        FragmentManager fragmentManager = getParentFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.nav_host_fragment, profile);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Update Password Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            confirmPass.setError("confirm password is not same !");
                        }
                    } else {
                        currentPass.setError("Wrong Password");
                    }
                }
            }
        });

        return v;
    }

    private void validateData(EditText field){
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field");
        }else{
            isDataValid = true;
        }
    }

}