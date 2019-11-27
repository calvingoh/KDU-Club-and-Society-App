package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.LoginActivity;
import com.example.kduclubandsociety.Notification.NotificationReciever;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.kduclubandsociety.Notification.NotificationActivity.CHANNEL_1_ID;

public class AnnouncementAddActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementAddActivity";
    private Context mContext = AnnouncementAddActivity.this;

    EditText txtTitle;
    EditText txtBody;
    String ancTitle;
    String ancBody;
    String username;
    String icon;
    String clubName;

    DatabaseReference ref, mClubRef, mStudentRef;
    int clubId;
    String currentUid;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Calendar c;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement_add);

        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId", 0);
        currentUid = intent.getStringExtra("currentUid");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student").child (currentUid);


        txtTitle = findViewById(R.id.txtAncTitle);
        txtBody = findViewById(R.id.txtBody);

        createNotificationChannel();
        getUsername();
        getClubDetails();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave: {
                save();
                sendNotification();

            }
            break;

            case R.id.btnCancel: {
                AnnouncementAddActivity.this.finish();
            }
            break;
        }
    }

    void save() {
        ancTitle = txtTitle.getText().toString().trim();
        ancBody = txtBody.getText().toString().trim();
        c = Calendar.getInstance();
        date = sdf.format(c.getTime());

        if (ancBody.length() > 0 && ancTitle.length() > 0){
            Announcement announcement1 = new Announcement();
            announcement1.setDate(date);
            announcement1.setTitle(ancTitle);
            announcement1.setBody(ancBody);
            announcement1.setUsername (username);
            announcement1.setClubIcon(icon);
            announcement1.setClubName(clubName);
            announcement1.setClubId (clubId);


            mClubRef.child("announcement").child(announcement1.getDate()).setValue(announcement1);
            Toast.makeText(AnnouncementAddActivity.this, "Announcement Posted!", Toast.LENGTH_LONG).show();

            AnnouncementAddActivity.this.finish();
            sendNotification();
        }

        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(AnnouncementAddActivity.this);
            dialog.setTitle("Oh no");
            dialog.setMessage("Do not leave the title/ body empty!");
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }




    }

    void getUsername(){
        mStudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username= dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void  getClubDetails (){
        mClubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                icon = dataSnapshot.child("icon").getValue().toString();
                clubName = dataSnapshot.child("name").getValue().toString();
                clubId = Integer.parseInt(dataSnapshot.child ("id").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Channel 1", importance);
            channel.setDescription("Channel 1");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

        public void sendNotification(){
            String title = txtTitle.getText().toString();
            String body = txtBody.getText().toString();

            Intent activityIntent = new Intent(this, DashboardActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

            Intent broadcastIntent = new Intent(this, NotificationReciever.class);
            broadcastIntent.putExtra("toastMessage", body);
            PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_one)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setGroup("example_group")
                    .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_ALL)
                    .setColor(Color.BLUE)
                    .setContentIntent(contentIntent)
                    .build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notification);


        }

    @Override
    public void onBackPressed() {

    }
}

