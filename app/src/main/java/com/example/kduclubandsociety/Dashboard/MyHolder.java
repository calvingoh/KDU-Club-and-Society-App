package com.example.kduclubandsociety.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kduclubandsociety.MainActivity;
import com.example.kduclubandsociety.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class MyHolder extends RecyclerView.ViewHolder {
    View mView;
    Intent intent;
    String title, desc, image;
    int id;
    String uid;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), DashboardOptionActivity.class);
                intent.putExtra("mTitle", title);
                intent.putExtra("mDesc", desc);
                intent.putExtra("mImage", image);
                intent.putExtra("mId", id);
                intent.putExtra("currentUid", uid);
                v.getContext().startActivity (intent);
            }
        });
    }

    public void setTitle (String title){
        TextView post_title = (TextView)mView.findViewById(R.id.title);
        this.title =  title;
        post_title.setText(title);
    }

    public void setDesc (String desc){
        TextView post_desc = (TextView)mView.findViewById(R.id.details);
        this.desc = desc;
        post_desc.setText(desc);
    }

    public void setImage (String image){
        ImageView post_img = (ImageView)mView.findViewById(R.id.img);
        this.image= image;
        Picasso.get().load(image).into(post_img);
    }


    }

