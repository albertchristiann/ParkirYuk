package com.example.ParkirYuk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class DetailsFragment extends Fragment {
    private static final String TAG = "tag";
    private TextView place, max, current;

    FirebaseFirestore fStore;
    public static String KEY_FRG = "msg_fragment";
    public static String KEY_FRG1 = "msg_fragment1";
    public static String KEY_FRG2 = "msg_fragment2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        place = v.findViewById(R.id.place);
        max = v.findViewById(R.id.max);
        current = v.findViewById(R.id.current);
        ImageButton imageButton = v.findViewById(R.id.refresh);

        fStore = FirebaseFirestore.getInstance();

        String msg = getArguments().getString(KEY_FRG);
        String maxNum = getArguments().getString(KEY_FRG1);
        String currNum = getArguments().getString(KEY_FRG2);
        Integer i = Integer.parseInt(currNum);
        Integer m = Integer.parseInt(maxNum);
        // Get the message from Fragment 1

        if (msg != null && msg != ""){ // i = mobil yang msk, m = kapasitas max
            place.setText(msg);
            max.setText(maxNum);
            Integer pink = m/3;
            Integer merah = (m*2)/3;
            Integer merahtua = (m*2)/3;
            if(i <= pink){
                current.setText(currNum);
                current.setTextColor(getResources().getColor(R.color.merahmuda));
            }else if(i <= merah){
                //(i*2)/3 <= i
                current.setText(currNum);
                current.setTextColor(getResources().getColor(R.color.merah));
            }else{
                current.setText(currNum);
                current.setTextColor(getResources().getColor(R.color.merahtua));
            }

        } else {
            place.setText("-");
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (currentFragment instanceof DetailsFragment){
                    FragmentTransaction fragTransaction = (getActivity()).getSupportFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();
                }
            }
        });

        return v;
    }
}