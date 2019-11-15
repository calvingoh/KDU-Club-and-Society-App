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
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = DashboardActivity.this;
    private String uid;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    //Top Tabs
    private TextView topTitle;

    // Recycle View - Dash board
    RecyclerView dashView;
    DashboardAdapter dashAdapter;

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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Club");
        myRef.keepSynced(true);



        //recycler view
        dashView = findViewById(R.id.dashboardView);
        //dashView.setHasFixedSize(true);
        dashView.setLayoutManager(new GridLayoutManager(this,2));
      //  dashAdapter = new DashboardAdapter(DashboardActivity.this,clubList );
      //  dashView.setAdapter(dashAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Club,MyHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Club, MyHolder>
                (Club.class, R.layout.dashboard_cardview,MyHolder.class,myRef) {
            @Override
            protected void populateViewHolder(MyHolder myHolder, Club club, int i) {
                myHolder.setTitle(club.getName());
                myHolder.setDesc(club.getDescription());
                myHolder.setImage(club.getImage());
            }
        };

        dashView.setAdapter(firebaseRecyclerAdapter);
    }



    /*
    @Override
    protected void onStart() {
        super.onStart();
        clubList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //club = new ArrayList<>();
                for(DataSnapshot clubSnapshot: dataSnapshot.getChildren()){
                    club = clubSnapshot.getValue(Club.class);
                    clubList.add(club);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
     */


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
