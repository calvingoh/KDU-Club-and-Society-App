package com.example.kduclubandsociety.Clubs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ClubListAdapter extends ArrayAdapter<Club> {
    private Activity context;
    private List<Club> clubList;

    public ClubListAdapter(Activity context, List<Club> clubList) {
        super(context, R.layout.activity_clubs, clubList);
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_listview_row, null, true);

        TextView clubName = listViewItem.findViewById(R.id.txtClubName);
       // TextView clubDescription = listViewItem.findViewById(R.id.txtClubDesc);
        ImageView icon = listViewItem.findViewById(R.id.imgIcon);

        Club club = clubList.get(position);

        clubName.setText(clubList.get(position).getName());
        Picasso.get().load(clubList.get(position).getIcon()).into(icon);

        return listViewItem;

    }

    public void setFilter (List<Club> newList){
        clubList = new ArrayList<Club>();
        clubList.addAll(newList);
        notifyDataSetChanged();
    }

}



