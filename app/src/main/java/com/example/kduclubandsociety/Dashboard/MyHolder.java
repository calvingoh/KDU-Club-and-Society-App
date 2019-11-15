package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyHolder extends RecyclerView.ViewHolder {
    View mView;
    ImageView imageView;
    TextView title, descro;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        /*
        this.imageView = itemView.findViewById(R.id.img);
        this.title = itemView.findViewById(R.id.title);
        this.descro= itemView.findViewById(R.id.details);*/
    }

    public void setTitle (String title){
        TextView post_title = (TextView)mView.findViewById(R.id.title);
        post_title.setText(title);
    }

    public void setDesc (String desc){
        TextView post_desc = (TextView)mView.findViewById(R.id.details);
        post_desc.setText(desc);
    }

    public void setImage (String image){
        ImageView post_img = (ImageView)mView.findViewById(R.id.img);
        Picasso.get().load(image).into(post_img);
    }


    }

