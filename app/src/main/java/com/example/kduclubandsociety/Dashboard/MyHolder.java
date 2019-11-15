package com.example.kduclubandsociety.Dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView title, descro;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.img);
        this.title = itemView.findViewById(R.id.title);
        this.descro= itemView.findViewById(R.id.details);
    }

    }

