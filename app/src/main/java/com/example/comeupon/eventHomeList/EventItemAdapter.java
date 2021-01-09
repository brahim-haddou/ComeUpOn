package com.example.comeupon.eventHomeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Event;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.MapLocationViewHolder>{

    private final ArrayList<Event> mEventItem;
    private final OnEventListener mEventListener;
    public EventItemAdapter(ArrayList<Event> eventItem, OnEventListener eventListener) {
        mEventItem = eventItem;
        mEventListener = eventListener;
    }

    @NonNull
    @Override
    public MapLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new MapLocationViewHolder(parent.getContext(), view, mEventListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MapLocationViewHolder holder, int position) {
        Event event = mEventItem.get(position);
        Profile owner = event.getOwner();


        holder.username.setText(owner.getUser().getUsername());
        Picasso.get()
                .load(owner.getImage())
                .into(holder.imageUser);

        holder.title.setText(event.getTitle());
        Picasso.get()
                .load(event.getImage())
                .into(holder.imageEvent);
        holder.eventDate.setText(event.getEnd_date().getMonth().toString());
        holder.eventHour.setText(event.getStart_date().getHour()+"-"+event.getEnd_date().getHour());


    }

    @Override
    public int getItemCount() {
        return mEventItem == null ? 0 : mEventItem.size();
    }

    public interface OnEventListener{
        void onEventClick(int position);
    }

    public static class MapLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView eventDate;
        public TextView eventHour;
        public TextView username;
        public ImageView imageEvent, imageUser;
        public OnEventListener mOnEventListener;

        private final Context mContext;



        public MapLocationViewHolder(Context context, View view,OnEventListener onEventListener){
            super(view);

            mContext = context;
            mOnEventListener = onEventListener;

            title = view.findViewById(R.id.event_item_title);
            imageEvent = view.findViewById(R.id.event_item_image);
            imageUser = view.findViewById(R.id.event_item_image_user);
            username = view.findViewById(R.id.event_item_username);
            eventDate = view.findViewById(R.id.event_item_date);
            eventHour = view.findViewById(R.id.event_item_time);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnEventListener.onEventClick(getAdapterPosition());
        }
    }
}
