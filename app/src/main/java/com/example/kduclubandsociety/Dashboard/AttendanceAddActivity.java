package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.Class.Member;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAddActivity extends AppCompatActivity {
    private static final String TAG = "AttendanceAddActivity";
    private Context mContext = AttendanceAddActivity.this;

    EditText date;
    EditText time;
    EditText location;

    String mtgDate;
    String mtgTime;
    String mtgLocation;

    DatabaseReference ref, mClubRef, mStudentRef;
    int clubId;
    String currentUid;

   String[] student_club_id;
   ArrayList<Member> memberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_attendance_add);

        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId", 0);
        currentUid = intent.getStringExtra("currentUid");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student");
        memberList = new ArrayList<Member>();

        //initialization
        date = findViewById(R.id.txtMtgDate);
        time = findViewById(R.id.txtMtgTime);
        location =findViewById(R.id.txtMtgLocation);

        getMembers();

    }

    public void onClick (View v){
        switch (v.getId()){
            case R.id.btnSave:{
                save();
            }

            case R.id.btnCancel:{
                AttendanceAddActivity.this.finish();
            }
        }
    }

    void save(){
        mtgDate=date.getText().toString().trim();
        mtgTime= time.getText().toString().trim();
        mtgLocation=location.getText().toString().trim();

        Attendance attendance = new Attendance();
        attendance.setDate(mtgDate);
        attendance.setTime(mtgTime);
        attendance.setLocation(mtgLocation);
        attendance.setMembers(memberList);

        mClubRef.child("attendance").child(mtgDate+ " " + mtgTime).setValue(attendance);
        Toast.makeText(AttendanceAddActivity.this, "Meeting added", Toast.LENGTH_LONG).show();

        AttendanceAddActivity.this.finish();
    }

    void getMembers(){
       ValueEventListener studentListener = new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot data : dataSnapshot.getChildren()){
                   student_club_id = data.child("clubs").getValue(String.class).split(";");
                   for (int i = 0; i<student_club_id.length; i++){
                       if (Integer.toString(clubId).equals(student_club_id[i])){
                           Member member = new Member();
                           member.setUid(data.child("uid").getValue(String.class));
                           member.setPresent(false);
                           member.setName(data.child("name").getValue(String.class));
                           memberList.add (member);
                       }
                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
       mStudentRef.addListenerForSingleValueEvent(studentListener);
    }
}
