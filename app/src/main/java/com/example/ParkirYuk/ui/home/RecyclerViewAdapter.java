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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "tag";
    private ArrayList<PlacesData> exampleList = new ArrayList<>();
    private ArrayList<PlacesData> exampleListFull = new ArrayList<>();
    private Context context;
    private Dialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String userID = fAuth.getCurrentUser().getUid();
    private String address;
    private String linkAddress(){
        return address;
    }

    public RecyclerViewAdapter (Context context, ArrayList<PlacesData> list, Dialog dialog){
        this.context = context;
        this.exampleList = list;
        this.dialog = dialog;
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
//        holder.itemView.setTag(exampleList.get(position));
        holder.places.setText(exampleList.get(position).getPlaces());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked item");
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("place_name", exampleList.get(position).getPlaces());
                intent.putExtra("place_max_num", exampleList.get(position).getMax());
                intent.putExtra("place_curr_num", exampleList.get(position).getCurrent());
                intent.putExtra("place_address_link", exampleList.get(position).getLink());
                intent.putExtra("place_id", exampleList.get(position).getId());
                intent.putExtra("place_user_id", exampleList.get(position).getUserID());
                intent.putExtra("place_address", exampleList.get(position).getAddress());
                address = exampleList.get(position).getLink();
                String places = exampleList.get(position).getPlaces();
                String historyID = UUID.randomUUID().toString();
                DocumentReference docref = db.collection("users")
                        .document(userID).collection("history").document(historyID);
                Map<String, Object> history = new HashMap<>();
                history.put("id", historyID);
                history.put("place", places);
                history.put("time", FieldValue.serverTimestamp());
                docref.set(history).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: history make");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: history fail");
                    }
                });
                dialog.dismiss();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

//    public Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<PlaceData> filteredList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0){
//                filteredList.addAll(exampleListFull);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for(PlaceData item : exampleListFull){
//                    if(item.getPlaces().toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            exampleList.clear();
//            exampleList.addAll((ArrayList)results.values);
//        }
//    };
//
//    @Override
//    public Filter getFilter() {
//        return exampleFilter;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView places;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            places = itemView.findViewById(R.id.NamaTempat);
        }
    }


}
