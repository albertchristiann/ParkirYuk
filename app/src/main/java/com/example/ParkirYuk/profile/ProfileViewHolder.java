package com.example.ParkirYuk.profile;

import android.view.View;
import android.widget.TextView;

import com.example.ParkirYuk.R;

public class ProfileViewHolder {
    TextView historyNamePlace, historyTimestamp, historyCount;

    public ProfileViewHolder(View v){
        historyNamePlace = v.findViewById(R.id.namePlaceHistory);
        historyTimestamp = v.findViewById(R.id.timestamp);
        historyCount = v.findViewById(R.id.countHistory);
    }
}
