package com.example.kduclubandsociety.Clubs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ClubListAdapter extends ArrayAdapter<Club> implements Filterable {
    private Activity context;
    private List<Club> clubList;

    public ClubListAdapter(Activity context, List<Club> clubList){
        super(context, R.layout.activity_clubs, clubList);
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_listview_layout, null, true);

        TextView clubName = listViewItem.findViewById(R.id.txtClubName);
        TextView clubDescription = listViewItem.findViewById(R.id.txtClubDesc);
        ImageView img = listViewItem.findViewById(R.id.imgIcon);

        Club club = clubList.get(position);

        clubName.setText(club.getName());
        clubDescription.setText(club.getDescription());
        Picasso.get().load(club.getImage()).into(img);

        return listViewItem;
        //return super.getView(position, convertView, parent);
    }

}



