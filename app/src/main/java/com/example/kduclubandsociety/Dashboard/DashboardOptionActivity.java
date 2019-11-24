package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.Club_Profile;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardOptionActivity extends AppCompatActivity {
    private static final String TAG = "ClubsActivity";
    private Context mContext = DashboardOptionActivity.this;

    private TextView topTitle;
    private ImageView mImage;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;

    ListView option;
    Club club;

    int pos;
    String uid;
    String[]student_club_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_option);

        Intent intent = getIntent();
        String title = intent.getStringExtra("mTitle");
        String image = intent.getStringExtra("mImage");
        int id = intent.getIntExtra("mId",0);
        uid = intent.getStringExtra("currentUid");
        student_club_id= intent.getStringArrayExtra("student_clubId");



        mImage = findViewById(R.id.dashboardImg);
        Picasso.get().load(image).into(mImage);

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(title);

        //firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Club").child(Integer.toString(id));

        String optionList[]= {"Announcement", "Attendance", "Club Details"};

        option = findViewById(R.id.optionListview);
        ArrayAdapter<String> arrOption = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
        option.setAdapter(arrOption);

        getList();
        setOnClick();
    }

    void setOnClick (){
        option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                 switch (position){
                     case 0 :{

                         Intent intentAnnouncement = new Intent(mContext, AnnouncementActivity.class);
                         intentAnnouncement.putExtra("currentUid",uid);
                         intentAnnouncement.putExtra("cId",club.getId());
                         startActivity(intentAnnouncement);
                     }
                     break;

                     case 1:{

                     }
                     break;

                     case 2:{
                         Intent intentClubDetails = new Intent(mContext, Club_Profile.class);
                         intentClubDetails.putExtra("cId",club.getId());
                         intentClubDetails.putExtra("cName", club.getName());
                         intentClubDetails.putExtra("cDescription", club.getDescription());
                         intentClubDetails.putExtra("cMaxNum", club.getMaxNum());
                         intentClubDetails.putExtra("cMeeting", club.getMeeting());
                         intentClubDetails.putExtra("cImage", club.getImage());
                         intentClubDetails.putExtra("cAdmin", club.getAdmin());
                         intentClubDetails.putExtra("student_clubId",student_club_id );
                         intentClubDetails.putExtra("currentUid",uid);
                         startActivity (intentClubDetails);
                     }
                     break;
                 }
            }
        });
    }


    void getList(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                club = dataSnapshot.getValue(Club.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
