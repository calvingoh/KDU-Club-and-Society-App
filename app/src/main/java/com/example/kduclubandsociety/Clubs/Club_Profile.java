package com.example.kduclubandsociety.Clubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class Club_Profile extends AppCompatActivity {

    private TextView topTitle;

   // private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mMeetingTextView;
    private TextView mMaxTextView;
    private ImageView mImage;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_profile);



      //  mNameTextView = findViewById(R.id.NametextView);
        mDescriptionTextView = findViewById(R.id.DescriptiontextView);
        mMeetingTextView = findViewById(R.id.MeetingtextView);
        mMaxTextView = findViewById(R.id.MaxtextView);
        mImage = findViewById(R.id.clubImage);

        Intent intent = getIntent();
        String clubName = intent.getStringExtra("cName");
        String clubDesc = intent.getStringExtra("cDescription");
        String clubMeeting = intent.getStringExtra("cMeeting");
        String clubMax = Integer.toString(intent.getIntExtra("cMaxNum",0));
        String clubImage = intent.getStringExtra("cImage");


        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(clubName);

     //   mNameTextView.setText(clubName);
        mDescriptionTextView.setText(clubDesc);
        mMaxTextView.setText(clubMax);
        mMeetingTextView.setText(clubMeeting);
        Picasso.get().load(clubImage).into(mImage);

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
