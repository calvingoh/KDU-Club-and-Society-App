package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.TempListAdapter;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = DashboardActivity.this;
    private String uid;
    public static String[] student_clubs_id;
    private List<Club> student_clubs;
    private TempListAdapter temp;

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
                if(student_clubs_id != null) //if theres clubs were added to this student
                {
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
                            temp = new TempListAdapter(student_clubs,mContext,uid, student_clubs_id);
                            dashView = findViewById(R.id.dashboardView);
                            dashView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                            dashView.setAdapter(temp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mClubRef.addValueEventListener(loadClubListener);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mStudentRef.child("clubs").addListenerForSingleValueEvent(studentClubListener);
        myRef.keepSynced(true);
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

    @Override
    public void onBackPressed() {

    }
}
