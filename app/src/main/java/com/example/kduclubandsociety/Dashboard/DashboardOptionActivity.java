package com.example.kduclubandsociety.Dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

public class DashboardOptionActivity extends AppCompatActivity {

    private TextView topTitle;
    private ImageView mImage;

    ListView option;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_option);

        Intent intent = getIntent();
        String title = intent.getStringExtra("mTitle");
        String image = intent.getStringExtra("mImage");
        int id = intent.getIntExtra("mId",0);

        mImage = findViewById(R.id.dashboardImg);
        Picasso.get().load(image).into(mImage);

        //TOP TAB TITLE
        topTitle =findViewById(R.id.txtTitle);
        topTitle.setText(title);

        String optionList[]= {"Announcement", "Attendance", "Club Details"};

        option = findViewById(R.id.optionListview);
        ArrayAdapter<String> arrOption = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionList);
        option.setAdapter(arrOption);

    }
}
