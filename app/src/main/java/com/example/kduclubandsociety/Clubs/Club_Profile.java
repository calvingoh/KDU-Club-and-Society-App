package com.example.kduclubandsociety.Clubs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kduclubandsociety.Dashboard.DashboardActivity;
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
    String clubAdmin;
    String tempClubIds;

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
        clubAdmin = intent.getStringExtra("cAdmin");
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

        checkPermission();
        checkRegistered();
    }

    void checkPermission(){
        mClubRef.child(Integer.toString(clubId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").getValue(String.class).equals(currentUid)){
                    btnEdit.show();
                }

                else{
                    btnEdit.hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // check whether student registered into the club or no and their permission
    void checkRegistered(){
        String[] clubs = DashboardActivity.student_clubs_id;
        for(int i = 0; i < clubs.length; i++) {
            if(Integer.toString(clubId).equals(clubs[i])) {
                btnRegister.setVisibility(View.GONE);
                return;
            }
            btnRegister.setVisibility(View.VISIBLE);
        }
    }


    //REGISTER Student to Club
    public void onClick(View v){
        switch (v.getId()){

            case R.id.btnRegister: {
                confirmRegister();
            }

            case R.id.btnEdit:{
                //edit and update codes
            }
        }

    }

    void register () {
        //register student into club
        mStudentRef.child("clubs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempClubIds = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStudentRef.child("clubs").setValue(tempClubIds + ";" + clubId);
        Toast.makeText(Club_Profile.this, "Club Registered Successfully", Toast.LENGTH_LONG).show();
        Club_Profile.this.finish();
    }

    void confirmRegister(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(Club_Profile.this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Do you want to register into this club?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                register();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
