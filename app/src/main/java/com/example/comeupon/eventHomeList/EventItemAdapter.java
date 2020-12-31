package com.example.comeupon.eventHomeList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.R;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.HashSet;

public class EventItemAdapter extends RecyclerView.Adapter<MapLocationViewHolder>{

    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected ArrayList<EventItem> mEventItem;

    public void setMapLocations(ArrayList<EventItem> mapLocations) {
        mEventItem = mapLocations;
    }

    @NonNull
    @Override
    public MapLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        MapLocationViewHolder viewHolder = new MapLocationViewHolder(parent.getContext(), view);

        mMapViews.add(viewHolder.mapView);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MapLocationViewHolder holder, int position) {
        EventItem mapLocation = mEventItem.get(position);

        holder.mapView.setTag(mapLocation);
        holder.eventDate.setText(mapLocation.Date);
        holder.eventHour.setText(mapLocation.Hour);
        holder.title.setText(mapLocation.Title);
        holder.imageView1.setImageResource(mapLocation.images.get(0));
        holder.imageView2.setImageResource(mapLocation.images.get(1));
        holder.imageView3.setImageResource(mapLocation.images.get(2));
        holder.setMapLocation(mapLocation);
    }

    @Override
    public int getItemCount() {
        return mEventItem == null ? 0 : mEventItem.size();
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }
}
