package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.ClubListAdapter;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = DashboardActivity.this;
    private String uid;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;

    //Top Tabs
    private TextView topTitle;

    // Recycle View - Dash board
    RecyclerView dashView;
    DashboardAdapter dashAdapter;

    ArrayList<Club> club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d (TAG, "onCreate: starting");

        Intent intent = getIntent();
        uid = intent.getStringExtra("currentUid");

        setupBottomNavigationView();

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Dashboard");

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                club = new ArrayList<>();
                for(DataSnapshot clubSnapshot: dataSnapshot.getChildren()){
                    Club c = new Club (clubSnapshot.getValue(Club.class).getId(),
                            clubSnapshot.getValue(Club.class).getName(),
                            clubSnapshot.getValue(Club.class).getDescription(),
                            clubSnapshot.getValue(Club.class).getMaxNum(),
                            clubSnapshot.getValue(Club.class).getMeeting(),
                            clubSnapshot.getValue(Club.class).getImage());
                    club.add(c);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //recycler view
        dashView = findViewById(R.id.dashboardView);
        dashView.setLayoutManager((new GridLayoutManager(this, 2)));
        dashAdapter = new DashboardAdapter(DashboardActivity.this,club );
        dashView.setAdapter(dashAdapter);

    }


    // set up bottom navigation bar
    private void setupBottomNavigationView(){
        Log.d (TAG, "setupBottomNavigationView: setting up Bottom Navigation View");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView, uid);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
