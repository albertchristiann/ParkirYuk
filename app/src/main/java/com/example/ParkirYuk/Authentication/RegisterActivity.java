package com.example.ParkirYuk.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.AdminUser.HomeActivity;
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

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText femail, fpassword, fusername, fconfPass;
    Button fRegister;
    TextView fLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        femail = findViewById(R.id.Email);
        fusername = findViewById(R.id.Username);
        fpassword = findViewById(R.id.Password);
        fconfPass = findViewById(R.id.ConfirmPassword);
        fRegister = findViewById(R.id.Register);
        fLoginBtn = findViewById(R.id.Login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home);
        }

        fLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        fRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });
    }

    private boolean validateName() {
        String val = fusername.getText().toString();

        if(val.isEmpty()){
            fusername.setError("Field cannot be empty");
            return false;
        }else{
            fusername.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = femail.getText().toString();

        if(val.isEmpty()){
            femail.setError("Field cannot be empty");
            return false;
        }else{
            femail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = fpassword.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\s+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if(val.isEmpty()){
            fpassword.setError("Field cannot be empty");
            return false;
        }else{
            fpassword.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPass() {
        String val = fconfPass.getText().toString();

        if(val.isEmpty()){
            fconfPass.setError("Field cannot be empty");
            return false;
        }else if(!val.equals(fpassword.getText().toString())){
            fconfPass.setError("Password not match");
            return false;
        }else{
            fconfPass.setError(null);
            return true;
        }
    }

    public void createNewUser(){
        if (!validateConfirmPass() | !validateEmail() | !validateName() | !validatePassword()){
            return;
        }

        String username = fusername.getText().toString();
        String email = femail.getText().toString().trim();
        String password = fpassword.getText().toString().trim();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(home);
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", username);
                    user.put("Email", email);
//                    user.put("Password", password);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "user profile created", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    String historyID = UUID.randomUUID().toString();
//                    Map<String, Object> history = new HashMap<>();
//                    history.put("id", historyID);
//                    fStore.collection("users").document(userID).collection("history")
//                            .document(historyID)
//                            .set(history).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Log.d(TAG, "onComplete: history collection created");
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d(TAG, "onFailure: history collection failed");
//                        }
//                    });
                }else {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "Error! : "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}