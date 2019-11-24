package com.example.kduclubandsociety.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.kduclubandsociety.Dashboard.DashboardActivity;
import com.example.kduclubandsociety.Notification.NotificationActivity;
import com.example.kduclubandsociety.Profile.ProfileActivity;
import com.example.kduclubandsociety.Clubs.ClubsActivity;
import com.example.kduclubandsociety.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    public static void enableNavigation (final Context context, BottomNavigationView view, final String uid, final String[] student_clubId){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_dashboard:{
                        Intent intent1 =new Intent(context, DashboardActivity.class);   //ACTIVITY_NUM = 0
                        intent1.putExtra("currentUid",uid);
                        intent1.putExtra("student_clubId", student_clubId);
                        context.startActivity(intent1);
                        break;
                    }
                    case R.id.ic_club:{
                        Intent intent2 =new Intent(context, ClubsActivity.class);   //ACTIVITY_NUM = 1
                        intent2.putExtra("currentUid",uid);
                        intent2.putExtra("student_clubId", student_clubId);
                        context.startActivity(intent2);
                        break;
                    }

                    case R.id.ic_notification:{
                        Intent intent3 =new Intent(context, NotificationActivity.class);   //ACTIVITY_NUM = 2
                        intent3.putExtra("currentUid",uid);
                        intent3.putExtra("student_clubId", student_clubId);
                        context.startActivity(intent3);
                        break;
                    }

                    case R.id.ic_profile:{
                        Intent intent4 =new Intent(context, ProfileActivity.class);    //ACTIVITY_NUM = 3
                        intent4.putExtra("currentUid",uid);
                        intent4.putExtra("student_clubId", student_clubId);
                        context.startActivity(intent4);
                        break;
                    }


                }

                return false;
            }
        });
    }

}
