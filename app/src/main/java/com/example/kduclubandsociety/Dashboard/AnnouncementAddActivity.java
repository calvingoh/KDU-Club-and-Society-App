package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.MainActivity;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AnnouncementAddActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementAddActivity";
    private Context mContext = AnnouncementAddActivity.this;

    EditText txtTitle;
    EditText txtBody;
    String ancTitle;
    String ancBody;
    String username;

    DatabaseReference ref, mClubRef, mStudentRef;
    int clubId;
    String currentUid;


    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Calendar c;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement_add);

        Intent intent = getIntent();
        clubId =intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");


        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student").child(currentUid);


        txtTitle= findViewById(R.id.txtAncTitle);
        txtBody = findViewById(R.id.txtBody);

        getusername();
        windowSize();
    }

    //change window size
    void windowSize(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout ((int)(width*.8), (int)(height*.65));
    }

    public void onClick (View v){
        switch (v.getId()){
            case R.id.btnSave:{
               save();
            }
            break;

            case R.id.btnCancel:{
                AnnouncementAddActivity.this.finish();
            }
            break;
        }
    }

    void save(){
        ancTitle = txtTitle.getText().toString();
        ancBody = txtBody.getText().toString();


        c = Calendar.getInstance();
        date = sdf.format(c.getTime());

        Announcement announcement1 = new Announcement();
        announcement1.setDate(date);
        announcement1.setTitle(ancTitle);
        announcement1.setBody(ancBody);
        announcement1.setUsername(username);

        mClubRef.child("announcement").child(announcement1.getDate()).setValue(announcement1);
        Toast.makeText(AnnouncementAddActivity.this, "Announcement Posted!", Toast.LENGTH_LONG).show();

        AnnouncementAddActivity.this.finish();
    }

    void getusername(){
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
