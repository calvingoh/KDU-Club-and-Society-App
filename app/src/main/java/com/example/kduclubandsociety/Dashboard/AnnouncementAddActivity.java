package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.MainActivity;
import com.example.kduclubandsociety.Notification.NotificationReciever;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.kduclubandsociety.Dashboard.AnnouncementActivity.CHANNEL_1_ID;

public class AnnouncementAddActivity extends AppCompatActivity {
    private static final String TAG = "AnnouncementAddActivity";
    private Context mContext = AnnouncementAddActivity.this;

    EditText txtTitle;
    EditText txtBody;
    String ancTitle;
    String ancBody;

    DatabaseReference ref, mClubRef;
    int clubId;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Calendar c;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_announcement_add);

        Intent intent = getIntent();
        clubId =intent.getIntExtra("cId",0);

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));


        txtTitle= findViewById(R.id.txtAncTitle);
        txtBody = findViewById(R.id.txtBody);

        windowSize();
        createNotificationChannel();
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
               sendNotification();

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

        mClubRef.child("announcement").child(announcement1.getDate()).setValue(announcement1);
        Toast.makeText(AnnouncementAddActivity.this, "Announcement Posted!", Toast.LENGTH_LONG).show();

        AnnouncementAddActivity.this.finish();
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("this is channel 1");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification() {
        String title = txtTitle.getText().toString();
        String body = txtBody.getText().toString();

        Intent activityIntent = new Intent(this, AnnouncementActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReciever.class);
        broadcastIntent.putExtra("toastMessage", body);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification);

    }
}
