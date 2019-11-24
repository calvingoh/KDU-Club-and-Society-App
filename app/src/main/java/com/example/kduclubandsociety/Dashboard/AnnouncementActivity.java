package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.ClubListAdapter;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.example.kduclubandsociety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementActivity";
    private Context mContext = AnnouncementActivity.this;
    private FloatingActionButton btnAddAnnouncement;

    private TextView topTitle;

    String currentUid;
    int clubId;

    ListView aListview;
    AnnouncementAdapter adp;
    List<Announcement> announcementList;
    Announcement announcement;

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
        mClubRef= ref.child("Club").child(Integer.toString(clubId));

        //listView
        aListview = findViewById(R.id.announcement_list);
        announcementList= new ArrayList<>();

        //button
        btnAddAnnouncement = findViewById(R.id.btnAddAnnouncement);

        checkPermission();
        addListView();
        viewDetails();
    }

    // check whether student's permission
    void checkPermission(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").getValue(String.class).equals(currentUid)){
                    btnAddAnnouncement.show();
                }
                else {
                    btnAddAnnouncement.hide();
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
       intentAdd.putExtra("currentUid", currentUid);
       startActivity(intentAdd);
    }

    public void onClick(View v){
        if (v.getId()==R.id.btnAddAnnouncement){
            add();
        }
    }

    void addListView(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("announcement")){
                    DataSnapshot pathSnapshot = dataSnapshot.child("announcement").child("24").child("11");
                    announcementList.clear();

                    for (DataSnapshot annoSnapshot: pathSnapshot.getChildren() ){
                        announcement = annoSnapshot.getValue(Announcement.class);
                        announcementList.add (announcement);
                    }
                }
                adp = new AnnouncementAdapter(AnnouncementActivity.this, announcementList);
                aListview.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void viewDetails(){
        aListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentAnnDeets = new Intent (mContext,AnnouncementDetails.class);
                intentAnnDeets.putExtra("cId",clubId);
                intentAnnDeets.putExtra("currentUid", currentUid);
                intentAnnDeets.putExtra("annTitle", announcementList.get(position).getTitle());
                intentAnnDeets.putExtra("annBody", announcementList.get(position).getBody());
                intentAnnDeets.putExtra("annDate", announcementList.get(position).getDate());
                intentAnnDeets.putExtra("annUsername", announcementList.get(position).getUsername());
                startActivity(intentAnnDeets);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
