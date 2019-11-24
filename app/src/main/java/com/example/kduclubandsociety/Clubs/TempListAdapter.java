package com.example.kduclubandsociety.Clubs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.Dashboard.DashboardOptionActivity;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TempListAdapter extends RecyclerView.Adapter<TempListAdapter.MyViewHolder> {
    private List<Club> mDataset;
    private Context context;
    private String uid;
    private String[] student_club_id;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView img;
        public TextView title;
        public TextView details;
        public CardView card;
        public MyViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img);
            title = v.findViewById(R.id.title);
            details = v.findViewById(R.id.details);
            card = v.findViewById(R.id.card_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TempListAdapter(List<Club> clubList, Context context,String uid , String[] student_club_id) {
        mDataset = clubList;
        this.context = context;
        this.uid = uid;
        this.student_club_id = student_club_id;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TempListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_cardview, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.get().load(mDataset.get(position).getImage()).into(holder.img);

        holder.details.setText(mDataset.get(position).getDescription());
        holder.title.setText(mDataset.get(position).getName());
        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, DashboardOptionActivity.class);
            intent.putExtra("mTitle", mDataset.get(position).getName());
            intent.putExtra("mDesc", mDataset.get(position).getDescription());
            intent.putExtra("mImage", mDataset.get(position).getImage());
            intent.putExtra("mId", mDataset.get(position).getId());
            intent.putExtra("currentUid", uid);
            intent.putExtra("student_clubId",student_club_id);
            context.startActivity (intent);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}