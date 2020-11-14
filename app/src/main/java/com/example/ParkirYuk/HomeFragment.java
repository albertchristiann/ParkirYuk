package com.example.ParkirYuk;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    private static final String TAG = "tag";
    private HomeViewModel homeViewModel;
    TextView searchView;
    Dialog dialog;
    private RecyclerViewAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = v.findViewById(R.id.search);

        //init view model
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        homeViewModel.init();

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                //init recycler view
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerViewAdapter(homeViewModel.getData().getValue());
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
                        adapter.getFilter().filter(charSequence);
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

//    public void data(){
//        arrayList = new ArrayList<PlacesData>();
//        note = new ArrayList<>();
//        note2 = new ArrayList<>();
//
//        fStore.collection("places").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
//                if(e != null){
//                    Log.d(TAG, "onEvent: "+ e.getMessage());
//                }else{
//                    arrayList.clear();
//                    for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        arrayList.add(snapshot.getString("place"));
//                        note.add(snapshot.getString("max"));
//                        note2.add(snapshot.getString("current"));
//                    }
//                }
//            }
//        });
//    }

}