package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.Class.Member;
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
import java.util.List;

public class AttendanceDetails extends AppCompatActivity {
    private static final String TAG = "AttendanceDetails";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = AttendanceDetails.this;

    EditText txtTime;
    EditText txtLocation;
    FloatingActionButton btnEdit;
    Button btnSave;

    String currentUid;
    int clubId;
    String date, time, location;

    ListView mListview;
    MemberAdapter adp;
    List<Member> memberList;
    Member member;

    DatabaseReference ref, mClubRef, mStudentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_attendance_details);

        txtTime = findViewById(R.id.txtTime);
        txtLocation = findViewById(R.id.txtLocation);


        Intent intent = getIntent();
        clubId =intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");
        date =intent.getStringExtra("mtgDate");
        time =intent.getStringExtra("mtgTime");
        location =intent.getStringExtra("mtgLocation");

        setupBottomNavigationView();

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student").child(currentUid);

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(date);

        //listView
        mListview = findViewById(R.id.namelist);
        memberList= new ArrayList<>();

        //buttons
        btnSave = findViewById(R.id.btnRegister);
        btnEdit = findViewById(R.id.btnEdit);
//        btnSave.setVisibility(View.GONE);

        txtTime.setText (time);
        txtLocation.setText(location);

        checkPermission();
        addList();
    }

    void addList(){
        mClubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot pathSnapshot = dataSnapshot.child("attendance").child(date + " " + time).child("members");
                memberList.clear();

                for (int i = 0; i<pathSnapshot.getChildrenCount(); i++ ){
                    DataSnapshot memberSnapshot = pathSnapshot.child(Integer.toString(i));
                    member = memberSnapshot.getValue(Member.class);
                    memberList.add (member);
                }
                adp = new MemberAdapter(AttendanceDetails.this, memberList);
                mListview.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void checkPermission(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String admin =dataSnapshot.child("admin").getValue(String.class);
                if (admin.equals(currentUid)){
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

    public void onClick (View v){
        switch (v.getId()){
            case R.id.btnEdit:{
                btnSave.setVisibility(View.VISIBLE);
            }

            case R.id.btnSave :{
                btnSave.setVisibility(View.GONE);

                //save details
            }
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
