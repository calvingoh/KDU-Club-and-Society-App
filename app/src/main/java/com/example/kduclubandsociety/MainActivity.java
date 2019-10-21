package com.example.kduclubandsociety;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        club = new Club();

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize views
        mSignOut = findViewById(R.id.SignOutbutton);
        mSave = findViewById(R.id.Savebutton);
        mShow = findViewById(R.id.Showbutton);
        mNameEditText = findViewById(R.id.NameeditText);
        mDescriptionEditText = findViewById(R.id.DescriptioneditText);
        mMaxEditText = findViewById(R.id.MaxeditText);
        mMeetingEditText = findViewById(R.id.MeetingeditText);

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
                myRef.child(String.valueOf(maxid+1)).setValue(club);
                Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_LONG).show();
            }
        });

        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Club_Profile.class);
                startActivity(intent);
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
/*
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = FirebaseDatabase.getInstance().getReference().child("club");
                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //Object value = dataSnapshot.getValue();
                        //Log.d(TAG, "Value is: " + value);
                        Club club = dataSnapshot.getValue(Club.class);
                        mTextView.setText(club.getDescription());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

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
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
*/
/*
    private void signOut() {
        mAuth.signOut();
    }
    */
/*
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSignout) {
            signOut();
        }
    }
*/
        @Override
        public void onBackPressed () {

        }

}




