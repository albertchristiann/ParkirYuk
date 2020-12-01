package com.example.ParkirYuk.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkirYuk.AdminUser.HomeActivity;
import com.example.ParkirYuk.R;
import com.example.ParkirYuk.model.PlacesData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "tag";
    private HomeViewModel homeViewModel;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter.ViewHolder holder;

    TextView searchView;
    Dialog dialog;
    Integer count = 0;
    private OnDataAdded onDataAdded;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = v.findViewById(R.id.search);
        //init view model
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

                EditText editText = dialog.findViewById(R.id.edit_text);
                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);

                //init recycler view
                adapter = new RecyclerViewAdapter(getActivity(), homeViewModel.getData().getValue(), dialog);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<PlacesData>>() {
                    @Override
                    public void onChanged(ArrayList<PlacesData> placesData) {
                        adapter.notifyDataSetChanged();
                    }
                });


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
        return v;
    }

}