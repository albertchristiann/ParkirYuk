package com.example.ParkirYuk.profie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ParkirYuk.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    private static final String TAG = "MyAdapter";
    Context context;
    ArrayList<String> hName = new ArrayList<>(), hTimestamp = new ArrayList<>();
    Integer count = 0;

    public MyAdapter (Context context, ArrayList<String> name, ArrayList<String> time){
        super(context, R.layout.row, R.id.namePlaceHistory, name);
        this.context = context;
        this.hName = name;
        this.hTimestamp = time;
        Log.d(TAG, "MyAdapter: called");
        Log.d(TAG, "MyAdapter: "+hName);
        Log.d(TAG, "MyAdapter: "+ name);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        ProfileViewHolder holder = null;
        Log.d(TAG, "getView: "+singleItem);
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.row, parent, false);
            holder = new ProfileViewHolder(singleItem);
            singleItem.setTag(holder);
            count = count + 1;
        } else {
            holder = (ProfileViewHolder) singleItem.getTag();
            count = count + 1;
        }
        holder.historyNamePlace.setText(hName.get(position));
        holder.historyTimestamp.setText(hTimestamp.get(position));
        Log.d(TAG, "getView: "+hTimestamp.get(position));
        holder.historyCount.setText(String.valueOf(count));

        return singleItem;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
