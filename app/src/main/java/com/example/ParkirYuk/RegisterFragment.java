package com.example.ParkirYuk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    public static final String TAG = "TAG";
    EditText femail, fpassword, fusername, fconfPass;
    Button fRegister;
    TextView fLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Boolean isDataValid = false;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        femail = v.findViewById(R.id.Email);
        fusername = v.findViewById(R.id.Username);
        fpassword = v.findViewById(R.id.Password);
        fconfPass = v.findViewById(R.id.ConfirmPassword);
        fRegister = (Button) v.findViewById(R.id.Register);
        fLoginBtn = (TextView) v.findViewById(R.id.Login);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            Intent home = new Intent(getActivity(), HomeActivity.class);
            startActivity(home);

            return v;
        }

        fLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(femail);
                validateData(fusername);
                validateData(fpassword);
                validateData(fconfPass);

                if (!fpassword.getText().toString().equals(fconfPass.getText().toString())) {
                    isDataValid = false;
                    fconfPass.setError("Password do not match");
                } else {
                    isDataValid = true;
                }

                if (isDataValid) {
                    CreateNewAccount();
                }

            }
        });

        return v;
    }

    public void validateData(EditText field) {
        if (field.getText().toString().isEmpty()) {
            isDataValid = false;
            field.setError("Required Field");
        } else {
            isDataValid = true;
        }
    }

    public void CreateNewAccount(){
        String username = fusername.getText().toString();
        String email = femail.getText().toString();
        String password = fpassword.getText().toString();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "User Created", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(getActivity(), HomeActivity.class);
                    startActivity(home);
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", username);
                    user.put("Email", email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"user profile is created for "+userID);
                        }
                    });
                }else {
                    String message = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error : "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}