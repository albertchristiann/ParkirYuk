package com.example.ParkirYuk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nonnull;

public class LoginActivity extends AppCompatActivity {

    EditText ePassword, eEmail;
    Button eLogin;
    TextView eRegisterBtn;
    FirebaseAuth mAuth;

    boolean isValid = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eEmail = findViewById(R.id.email);
        ePassword = findViewById(R.id.password);
        eLogin = findViewById(R.id.button_login);
        eRegisterBtn = findViewById(R.id.Register);
        mAuth = FirebaseAuth.getInstance();

        eLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String inputEmail = eEmail.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if(TextUtils.isEmpty(inputEmail)){
                    eEmail.setError("Email is Required!");
                    return;
                }

                if(TextUtils.isEmpty(inputPassword)){
                    ePassword.setError("Password is Required!");
                    return;
                }
                mAuth.signInWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(new  OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@Nonnull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "Error!" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        eRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}