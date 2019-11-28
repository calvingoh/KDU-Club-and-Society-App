package com.example.kduclubandsociety.Clubs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Club_Profile extends AppCompatActivity {
    private static final String TAG = "ClubProfile";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = Club_Profile.this;

   // private TextView mNameTextView;
    private TextView mDescriptionTextView;
    private TextView mMeetingTextView;
    private TextView mMaxTextView;
    private ImageView mImage;
    private Button btnRegister;
    private FloatingActionButton btnEdit;


    String currentUid;
    int clubId;
    String clubName, clubDesc, clubMeeting, clubImage;
    int clubMax;
    String clubAdmin;
    String tempClubIds;
    String[] clubs;

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
        clubName = intent.getStringExtra("cName");
        clubDesc = intent.getStringExtra("cDescription");
        clubMeeting = intent.getStringExtra("cMeeting");
        clubMax = intent.getIntExtra("cMaxNum",0);
        clubImage = intent.getStringExtra("cImage");
        clubAdmin = intent.getStringExtra("cAdmin");
        currentUid = intent.getStringExtra("currentUid");

        setupBottomNavigationView();

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
        clubs = DashboardActivity.student_clubs_id;
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
            break;

            case R.id.btnEdit:{
                Intent intentEdit = new Intent(mContext, Club_ProfileEdit.class);
                intentEdit.putExtra("cId", clubId);
                intentEdit.putExtra("cName", clubName);
                intentEdit.putExtra("cDescription", clubDesc);
                intentEdit.putExtra("cMeeting", clubMeeting);
                intentEdit.putExtra("cMaxNum", clubMax);
                intentEdit.putExtra("cImage", clubImage);
                intentEdit.putExtra("cAdmin", clubAdmin);
                intentEdit.putExtra("currentUid", currentUid);
                startActivity(intentEdit);
            }
            break;
        }

    }

    void register () {
        //register student into club
        mStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempClubIds = dataSnapshot.child("clubs").getValue(String.class);
                mStudentRef.child("clubs").setValue(tempClubIds + ";" + clubId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    // set up bottom navigation bar
    private void setupBottomNavigationView(){
        Log.d (TAG, "setupBottomNavigationView: setting up Bottom Navigation View");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView,currentUid);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
