package com.example.kduclubandsociety.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kduclubandsociety.LoginActivity;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = ProfileActivity.this;
    private String uid;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;


    private Button mSignOut;
    private TextView mName;
    private TextView mEmail;
    private TextView topTitle;
    private TextView mClubName;


    String findClubs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        uid = intent.getStringExtra("currentUid");

        setupBottomNavigationView();

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Profile");

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize views
        mName = findViewById(R.id.txtName);
        mEmail = findViewById(R.id.txtEmail);
        mClubName =findViewById(R.id.txtClub);


        //set profile details
        myRef = FirebaseDatabase.getInstance().getReference().child("Student").child(uid); //problem
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                findClubs = dataSnapshot.child ("clubs").getChildren().toString(); //to figure this part

                mName.setText(name);
                mEmail.setText (email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        //set profile details
        myRef = FirebaseDatabase.getInstance().getReference().child("Club").child("2"); //problem
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String clubs =  dataSnapshot.child("name").getValue().toString();

                mClubName.setText(clubs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    // set up bottom navigation bar
    private void setupBottomNavigationView(){
        Log.d (TAG, "setupBottomNavigationView: setting up Bottom Navigation View");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationView,uid);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    //sign out
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignOut :{
                mAuth.signOut();
                Intent intentSignOut = new Intent(mContext, LoginActivity.class);
                startActivity(intentSignOut);
            }
        }

    }
}
