package com.example.ParkirYuk.ui.home;

import android.app.Dialog;
import android.content.Context;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParkirYuk.AdminUser.HomeActivity;
import com.example.ParkirYuk.model.HomeModel;
import com.example.ParkirYuk.model.PlacesData;
import com.example.ParkirYuk.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private static final String TAG = "tag";
    private HomeViewModel homeViewModel;
    private RecyclerViewAdapter adapter;
    TextView searchView;
    Dialog dialog;
    private OnDataAdded onDataAdded;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = v.findViewById(R.id.search);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.edit_text);
                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
                //init view model
                homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
                homeViewModel.init();

                Log.d(TAG, "onClick: "+homeViewModel.getData().getValue());
                //init recycler view
                adapter = new RecyclerViewAdapter(homeViewModel.getData().getValue());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);

                homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<HomeModel>>() {
                    @Override
                    public void onChanged(ArrayList<HomeModel> homeModels) {
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

//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                        PlacesData placeName = adapter.getItem(i);
////                        PlacesData max = note.get(i);
////                        PlacesData curr = note2.get(i);
////
////                        DetailsFragment secondFragtry = new DetailsFragment();
////                        Bundle mBundle = new Bundle();
////                        mBundle.putString(KEY_FRG, String.valueOf(placeName));
////                        mBundle.putString(KEY_FRG1, String.valueOf(max));
////                        mBundle.putString(KEY_FRG2, String.valueOf(curr));
////
////                        secondFragtry.setArguments(mBundle);
////
////                        FragmentManager mFragmentManager = getFragmentManager();
////                        FragmentTransaction mFragmentTransaction = mFragmentManager
////                                .beginTransaction()
////                                .replace(R.id.nav_host_fragment, secondFragtry, DetailsFragment.class.getSimpleName());
////                        mFragmentTransaction.addToBackStack(null).commit();
//
//                        dialog.dismiss();
//                    }
//                });
            }
        });
        return v;
    }

//    @Override
//    public void added() {
//        homeViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<PlacesData>>() {
//            @Override
//            public void onChanged(List<PlacesData> placesData) {
//                adapter.notifyDataSetChanged();
//            }
//        });
//    }
}