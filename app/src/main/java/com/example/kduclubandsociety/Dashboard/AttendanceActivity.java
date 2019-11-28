package com.example.kduclubandsociety.Dashboard;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementActivity";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = AttendanceActivity.this;

    private FloatingActionButton btnAddMeeting;

    String currentUid;
    int clubId;

    ListView aListview;
    AttendanceAdapter adp;
    ArrayList<Attendance> attendanceList;
    Attendance attendance;

    DatabaseReference ref, mClubRef, mStudentRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_attendance);

        //get intent
        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");

        setupBottomNavigationView();

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Attendance");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mStudentRef = ref.child("Student").child(currentUid);
        mClubRef= ref.child("Club").child(Integer.toString(clubId));

        //listView
        aListview = findViewById(R.id.attendance_dates);
        attendanceList= new ArrayList<>();

        //button
        btnAddMeeting = findViewById(R.id.btnAddMeeting);

        checkPermission();
        addMeetingList();
       // deleteMeeting();
        viewMeeting();

    }

    // check whether student's permission
    void checkPermission(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").getValue(String.class).equals(currentUid)){
                    btnAddMeeting.show();
                    deleteMeeting();
                }
                else {
                    btnAddMeeting.hide();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void addMeeting(){
        Intent intentAdd = new Intent(mContext, AttendanceAddActivity.class);
        intentAdd.putExtra("cId",clubId);
        intentAdd.putExtra("currentUid", currentUid);
        startActivity(intentAdd);
    }

    void addMeetingList(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("attendance")){
                    DataSnapshot pathSnapshot = dataSnapshot.child("attendance");
                    attendanceList.clear();

                    for (DataSnapshot annoSnapshot: pathSnapshot.getChildren() ){
                        attendance = annoSnapshot.getValue(Attendance.class);
                        attendanceList.add (attendance);
                    }
                }
                adp = new AttendanceAdapter(AttendanceActivity.this, attendanceList);
                aListview.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void viewMeeting(){
        aListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentAttend = new Intent (mContext,AttendanceDetails.class);
                intentAttend.putExtra("cId",clubId);
                intentAttend.putExtra("currentUid", currentUid);
                intentAttend.putExtra("mtgDate", attendanceList.get(position).getDate());
                intentAttend.putExtra("mtgTime", attendanceList.get(position).getTime());
                intentAttend.putExtra ("mtgLocation", attendanceList.get(position).getLocation());
                intentAttend.putExtra("mtgId", attendanceList.get(position).getId());
                startActivity(intentAttend);
            }
        });
    }

    void deleteMeeting (){
        aListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AttendanceActivity.this);
                dialog.setTitle("Delete Meeting?");
                dialog.setMessage("Are you sure you want to delete this meeting?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mClubRef.child("attendance").child(attendanceList.get(position).getId()).removeValue();
                        adp.notifyDataSetChanged();
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }


    public void onClick (View v){
        if (v.getId()==R.id.btnAddMeeting){
            addMeeting();
        }
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
