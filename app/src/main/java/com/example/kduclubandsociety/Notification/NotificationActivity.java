package com.example.kduclubandsociety.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.Class.Student;
import com.example.kduclubandsociety.Dashboard.AnnouncementActivity;
import com.example.kduclubandsociety.Dashboard.AnnouncementAdapter;
import com.example.kduclubandsociety.Dashboard.DashboardActivity;
import com.example.kduclubandsociety.R;
import com.example.kduclubandsociety.Utils.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = NotificationActivity.this;
    private String uid;

    public static final String CHANNEL_1_ID = "Channel1";

    ListView nListview;
    AnnouncementAdapter adp;
    List<Announcement> notificationList;
    Announcement notification;

    DatabaseReference ref, mClubRef, mStudentRef;

    private TextView topTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        uid = intent.getStringExtra("currentUid");

        setupBottomNavigationView();

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText("Notification");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mStudentRef = ref.child("Student").child(uid);
        mClubRef= ref.child("Club");

        //listView
        nListview = findViewById(R.id.notification_list);
        notificationList= new ArrayList<>();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
                channel1.setDescription("This is channel 1");


                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel1);

            }
        }

        addNotification();
    }

    void addNotification(){
        String[] registeredClub = DashboardActivity.student_clubs_id;
        notificationList.clear();
        adp = new AnnouncementAdapter(NotificationActivity.this, notificationList);
        nListview.setAdapter(adp);
        for(int i = 0; i < registeredClub.length; i++) {
            mClubRef.child(registeredClub[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("announcement")){
                        DataSnapshot pathSnapshot = dataSnapshot.child("announcement");

                        for (DataSnapshot annoSnapshot: pathSnapshot.getChildren() ){
                            notification = annoSnapshot.getValue(Announcement.class);
                            notificationList.add (notification);
                        }
                    }
                    adp.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
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

    @Override
    public void onBackPressed() {

    }
}
