package com.example.ParkirYuk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

public class LoginFragment extends Fragment {
    EditText femail, fpassword;
    Button fLogin;
    TextView fRegisterBtn;
    FirebaseAuth fAuth;
    Boolean isDataValid = false;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        femail = v.findViewById(R.id.email);
        fpassword = v.findViewById(R.id.password);
        fLogin = (Button) v.findViewById(R.id.button_login);
        fRegisterBtn = (TextView) v.findViewById(R.id.Register);
        fAuth = FirebaseAuth.getInstance();


        if (fAuth.getCurrentUser() != null){
            Intent home = new Intent(getActivity(), HomeActivity.class);
            startActivity(home);

            return v;
        }

        fRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Fragment loginFragment = new RegisterFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
                                Toast.makeText(getActivity(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(getActivity(), HomeActivity.class);
                                startActivity(home);
                            }else{
                                Toast.makeText(getActivity(), "Please input your email/password correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        return v;
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