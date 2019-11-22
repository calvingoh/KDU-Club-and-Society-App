package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AnnouncementActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementActivity";
    private Context mContext = AnnouncementActivity.this;
    private FloatingActionButton btnAddAnnouncement;

    private TextView topTitle;

    String currentUid;
    int clubId;

    ListView announcementsList;

    DatabaseReference ref, mClubRef, mStudentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement);

        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Announcement");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mStudentRef = ref.child("Student").child(currentUid);

        //button
        btnAddAnnouncement = findViewById(R.id.btnAddAnnouncement);
        checkPermission();
    }

    // check whether student's permission
    void checkPermission(){
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("clubs").child(Integer.toString(clubId))
                        .getValue(Boolean.class)== false){
                    btnAddAnnouncement.hide();
                }
                else {
                    btnAddAnnouncement.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void add(){
       Intent intentAdd = new Intent(mContext, AnnouncementAddActivity.class);
       intentAdd.putExtra("cId",clubId);
       startActivity(intentAdd);
    }

    public void onClick(View v){

        if (v.getId()==R.id.btnAddAnnouncement){
            add();
        }
    }
}
