package com.example.ParkirYuk.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "forgotPasswordFragment";
    private Button submit;
    private EditText email;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        submit = findViewById(R.id.button_submit);
        email = findViewById(R.id.emailBox);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress= email.getText().toString();
                fAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Reset Password Sended", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, "Email Not Available", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Error Email Send");
                        }
                    }
                });
            }
        });
    }
}