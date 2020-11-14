package com.example.ParkirYuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<PlacesData> exampleList;
    private ArrayList<PlacesData> exampleListFull;
    private String placeName;
    private Context context;

    public RecyclerViewAdapter (ArrayList<PlacesData> exampleList){
//        this.context = context;
        this.exampleList = exampleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(exampleList.get(position));
        holder.places.setText(exampleList.get(position).getPlaces());

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

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PlacesData> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(exampleListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(PlacesData item : exampleListFull){
                    if(item.getPlaces().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView places;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            places = itemView.findViewById(R.id.NamaTempat);
            cardView = itemView.findViewById(R.id.parent_layout);
        }
    }
}
