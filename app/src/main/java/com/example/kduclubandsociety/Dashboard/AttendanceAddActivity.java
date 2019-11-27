package com.example.kduclubandsociety.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kduclubandsociety.Class.Attendance;
import com.example.kduclubandsociety.Class.Member;
import com.example.kduclubandsociety.Notification.NotificationReciever;
import com.example.kduclubandsociety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.kduclubandsociety.Notification.NotificationActivity.CHANNEL_1_ID;

public class AttendanceAddActivity extends AppCompatActivity {
    private static final String TAG = "AttendanceAddActivity";
    private Context mContext = AttendanceAddActivity.this;

    EditText date;
    EditText time;
    EditText location;

    String mtgDate;
    String mtgTime;
    String mtgLocation;

    DatabaseReference ref, mClubRef, mStudentRef;
    int clubId;
    String currentUid;

   String[] student_club_id;
   ArrayList<Member> memberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_attendance_add);

        Intent intent = getIntent();
        clubId = intent.getIntExtra("cId", 0);
        currentUid = intent.getStringExtra("currentUid");

        // firebase
        ref = FirebaseDatabase.getInstance().getReference();
        mClubRef = ref.child("Club").child(Integer.toString(clubId));
        mStudentRef = ref.child("Student");
        memberList = new ArrayList<Member>();

        //initialization
        date = findViewById(R.id.txtMtgDate);
        time = findViewById(R.id.txtMtgTime);
        location =findViewById(R.id.txtMtgLocation);


        getMembers();
        createNotificationChannel();
        pickDate();
        pickTime();
    }

     public void onClick (View v){
        switch (v.getId()){
            case R.id.btnSave:{
                save();
            }
            break;

            case R.id.btnCancel:{
                AttendanceAddActivity.this.finish();
            }
            break;
        }
    }

    void save(){
        mtgDate=date.getText().toString().trim();
        mtgTime= time.getText().toString().trim();
        mtgLocation=location.getText().toString().trim();
        String key = mClubRef.child("attendance").push().getKey();

        if (mtgDate.length()==0 || mtgTime.length()==0 || mtgLocation.length()==0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(AttendanceAddActivity.this);
            dialog.setTitle("Oh no");
            dialog.setMessage("Do not leave the details empty!");
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        else{
            Attendance attendance = new Attendance();
            attendance.setId(key);
            attendance.setDate(mtgDate);
            attendance.setTime(mtgTime);
            attendance.setLocation(mtgLocation);
            attendance.setMembers(memberList);

            mClubRef.child("attendance").child(key).setValue(attendance);
            Toast.makeText(AttendanceAddActivity.this, "Meeting added", Toast.LENGTH_LONG).show();

            AttendanceAddActivity.this.finish();
            sendNotification();
        }

    }

    void pickDate(){
        Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                date.setText(sdf.format(myCalendar.getTime()));

            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(mContext, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    void pickTime(){
        Calendar mcurrentTime = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mcurrentTime.set(Calendar.HOUR, hourOfDay);
                mcurrentTime.set(Calendar.MINUTE, minute);
                String myFormat = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                time.setText(sdf.format(mcurrentTime.getTime()));
            }
        };
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              new TimePickerDialog(mContext,timePickerListener,
                      mcurrentTime.get(Calendar.HOUR),
                      mcurrentTime.get(Calendar.MINUTE),false).show();
            }
        });

    }

    void getMembers(){
       ValueEventListener studentListener = new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot data : dataSnapshot.getChildren()){
                   student_club_id = data.child("clubs").getValue(String.class).split(";");
                   for (int i = 0; i<student_club_id.length; i++){
                       if (Integer.toString(clubId).equals(student_club_id[i])){
                           Member member = new Member();
                           member.setUid(data.child("uid").getValue(String.class));
                           member.setPresent(false);
                           member.setName(data.child("name").getValue(String.class));
                           memberList.add (member);
                       }
                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
       mStudentRef.addListenerForSingleValueEvent(studentListener);
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
        String title = date.getText().toString();
        String body = time.getText().toString();
        String body2 = location.getText().toString();

        Intent activityIntent = new Intent(this, AttendanceActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReciever.class);
        broadcastIntent.putExtra("toastMessage", body);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("New Meeting")
                .setContentText(body)
                .setContentText(body2)
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
}
