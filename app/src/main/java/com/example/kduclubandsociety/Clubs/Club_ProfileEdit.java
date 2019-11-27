package com.example.kduclubandsociety.Clubs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kduclubandsociety.Dashboard.AnnouncementDetailsEdit;
import com.example.kduclubandsociety.Dashboard.AttendanceDetailsEdit;
import com.example.kduclubandsociety.Dashboard.DashboardActivity;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Club_ProfileEdit extends AppCompatActivity {
    private static final String TAG = "Clubs_ProfileEdit";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = Club_ProfileEdit.this;

   // private TextView mNameTextView;
    private EditText mDescriptionTextView;
    private EditText mMeetingTextView;
    private EditText mMaxTextView;
    private ImageView mImage;
    private Button btnRegister;
    private FloatingActionButton btnEdit;


    String currentUid;
    int clubId, clubMax;
    String clubName, clubDesc, clubMeeting, clubImage, clubAdmin;
    String tempClubIds;
    String[] clubs;

    DatabaseReference ref, mClubRef, mStudentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_profile_edit);

        //initialization
        mDescriptionTextView = findViewById(R.id.DescriptiontextView);
        mMeetingTextView = findViewById(R.id.MeetingtextView);
        mMaxTextView = findViewById(R.id.MaxtextView);
        mImage = findViewById(R.id.clubImage);

        //intents
        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId",0);
        clubName = intent.getStringExtra("cName");
        clubDesc = intent.getStringExtra("cDescription");
        clubMeeting = intent.getStringExtra("cMeeting");
        clubMax = intent.getIntExtra("cMaxNum",0);
        clubImage = intent.getStringExtra("cImage");
        clubAdmin = intent.getStringExtra("cAdmin");
        currentUid = intent.getStringExtra("currentUid");


        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(clubName);

        //   mNameTextView.setText(clubName);
        mDescriptionTextView.setText(clubDesc);
        mMaxTextView.setText(Integer.toString(clubMax));
        mMeetingTextView.setText(clubMeeting);
        Picasso.get().load(clubImage).into(mImage);

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club");
        mStudentRef = ref.child("Student").child(currentUid);

        //buttons
        btnRegister = findViewById(R.id.btnRegister);
        btnEdit = findViewById(R.id.btnEdit);
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

    //save profile details
    void save(){
        clubDesc = mDescriptionTextView.getText().toString().trim();
        clubMeeting= mMeetingTextView.getText().toString().trim();
        clubMax= Integer.parseInt(mMaxTextView.getText().toString().trim());

        if (clubDesc.length()>0 && clubMeeting.length()>0 && clubMax>0){
            mClubRef.child(Integer.toString(clubId)).child("description").setValue(clubDesc);
            mClubRef.child(Integer.toString(clubId)).child("meeting").setValue(clubMeeting);
            mClubRef.child(Integer.toString(clubId)).child("maxNum").setValue(clubMax);

            Toast.makeText(Club_ProfileEdit.this, "Details edited", Toast.LENGTH_LONG).show();

            Intent intentClubDetails = new Intent(mContext, Club_Profile.class);
            intentClubDetails.putExtra("cId",clubId);
            intentClubDetails.putExtra("cName", clubName);
            intentClubDetails.putExtra("cDescription", clubDesc);
            intentClubDetails.putExtra("cMaxNum", clubMax);
            intentClubDetails.putExtra("cMeeting", clubMeeting);
            intentClubDetails.putExtra("cImage", clubImage);
            intentClubDetails.putExtra("cAdmin", clubAdmin);
            intentClubDetails.putExtra("currentUid",currentUid);
            startActivity (intentClubDetails);

        }

        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Club_ProfileEdit.this);
            dialog.setTitle("Oh no");
            dialog.setMessage("Do not leave the club profile details empty!");
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    //Save information
    public void onClick(View v){
        switch (v.getId()){

            case R.id.btnSave: {
                save();
            }
            break;

            case R.id.btnCancel:{
                Club_ProfileEdit.this.finish();
            }
            break;
        }

    }

    public void onBackPressed(){

    }
}
