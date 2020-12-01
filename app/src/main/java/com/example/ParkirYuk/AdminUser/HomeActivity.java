package com.example.ParkirYuk.AdminUser;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParkirYuk.R;
import com.example.ParkirYuk.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = "TAG";
    private HomeViewModel homeViewModel;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DrawerLayout drawer;
    NavigationView navigationView;

    private String adminID, userID;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.init();


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        menu = navigationView.getMenu();
        setSupportActionBar(toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_profile,R.id.nav_logout, R.id.nav_about_us,R.id.nav_feedback,R.id.nav_input)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

//      update navigation header
        updateNavHeader();

        if (fAuth.getCurrentUser() != null){
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_profile).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
            userAdmin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public void updateNavHeader(){
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);

        if (fAuth.getCurrentUser() != null) {
            navUsername.setVisibility(View.VISIBLE);
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference docRef = fStore.collection("users").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String userString = document.getString("Name");
                            navUsername.setText("Welcome, " + userString);
                        }else {
                            Log.d(TAG, "No such document");
                        }
                    }else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    public void userAdmin(){
        adminID = fAuth.getCurrentUser().getUid();
        if(adminID.equals("zpw7JrZMWrNZeGyJElriCPHnqDS2")){
            menu.findItem(R.id.nav_input).setVisible(true);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (item.getItemId()){
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
            case R.id.nav_login:
                navController.navigate(R.id.nav_login);
                break;
            case R.id.nav_logout:
                Toast.makeText(HomeActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                fAuth.signOut();
                finish();
                startActivity(getIntent());
                break;
            case R.id.nav_profile:
                navController.navigate(R.id.nav_profile);
                break;
            case R.id.nav_about_us:
                navController.navigate(R.id.nav_about_us);
                break;
            case R.id.nav_feedback:
                navController.navigate(R.id.nav_feedback);
                break;
            case R.id.nav_input:
                navController.navigate(R.id.nav_input);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}