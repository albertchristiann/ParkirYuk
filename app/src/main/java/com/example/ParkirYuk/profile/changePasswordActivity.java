package com.example.ParkirYuk.profile;

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
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.example.ParkirYuk.SettingActivity;
import com.example.ParkirYuk.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class changePasswordActivity extends AppCompatActivity {
    private static final String TAG = "changePasswordActivity";
    private EditText newPass, currentPass, confirmPass;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String currentPassword, newPassword, confirmPassword, confirmCurrent;
    private Button Submit;
    private Boolean isDataValid;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState()==true){
            setTheme(R.style.darkTheme);
        }else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        newPass = findViewById(R.id.newPassword);
        currentPass = findViewById(R.id.currentPassword);
        confirmPass = findViewById(R.id.confirmPassword);
        Submit = findViewById(R.id.button_submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(currentPass);
                validateData(newPass);
                validateData(confirmPass);

                if (isDataValid) {
                    currentPassword = currentPass.getText().toString();
                    newPassword = newPass.getText().toString();
                    confirmPassword = confirmPass.getText().toString();

//                    if (currentPassword.equals(confirmCurrent)) {
                    if (currentPassword.equals(newPassword)) {
                        newPass.setError("new password cannot be the same with previous password");
                    } else {
                        if (newPassword.equals(confirmPassword)) {
                            fAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(changePasswordActivity.this, "Update Password Succesfull", Toast.LENGTH_SHORT).show();
                                        Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
                                        startActivity(setting);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(changePasswordActivity.this, "Update Authentication Password Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            confirmPass.setError("confirm password is not same !");
                        }
                    }
                }
            }
        });
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