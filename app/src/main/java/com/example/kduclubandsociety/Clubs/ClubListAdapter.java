package com.example.kduclubandsociety.Clubs;

import android.app.Activity;
import android.graphics.ColorSpace;
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
import io.opencensus.trace.export.RunningSpanStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClubListAdapter extends ArrayAdapter<Club> implements Filterable {
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

        clubName.setText(club.getName());
        Picasso.get().load(club.getIcon()).into(icon);

        return listViewItem;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clubFilter;
    }

    private Filter clubFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Club> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(clubList);

            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Club item : clubList) {
                    if (item.getName().trim().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Club) resultValue).getName();
        }
    };

}



