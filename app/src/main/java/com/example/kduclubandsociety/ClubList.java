package com.example.kduclubandsociety;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ClubList extends ArrayAdapter<Club> {
    private Activity context;
    private List<Club> clubList;

    public ClubList(Activity context, List<Club> clubList){
        super(context, R.layout.activity_club_list_activity, clubList);
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list_layout, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textView);
        TextView textViewDescription = listViewItem.findViewById(R.id.textView2);

        Club club = clubList.get(position);

        textViewName.setText(club.getName());
        textViewDescription.setText(club.getDescription());

        return listViewItem;
        //return super.getView(position, convertView, parent);
    }
}
