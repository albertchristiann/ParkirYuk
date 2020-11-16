package com.example.ParkirYuk.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkirYuk.model.HomeModel;
import com.example.ParkirYuk.model.PlacesData;
import com.example.ParkirYuk.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "tag";
    private ArrayList<HomeModel> exampleList = new ArrayList<>();
    private ArrayList<HomeModel> exampleListFull = new ArrayList<>();
    private String placeName;
    private Context context;

    public RecyclerViewAdapter (ArrayList<HomeModel> list){
//        this.context = context;
        this.exampleList = list;
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
        holder.itemView.setTag(exampleList.get(position));
        holder.places.setText(exampleList.get(position).getPlaces());
        Log.d(TAG, "onBindViewHolder: data Added");
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

//    public Filter exampleFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<HomeModel> filteredList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0){
//                filteredList.addAll(exampleListFull);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for(HomeModel item : exampleListFull){
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
