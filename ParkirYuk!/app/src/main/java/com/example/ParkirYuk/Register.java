package com.example.ParkirYuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText userName, userPassword, confirmPassword, userEmail;
    Button eRegister;
    TextView eLogin;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.Username);
        userPassword = findViewById(R.id.Password);
        userEmail = findViewById(R.id.Email);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        eRegister = findViewById(R.id.Register);
        eLogin = findViewById(R.id.Login);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    public void CreateNewAccount(){
        String username = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confPass = confirmPassword.getText().toString();

        if(email.isEmpty()){
            Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
        }else if(username.isEmpty()){
            Toast.makeText(this, "Username Required", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show();
        }else if(confPass.isEmpty()){
            Toast.makeText(this, "Confirm Password Required", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(confPass)){
            Toast.makeText(this, "Password not match!", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", username);
                        user.put("Email", email);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSuccess: user profile is created for "+userID);
                            }
                        });
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(Register.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
