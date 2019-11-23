package com.example.kduclubandsociety.Dashboard;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Announcement;
import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> implements Filterable {
    private Activity context;
    private List<Announcement> announcementList;

    public AnnouncementAdapter(Activity context, List<Announcement> announcementList) {
        super(context, R.layout.dashboard_announcement, announcementList);
        this.context = context;
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.notification_listview_row, null, true);

        TextView title = listViewItem.findViewById(R.id.txtNotiTitle);
        TextView body = listViewItem.findViewById(R.id.txtNotiBody);
        TextView date = listViewItem.findViewById(R.id.txtNotiDate);


        Announcement announcement= announcementList.get(position);

        title.setText(announcement.getTitle());
        body.setText(announcement.getBody());
        date.setText(announcement.getDate());

        return listViewItem;

    }

}



