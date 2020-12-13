package com.example.ParkirYuk.Authentication;

import android.content.Intent;
import android.net.Uri;
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

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPasswordFragment extends Fragment {
    private static final String TAG = "forgotPasswordFragment";
    private Button submit;
    private EditText email;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        submit = v.findViewById(R.id.button_submit);
        email = v.findViewById(R.id.emailBox);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress= email.getText().toString();
                fAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Reset Password Sended", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Email Not Available", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Error Email Send");
                        }
                    }
                });
//                String to = email.getText().toString();
//
//                Intent Iemail = new Intent(Intent.ACTION_SEND);
//                Iemail.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
//                Iemail.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                Iemail.putExtra(Intent.EXTRA_TEXT, "message");
//
//                //need this to prompts email client only
//                Iemail.setType("message/rfc822");
//
//                startActivity(Intent.createChooser(Iemail, "Email Send"));
            }
        });

        return v;
    }
}