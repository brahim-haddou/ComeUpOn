package com.example.comeupon.event;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.comeupon.R;
import com.example.comeupon.eventHomeList.EventItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class EventActivityAdapter extends RecyclerView.Adapter<EventActivityAdapter.Activity_eventViewHolder> {

    ArrayList<Activity_event> activities;

    public EventActivityAdapter(ArrayList<Activity_event> activities) {
        this.activities = activities;
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
        Activity_event activity = activities.get(position);

        holder.name.setText(activity.name);
        holder.image.setImageBitmap(activity.image);
        holder.category.setText(activity.category);
        holder.number_activity.setText(""+activity.number_activity);
        holder.number_participant.setText(""+activity.number_participant);
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
