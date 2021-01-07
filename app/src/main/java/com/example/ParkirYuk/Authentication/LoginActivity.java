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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText femail, fpassword;
    Button fLogin;
    TextView fRegisterBtn, forgotPass;
    FirebaseAuth fAuth;
    Boolean isDataValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        femail = findViewById(R.id.email);
        fpassword = findViewById(R.id.password);
        fLogin = findViewById(R.id.button_login);
        fRegisterBtn = findViewById(R.id.Register);
        fAuth = FirebaseAuth.getInstance();
        forgotPass = findViewById(R.id.forgotPassword);

        if (fAuth.getCurrentUser() != null){
            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(home);
        }

        fRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent regis = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regis);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpass = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(forgotpass);
            }
        });

        fLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(femail);
                validateData(fpassword);

                if(isDataValid){
                    fAuth.signInWithEmailAndPassword(femail.getText().toString(), fpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(home);
                            }else{
                                Toast.makeText(LoginActivity.this, "Please input your email/password correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void validateData(EditText field){
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field");
        }else{
            isDataValid = true;
        }
    }
}