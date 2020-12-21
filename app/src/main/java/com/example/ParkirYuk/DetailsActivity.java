package com.example.ParkirYuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.AdminUser.HomeActivity;
import com.example.ParkirYuk.R;
import com.example.ParkirYuk.model.PlacesData;
import com.example.ParkirYuk.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private ArrayList<PlacesData> arrayList = new ArrayList<>();
    TextView place,current,max, address;
    ImageView back;
    ImageButton refresh;
    String placeName, placeAddressLink, userID, placeID, placeAddress;
    Integer maxNum, currNum;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        place = findViewById(R.id.place);
        current = findViewById(R.id.current);
        max = findViewById(R.id.max);
        back = findViewById(R.id.backButton);
        refresh = findViewById(R.id.refresh);
        address = findViewById(R.id.addressLink);

        placeName = getIntent().getStringExtra("place_name");
        maxNum = getIntent().getIntExtra("place_max_num", 0);
        currNum = getIntent().getIntExtra("place_curr_num", 0);
        placeAddressLink = getIntent().getStringExtra("place_address_link");
        placeID = getIntent().getStringExtra("place_id");
        userID = getIntent().getStringExtra("place_user_id");
        placeAddress = getIntent().getStringExtra("place_address");
        Log.d(TAG, "onCreate: if "+placeName+" "+maxNum+" "+currNum);
        Log.d(TAG, "onCreate: "+placeID);
        Log.d(TAG, "onCreate: "+maxNum+" "+currNum+" "+placeName);
//        Integer i = Integer.parseInt(currNum);
//        Integer m = Integer.parseInt(maxNum);
//        // Get the message from adapterText1.setText(
//        Html.fromHtml(
//            "<a href=\"http://www.google.com\">google</a> "));
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(placeAddressLink);
                Intent a = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(a);
            }
        });
        address.setText(placeAddress);

        validation();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();

//                userID = fStore.collection("users").document(fAuth.getUid())
//                        .collection("place").document(placeID).getId();
//                Query a = fStore.collectionGroup("places").whereEqualTo("id", placeID);
//                Log.d(TAG, "onClick: "+a);
//                String test = fStore.collection("users").document(userID).collection("places")
//                        .document(placeID).getPath();
//                Log.d(TAG, "onClick: "+test);
//                Log.d(TAG, "onClick: "+userID);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void refreshData(){
        fStore.collection("users").document(userID)
                .collection("places").document(placeID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists() && document != null){
                        currNum = Integer.parseInt(String.valueOf(document.get("current")));
                        validation();
                        Toast.makeText(DetailsActivity.this, "Refresh Succesfull", Toast.LENGTH_SHORT).show();
                        current.setText(String.valueOf(currNum));
                    }else{
                        Log.d(TAG, "onComplete: error refresh"+document.getData());
                    }
                }else {
                    Log.d(TAG, "onComplete: error refresh succesful");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: refresh");
            }
        });
    }

    private void validation(){
        if (placeName != null && placeName != ""){ // i = mobil yang msk, m = kapasitas max
            place.setText(placeName);
            max.setText(String.valueOf(maxNum));
            Integer pink = maxNum/3;
            Integer merah = (maxNum*2)/3;
            Integer merahtua = (maxNum*2)/3;
            if(currNum <= pink){
                current.setText(String.valueOf(currNum));
                current.setTextColor(getResources().getColor(R.color.merahmuda));
            }else if(currNum <= merah){
                //(i*2)/3 <= i
                current.setText(String.valueOf(currNum));
                current.setTextColor(getResources().getColor(R.color.merah));
            }else{
                current.setText(String.valueOf(currNum));
                current.setTextColor(getResources().getColor(R.color.merahtua));
            }
        } else {
            place.setText("-");
            current.setText("0");
            max.setText("0");
        }
    }
}