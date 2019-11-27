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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AnnouncementDetails extends AppCompatActivity {
    private static final String TAG = "AnnouncementDetails";
    private TextView topTitle;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = AnnouncementDetails.this;

    private TextView aTitle;
    private TextView aBody;
    private TextView aDate;
    private TextView aUser;
    private ImageView aIcon;
    private TextView aClubName;
    FloatingActionButton btnEdit;

    String currentUid, title, body , date, username, icon, clubName;
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
        aClubName = findViewById(R.id.txtClubName);
        aIcon = findViewById(R.id.imgIcon);
        btnEdit = findViewById(R.id.btnEdit);

        //intents
        Intent intent = getIntent();
        clubId= intent.getIntExtra("cId", 0);
        currentUid = intent.getStringExtra("currentUid");
        title = intent.getStringExtra("annTitle");
        body = intent.getStringExtra("annBody");
        date = intent.getStringExtra("annDate");
        username = intent.getStringExtra("annUsername");
        icon = intent.getStringExtra("annIcon");
        clubName = intent.getStringExtra("annClubName");

        setupBottomNavigationView();

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club");
        mStudentRef = ref.child("Student").child(currentUid);


        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Announcement Details");
   //     topTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
     //   topTitle.setTextSize(14);

        //DISPLAY DETAIL
        aTitle.setText(title);
        aUser.setText(username);
        aDate.setText("Posted on " +date);
        aBody.setText(body);
        aClubName.setText("from " + clubName);
        Picasso.get().load(icon).into(aIcon);

        checkPermission();
    }

    @NonNull
    void checkPermission(){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String admin =dataSnapshot.child(Integer.toString(clubId)).child("admin").getValue(String.class);
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

    public void onClick(View v){
        if (v.getId()==R.id.btnEdit){
            Intent intentEdit = new Intent (mContext, AnnouncementDetailsEdit.class);
            intentEdit.putExtra("title", title);
            intentEdit.putExtra("body", body);
            intentEdit.putExtra("date", date);
            intentEdit.putExtra("cId", clubId);
            intentEdit.putExtra("currentUid", currentUid);
            intentEdit.putExtra ("icon", icon);
            intentEdit.putExtra("username", username);
            intentEdit.putExtra("clubName", clubName);
            startActivity(intentEdit);
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
