package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.ClubListAdapter;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.example.kduclubandsociety.Clubs.TempListAdapter;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = DashboardActivity.this;
    private String uid;
    private String[] student_clubs_id;
    private List<Club> student_clubs;
    private ClubListAdapter temp; //rename as u see fit.

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef , mStudentRef, mClubRef;

    //Top Tabs
    private TextView topTitle;

    // Recycle View - Dash board
    RecyclerView dashView;

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
        myRef = mFirebaseDatabase.getReference();
        mStudentRef = myRef.child ("Student").child(uid);
        mClubRef = myRef.child ("Club");
        student_clubs = new ArrayList<Club>();
        ValueEventListener studentClubListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get string with club ids listed in it
                student_clubs_id = dataSnapshot.getValue(String.class).split(";");
                if(student_clubs_id != null) //if null means no clubs were added to this student
                {
                    //Firebase does not allow multiple condition queries e.g. filter by club ids 1,3, and 5.
                    //Two ways I would think of doing this is loading whole club list and then filter or load club from
                    //Firebase 1 by 1.
                    //Personally loading the whole club list is better in my opinion because
                    //it would be less likely to crash due to having too many listeners although it would take a bit more
                    //memory.
                    ValueEventListener loadClubListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                            //loop through all clubs that students have and add them to a list
                            for(DataSnapshot data:dataSnapshot.getChildren())
                            {
                                int club_id = data.child("id").getValue(int.class);
                                for(int i=0;i < student_clubs_id.length;i++)
                                {
                                    if(Integer.toString(club_id).equals(student_clubs_id[i]))
                                    {
                                        student_clubs.add(data.getValue(Club.class));
                                        break;
                                    }
                                }

                            }
                            TempListAdapter temp = new TempListAdapter(student_clubs,getApplicationContext(),uid, student_clubs_id);
                            dashView = findViewById(R.id.dashboardView);
                            dashView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                            dashView.setAdapter(temp);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mClubRef.addValueEventListener(loadClubListener);
                    //moved recyclerview loading here so that a list returning nothing wont crash your app.
                    //recycler view

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        //two different types .addValueEventListener & .addListenerForSingleValueEvent
        //One will only load the data from Firebase at Activity startup
        //The other one will reload the data when you change something in Firebase.
        mStudentRef.child("clubs").addListenerForSingleValueEvent(studentClubListener);

        myRef.keepSynced(true);


    }


    //set up card view
    @Override
    protected void onStart() {
        super.onStart();
        /*
        Commenting out since I made a custom recycler view list adapter
        FirebaseRecyclerAdapter<Club,MyHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Club, MyHolder>
                (Club.class, R.layout.dashboard_cardview,MyHolder.class,mClubRef) {

            @Override
            protected void populateViewHolder(MyHolder myHolder, Club club, int i) {
                myHolder.setTitle(club.getName());
                myHolder.setDesc(club.getDescription());
                myHolder.setImage(club.getImage());
                myHolder.id = club.getId();
                myHolder.uid = uid;
            }
        };
        dashView.setAdapter(firebaseRecyclerAdapter);

         */
    }

    // set up bottom navigation bar
    private void setupBottomNavigationView(){
        Log.d (TAG, "setupBottomNavigationView: setting up Bottom Navigation View");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView, uid, student_clubs_id);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
