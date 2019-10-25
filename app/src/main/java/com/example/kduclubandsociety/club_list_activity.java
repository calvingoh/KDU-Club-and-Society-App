package com.example.kduclubandsociety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.kduclubandsociety.Class.Club;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class club_list_activity extends AppCompatActivity {

    ListView mlistview;
    FirebaseDatabase mdatabase;
    DatabaseReference mref;
    List<Club> clubList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list_activity);


        mlistview = findViewById(R.id.listview);
        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference("Club");

        clubList = new ArrayList<>();

    }
    @Override
    protected void onStart() {
        super.onStart();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubList.clear();
                for(DataSnapshot clubSnapshot: dataSnapshot.getChildren()){
                    Club club = clubSnapshot.getValue(Club.class);
                    clubList.add(club);
                }
                ClubList adapter = new ClubList(club_list_activity.this, clubList);
                mlistview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
