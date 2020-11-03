package com.example.ParkirYuk.ui.home;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ParkirYuk.DetailsFragment;
import com.example.ParkirYuk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class HomeFragment extends Fragment {
    private static final String TAG = "tag";
    private HomeViewModel homeViewModel;
    TextView textView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Dialog dialog;
    Button button;
    FirebaseFirestore fStore;
    public static String KEY_FRG = "msg_fragment";
    public static String KEY_FRG1 = "msg_fragment1";
    public static String KEY_FRG2 = "msg_fragment2";
    public ArrayList<String> note;
    public ArrayList<String> note2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel =
        //        ViewModelProviders.of(this).get(HomeViewModel.class);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        fStore = FirebaseFirestore.getInstance();

        //final TextView textView = v.findViewById(R.id.text_view);
        //homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
        // @Override
        //public void onChanged(@Nullable String s) {
        //   textView.setText(s);
        //}
        //});

        textView = v.findViewById(R.id.text_view);
        button = v.findViewById(R.id.btn_check);
        data();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(1000,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, arrayList);

                listView.setAdapter(adapter);

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


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String placeName = adapter.getItem(i);
                        String max = note.get(i);
                        String curr = note2.get(i);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("PlaceName",placeName);

                        DetailsFragment secondFragtry = new DetailsFragment();
                        Bundle mBundle = new Bundle();
                        mBundle.putString(KEY_FRG,placeName);
                        mBundle.putString(KEY_FRG1, max);
                        mBundle.putString(KEY_FRG2, curr);

                        secondFragtry.setArguments(mBundle);

                        FragmentManager mFragmentManager = getFragmentManager();
                        FragmentTransaction mFragmentTransaction = mFragmentManager
                                .beginTransaction()
                                .replace(R.id.nav_host_fragment, secondFragtry, DetailsFragment.class.getSimpleName());
                        mFragmentTransaction.addToBackStack(null).commit();

                        dialog.dismiss();
                    }
                });
            }
        });

        return v;
    }

    public void data(){
        arrayList = new ArrayList<>();
        note = new ArrayList<>();
        note2 = new ArrayList<>();

        fStore.collection("places").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                arrayList.clear();
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    arrayList.add(snapshot.getString("place"));
                    note.add(snapshot.getString("max"));
                    note2.add(snapshot.getString("current"));
                }
            }
        });
    }
}