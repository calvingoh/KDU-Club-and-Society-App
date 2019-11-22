package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

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

    DatabaseReference ref, mClubRef;
    String clubId;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Calendar c;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement_add);

        Intent intent = getIntent();
        clubId =intent.getStringExtra("cId");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(clubId);


        txtTitle= findViewById(R.id.txtAncTitle);
        txtBody = findViewById(R.id.txtBody);

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
            //    save();
            }
            break;

            case R.id.btnCancel:{
                AnnouncementAddActivity.this.finish();
            }
        }
    }

    void save(){
        String ancTitle = txtTitle.getText().toString();
        String ancBody = txtBody.getText().toString();

        c = Calendar.getInstance();
        date = sdf.format(c.getTime());

       mClubRef.child("announcement").child(date)
               .child("title").setValue(ancTitle);

      /* mClubRef.child("announcement").child(date)
                .child ("body").setValue (ancBody);*/

    }
}
