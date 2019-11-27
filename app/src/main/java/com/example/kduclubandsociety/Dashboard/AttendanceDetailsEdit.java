package com.example.kduclubandsociety.Dashboard;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceDetailsEdit extends AppCompatActivity {
    private static final String TAG = "AttendanceDetailsEdit";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = AttendanceDetailsEdit.this;

    EditText txtTime;
    EditText txtLocation;
    EditText txtDate;
    Button btnSave;

    String currentUid;
    int clubId;
    String date, time, location, id;

    ListView mListview;
    MemberAdapter adp;
    List<Member> memberList;
    Member member;

    DatabaseReference ref, mClubRef, mStudentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_attendance_edit);

        txtTime = findViewById(R.id.txtTime);
        txtLocation = findViewById(R.id.txtLocation);
        txtDate=findViewById(R.id.txtDate);


        Intent intent = getIntent();
        clubId =intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");
        date =intent.getStringExtra("mtgDate");
        time =intent.getStringExtra("mtgTime");
        location =intent.getStringExtra("mtgLocation");
        id = intent.getStringExtra("mtgId");

        //setupBottomNavigationView();

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student").child(currentUid);

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Attendance");

        //listView
        mListview = findViewById(R.id.namelist);
        memberList= new ArrayList<>();

        //buttons
        btnSave = findViewById(R.id.btnSave);
//        btnSave.setVisibility(View.GONE);

        txtTime.setText (time);
        txtLocation.setText(location);
        txtDate.setText (date);

        addList();
    }

    void addList(){
        mClubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot pathSnapshot = dataSnapshot.child("attendance").child(id).child("members");
                memberList.clear();

                for (int i = 0; i<pathSnapshot.getChildrenCount(); i++ ){
                    DataSnapshot memberSnapshot = pathSnapshot.child(Integer.toString(i));
                    member = memberSnapshot.getValue(Member.class);
                    memberList.add (member);
                }
                adp = new MemberAdapter(AttendanceDetailsEdit.this, memberList);
                mListview.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void onClick (View v){
        switch (v.getId()){
            case R.id.btnSave :{
                date = txtDate.getText().toString().trim();
                time = txtTime.getText().toString().trim();
                location = txtLocation.getText().toString().trim();

                if (date.length()==0 || time.length()==0 || location.length()==0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AttendanceDetailsEdit.this);
                    dialog.setTitle("Oh no");
                    dialog.setMessage("Do not leave the details empty!");
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

                else{
                    mClubRef.child("attendance").child (id).child("date").setValue(date);
                    mClubRef.child("attendance").child (id).child("time").setValue(time);
                    mClubRef.child("attendance").child (id).child("location").setValue(location);
                    Toast.makeText(AttendanceDetailsEdit.this, "Meeting edited", Toast.LENGTH_LONG).show();

                    Intent intentAttend = new Intent (mContext,AttendanceDetails.class);
                    intentAttend.putExtra("cId",clubId);
                    intentAttend.putExtra("currentUid", currentUid);
                    intentAttend.putExtra("mtgDate",date);
                    intentAttend.putExtra("mtgTime", time);
                    intentAttend.putExtra ("mtgLocation",location);
                    intentAttend.putExtra("mtgId", id);
                    startActivity(intentAttend);
                }
            }

            case R.id.btnCancel:{
                AttendanceDetailsEdit.this.finish();
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

    public void onBackPressed(){

    }
}
