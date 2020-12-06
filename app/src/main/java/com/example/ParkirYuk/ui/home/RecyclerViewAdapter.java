package com.example.ParkirYuk.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkirYuk.Details.DetailsActivity;
import com.example.ParkirYuk.model.PlacesData;
import com.example.ParkirYuk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<PlacesData> exampleList = new ArrayList<>();
    ArrayList<String> checkArrayList = new ArrayList<>();
    private Context context;
    private Dialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String userID, address;
    private String linkAddress(){
        return address;
    }
    private ArrayList<Integer> checkNumber = new ArrayList<>();
    private int check;
    int b;

    public RecyclerViewAdapter (Context context, ArrayList<PlacesData> list, Dialog dialog, int count){
        this.context = context;
        this.exampleList = list;
        this.dialog = dialog;
        this.check = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.places.setText(exampleList.get(position).getPlaces());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("place_name", exampleList.get(position).getPlaces());
                intent.putExtra("place_max_num", exampleList.get(position).getMax());
                intent.putExtra("place_curr_num", exampleList.get(position).getCurrent());
                intent.putExtra("place_address_link", exampleList.get(position).getLink());
                intent.putExtra("place_id", exampleList.get(position).getId());
                intent.putExtra("place_user_id", exampleList.get(position).getUserID());
                intent.putExtra("place_address", exampleList.get(position).getAddress());
                address = exampleList.get(position).getLink();

//                Log.d(TAG, "onClick: repeat on click ");
                //flow history
                //1. klo gk ada data bikin baru, klo ada di validasi berdasarkan datenya
                //2. untuk validasi perlu menarik semua history id yg ada karena mau di cek date nya masing"
                if (fAuth.getCurrentUser() != null) {
                    String places = exampleList.get(position).getPlaces();
                    userID = fAuth.getCurrentUser().getUid();
                    Date date = new Date();
                    long time = date.getTime();
                    Timestamp ts = new Timestamp(time);
                    Map<String, Object> history = new HashMap<>();
//                    String historyID = UUID.randomUUID().toString();
//                    Log.d(TAG, "onClick: "+String.valueOf(date.getDate())+String.valueOf(date.getMonth())+exampleList.get(position).getPlaces().trim());

                    CollectionReference docref = db.collection("users")
                            .document(userID).collection("history");

                    //klo ada dokumen
                    Log.d(TAG, "onClick: count"+check);
                    if (check==0) {
                        //klo dokumennya gk ada baru dibuat baru
//                        history.put("id", historyID);
                        history.put("place", places);
                        history.put("time", ts);
                        docref.add(history).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "onSuccess: success add history");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: history fail");
                            }
                        });
                    } else{
                        //klo dokumennya ada baru deh validasi
                        //fetch setiap history id yg ada
//                        Log.d(TAG, "onClick: else check");
                        int[] dummy = {0};
                        final int[] count = new int[1];
//                        final int pop;
                        final ArrayList<String> checkContent = new ArrayList<>();
                        db.collection("users").document(userID)
                                .collection("history")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists() && document!=null) {
                                            count[0] = count[0] +1;
                                            com.google.firebase.Timestamp timest = (com.google.firebase.Timestamp) document.getData().get("time");
                                            int doll = timest.toDate().getDate();
//                                            Date dateB = new Date();
//                                            long timeB = dateB.getTime();
//                                            Timestamp tsB = new Timestamp(timeB);
                                            b = ts.getDate();
//                                            Log.d(TAG, "onComplete: "+b+">"+doll);
//                                            Log.d(TAG, "onComplete: document "+document.getString("place")+" "+exampleList.get(position).getPlaces());

                                            if (b==doll && exampleList.get(position).getPlaces().equals(document.getString("place"))) {
                                                dummy[0] = dummy[0]+1;
//                                                Log.d(TAG, "onComplete: dummy count "+ dummy[0]);
                                            }

                                            if(b==doll && !exampleList.get(position).getPlaces().equals(document.getString("place"))){
                                                checkContent.add(places);
//                                                Log.d(TAG, "onComplete: content "+checkContent.get(0));
                                            }

//                                            Log.d(TAG, "onComplete: count "+count[0]);
//                                            Log.d(TAG, "onComplete: max "+task.getResult().size());
//                                            Log.d(TAG, "onComplete: array list"+checkContent);
                                            if(count[0]==task.getResult().size()) {
                                                if (b >= doll && dummy[0] == 0) {
                                                    if(checkContent.isEmpty()){
                                                        Log.d(TAG, "onComplete: empty array");
                                                    }else {
                                                        if (exampleList.get(position).getPlaces().equals(checkContent.get(0))) {
//                                                            Map<String, Object> historyB = new HashMap<>();
//                                                            history.put("id", historyID);
                                                            history.put("place", places);
                                                            history.put("time", ts);
                                                            docref.add(history).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d(TAG, "onSuccess: history make");
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d(TAG, "onFailure: history fail");
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: history fail");
                            }
                        });


                    }
                }
                dialog.dismiss();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    public void filterList(ArrayList<PlacesData> filteredList){
        exampleList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView places;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            places = itemView.findViewById(R.id.NamaTempat);
        }
    }
}