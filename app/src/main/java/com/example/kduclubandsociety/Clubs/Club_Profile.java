package com.example.kduclubandsociety.Clubs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kduclubandsociety.MainActivity;
import com.example.kduclubandsociety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Club_Profile extends AppCompatActivity {

    private TextView topTitle;

   // private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mMeetingTextView;
    private TextView mMaxTextView;
    private ImageView mImage;
    private Button btnRegister;
    private FloatingActionButton btnEdit;


    String currentUid;
    int clubId;

    DatabaseReference ref, mClubRef, mStudentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_profile);

        //initialization
        mDescriptionTextView = findViewById(R.id.DescriptiontextView);
        mMeetingTextView = findViewById(R.id.MeetingtextView);
        mMaxTextView = findViewById(R.id.MaxtextView);
        mImage = findViewById(R.id.clubImage);

        //intents
        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId",0);
        String clubName = intent.getStringExtra("cName");
        String clubDesc = intent.getStringExtra("cDescription");
        String clubMeeting = intent.getStringExtra("cMeeting");
        String clubMax = Integer.toString(intent.getIntExtra("cMaxNum",0));
        String clubImage = intent.getStringExtra("cImage");
        currentUid = intent.getStringExtra("currentUid");

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(clubName);

        //   mNameTextView.setText(clubName);
        mDescriptionTextView.setText(clubDesc);
        mMaxTextView.setText(clubMax);
        mMeetingTextView.setText(clubMeeting);
        Picasso.get().load(clubImage).into(mImage);

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club");
        mStudentRef = ref.child("Student").child(currentUid);

        //buttons
        btnRegister = findViewById(R.id.btnRegister);
        btnEdit = findViewById(R.id.btnEdit);

        checkRegistered();


    }

    // check whether student registered into the club or no and their permission
    void checkRegistered(){
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              if (dataSnapshot.hasChild("clubs")){
                  if (dataSnapshot.child("clubs").hasChild(Integer.toString(clubId))){
                      btnRegister.setVisibility(View.GONE);

                      if (dataSnapshot.child("clubs").child(Integer.toString(clubId))
                              .getValue(Boolean.class)== false){
                          btnEdit.hide();
                      }
                      else {
                          btnEdit.show();
                      }

                  }
                  else {
                      btnRegister.setVisibility(View.VISIBLE);
                      btnEdit.hide();
                  }
              }

              else {
                  btnRegister.setVisibility(View.VISIBLE);
                  btnEdit.hide();
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //REGISTER Student to Club
    public void onClick(View v){
        switch (v.getId()){

            case R.id.btnRegister: {
                //register student into club
                mStudentRef.child ("clubs").child(Integer.toString(clubId)).setValue(false)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Club_Profile.this, "Club Registered Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Club_Profile.this, ClubsActivity.class);
                                    intent.putExtra("currentUid", currentUid);
                                    startActivity(intent);
                                }

                                else{
                                    Toast.makeText(Club_Profile.this, "Data Failed to insert", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

            case R.id.btnEdit:{
                //edit and update
            }
        }




    }
}
