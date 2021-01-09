package com.example.comeupon.event;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Activity;
import com.example.comeupon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class EventActivityAdapter extends RecyclerView.Adapter<EventActivityAdapter.Activity_eventViewHolder> {

    ArrayList<Activity> activities;
    ArrayList<Bitmap> images;
    public EventActivityAdapter(ArrayList<Activity> activities, ArrayList<Bitmap> images) {
        this.activities = activities;
        this.images = images;
    }

    @NonNull
    @Override
    public EventActivityAdapter.Activity_eventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Activity_eventViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_item,
                        parent,
                        false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventActivityAdapter.Activity_eventViewHolder holder, int position) {

        Activity activity = activities.get(position);
        holder.name.setText(activity.getName());
        holder.category.setText(activity.getCategory());
        holder.number_activity.setText(""+activity.getNumber_Activity());
        holder.number_participant.setText(""+activity.getNumber_Participant());
        if(images != null){
            Bitmap bitmap = images.get(position);
            holder.image.setImageBitmap(bitmap);
        }else {
            Picasso.get().load(activity.getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class Activity_eventViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView category;
        public TextView number_participant;
        public TextView number_activity;
        public ImageView image;

        public Activity_eventViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.activities_item_image);
            name = itemView.findViewById(R.id.activities_item_name);
            category = itemView.findViewById(R.id.activities_item_category);
            number_activity = itemView.findViewById(R.id.activities_item_number_activity);
            number_participant = itemView.findViewById(R.id.activities_item_number_participant);
        }
    }
}
