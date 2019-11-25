package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnnouncementDetails extends AppCompatActivity {
    private static final String TAG = "AnnouncementDetails";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = AnnouncementDetails.this;

    private TextView aTitle;
    private TextView aBody;
    private TextView aDate;
    private TextView aUser;

    String currentUid;
    int clubId;

    DatabaseReference ref, mClubRef, mStudentRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement_details);

        aTitle = findViewById(R.id.txtAnnouncementTitle);
        aBody = findViewById(R.id.txtBody);
        aDate = findViewById(R.id.txtDate);
        aUser = findViewById(R.id.txtUser);

        //intents
        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId",0);
        currentUid = intent.getStringExtra("currentUid");
        String title = intent.getStringExtra("annTitle");
        String body = intent.getStringExtra("annBody");
        String date = intent.getStringExtra("annDate");
        String username = intent.getStringExtra("annUsername");

        setupBottomNavigationView();

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club");
        mStudentRef = ref.child("Student").child(currentUid);


        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Announcement Details");

        //DISPLAY DETAIL
        aTitle.setText(title);
        aUser.setText(username);
        aDate.setText(date);
        aBody.setText(body);

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
