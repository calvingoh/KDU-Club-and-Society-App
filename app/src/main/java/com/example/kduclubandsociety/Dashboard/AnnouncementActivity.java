package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Class.Student;
import com.example.kduclubandsociety.Clubs.ClubListAdapter;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.example.kduclubandsociety.Notification.GenerateNotification;
import com.example.kduclubandsociety.Notification.NotificationActivity;
import com.example.kduclubandsociety.Notification.NotificationReciever;
import com.example.kduclubandsociety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementActivity";
    private Context mContext = AnnouncementActivity.this;
    private FloatingActionButton btnAddAnnouncement;

    public static final String CHANNEL_1_ID = "Channel1";
    private static final String CHANNEL_NAME = "KDU";
    private static final String CHANNEL_DESC = "KDU NOTIFICATION";

    private TextView topTitle;

    String currentUid;
    int clubId;

    ListView aListview;
    AnnouncementAdapter adp;
    List<Announcement> announcementList;
    Announcement announcement;

    DatabaseReference ref, mClubRef, mStudentRef;

    private EditText mtitle;
    private EditText mbody;
    private Button msavebtn;

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
        msavebtn = findViewById(R.id.btnSave);


        //edittext
        mtitle = findViewById(R.id.txtAncTitle);
        mbody = findViewById(R.id.txtBody);

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

    void addListView(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("announcement")){
                    DataSnapshot pathSnapshot = dataSnapshot.child("announcement").child("23").child("11");
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



    @Override
    protected void onStart() {
        super.onStart();
        addListView();
    }
}
