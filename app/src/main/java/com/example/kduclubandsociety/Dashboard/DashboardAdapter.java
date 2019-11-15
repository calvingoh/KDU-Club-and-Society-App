package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kduclubandsociety.Class.Club;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAdapter extends RecyclerView.Adapter<MyHolder>  {
    Context context;
    ArrayList <Club> club;

    public DashboardAdapter (Context context, ArrayList<Club> club ){
        this.context = context;
        this.club = club;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //convert xml to view objects
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_cardview, null);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.title.setText(club.get(position).getName());
        holder.descro.setText(club.get(position).getDescription());
        Picasso.get().load(club.get(position).getImage()).into (holder.imageView);
    }

    @Override
    public int getItemCount() {
        return club.size();
    }
}
