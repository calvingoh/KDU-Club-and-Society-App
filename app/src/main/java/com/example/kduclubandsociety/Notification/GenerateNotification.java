package com.example.kduclubandsociety.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kduclubandsociety.Class.Student;
import com.example.kduclubandsociety.R;

import retrofit2.Retrofit;

public class GenerateNotification extends AppCompatActivity {

    private static final String CHANNEL_1_ID = "Channel 1";




    //view
    EditText mtitle;
    EditText mbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_notification);


        mtitle = findViewById(R.id.TitleeditText);
        mbody = findViewById(R.id.BodyeditText);

        Student student = (Student) getIntent().getSerializableExtra("student");

       
    }


/*
    public void sendNotification1(View view){
        String title = mtitle.getText().toString();
        String body = mbody.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.profile_pic)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification);
      


    }

 */


}
