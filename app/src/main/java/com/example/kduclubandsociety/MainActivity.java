package com.example.kduclubandsociety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;

    Club club;
    long maxid=0;
    private static final String TAG = "MainActivity";
    private Button mSignOut;
    private Button mSave;
    private Button mShow;
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private EditText mMaxEditText;
    private EditText mMeetingEditText;

    private TextView mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("currentUid");

        //club = new Club();

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize views
        mSignOut = findViewById(R.id.signoutButton);
        mSave = findViewById(R.id.saveButton);
        mShow = findViewById(R.id.showButton);
        mNameEditText = findViewById(R.id.nameeditText);
        mDescriptionEditText = findViewById(R.id.descriptioneditText);
        mMaxEditText = findViewById(R.id.maxeditText);
        mMeetingEditText = findViewById(R.id.meetingeditText);
        mName = findViewById(R.id.txtName);



        myRef = FirebaseDatabase.getInstance().getReference().child("Student").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();

                mName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //insert data
        myRef = FirebaseDatabase.getInstance().getReference().child("Club");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                club.setName(mNameEditText.getText().toString().trim());
                club.setDescription(mDescriptionEditText.getText().toString().trim());
                club.setMeeting(mMeetingEditText.getText().toString().trim());
                int max = Integer.parseInt(mMaxEditText.getText().toString().trim());
                club.setMaxNum(max);
                myRef.child(String.valueOf(maxid+1)).setValue(club); //TO REGISTER NEW CLUB
                Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
            }
        });

        /*mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClubsActivity.class);
                startActivity(intent);
            }
        });

         */

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });


/*
        //Bottom Navigation
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_clubs, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Authentication
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "Sign in" + user.getUid());
                }

                else {
                    Log.d(TAG, "sign out");
                }
            }
        };

*/

    }

        @Override
        public void onBackPressed () {

        }

}




