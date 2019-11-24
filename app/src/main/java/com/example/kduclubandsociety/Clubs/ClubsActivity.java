package com.example.kduclubandsociety.Clubs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ClubsActivity extends AppCompatActivity {
    private static final String TAG = "ClubsActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = ClubsActivity.this;
    private String uid;
    private String[] student_club_id;
    private TextView topTitle;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    //Listview
    ClubListAdapter adapter;
    ListView mlistview;
    List<Club> clubList;
    Club club;

    //
    int pos;

    //

    private SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);

        Intent intent = getIntent();
        uid = intent.getStringExtra("currentUid");
        student_club_id = intent.getStringArrayExtra("student_clubId");


        setupBottomNavigationView();

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Clubs");

        //firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Club");

        //listview
        mlistview = findViewById(R.id.listview2);
        clubList = new ArrayList<>();

        //search filter
        sv= findViewById(R.id.searchFilter);

        clubDetails();
        searchView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubList.clear();
                for(DataSnapshot clubSnapshot: dataSnapshot.getChildren()){
                    club = clubSnapshot.getValue(Club.class);
                    clubList.add(club);
                }
                adapter = new ClubListAdapter(ClubsActivity.this, clubList);
                mlistview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void searchView (){

        sv.setQueryHint("Search Club and Society");

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();

                return false;
            }
        });
    }

    //move to club activity
    void clubDetails (){
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Intent intentClubDetails = new Intent(mContext, Club_Profile.class);
                intentClubDetails.putExtra("cId",clubList.get(pos).getId());
                intentClubDetails.putExtra("cName", clubList.get(pos).getName());
                intentClubDetails.putExtra("cDescription", clubList.get(pos).getDescription());
                intentClubDetails.putExtra("cMaxNum", clubList.get(pos).getMaxNum());
                intentClubDetails.putExtra("cMeeting", clubList.get(pos).getMeeting());
                intentClubDetails.putExtra("cImage", clubList.get(pos).getImage());
                intentClubDetails.putExtra("cAdmin", clubList.get(pos).getAdmin());
                intentClubDetails.putExtra("currentUid",uid);
                intentClubDetails.putExtra("student_clubId",student_club_id);
                startActivity (intentClubDetails);

            }
        });
    }

    // set up bottom navigation bar
    private void setupBottomNavigationView(){
        Log.d (TAG, "setupBottomNavigationView: setting up Bottom Navigation View");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView,uid, student_club_id);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
