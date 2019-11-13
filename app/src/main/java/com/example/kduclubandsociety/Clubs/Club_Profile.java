package com.example.kduclubandsociety.Clubs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Club_Profile extends AppCompatActivity {

    private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mMeetingTextView;
    private TextView mMaxTextView;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club__profile);

        mNameTextView = findViewById(R.id.NametextView);
        mDescriptionTextView = findViewById(R.id.DescriptiontextView);
        mMeetingTextView = findViewById(R.id.MeetingtextView);
        mMaxTextView = findViewById(R.id.MaxtextView);

        Intent intent = getIntent();
        String clubName = intent.getStringExtra("cName");
        String clubDesc = intent.getStringExtra("cDescription");
        String clubMeeting = intent.getStringExtra("cMeeting");
        String clubMax = Integer.toString(intent.getIntExtra("cMaxNum",0));


        mNameTextView.setText(clubName);
        mDescriptionTextView.setText(clubDesc);
        mMaxTextView.setText(clubMax);
        mMeetingTextView.setText(clubMeeting);

/*
        ref = FirebaseDatabase.getInstance().getReference().child("Club").child("1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String description = dataSnapshot.child("description").getValue().toString();
                String meeting = dataSnapshot.child("meeting").getValue().toString();
               // String max = dataSnapshot.child("max").getValue().toString();

                mNameTextView.setText(name);
                mDescriptionTextView.setText(description);
                mMeetingTextView.setText(meeting);
              //  mMaxTextView.setText(max);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */
    }
}
