package com.example.comeupon.eventHomeList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    public TextView title;
    public TextView eventDate;
    public TextView eventHour;
    public ImageView imageView1, imageView2, imageView3;

    public GoogleMap mGoogleMap;
    protected EventItem mMapLocation;

    public MapView mapView;
    private final Context mContext;



    public MapLocationViewHolder(Context context, View view){
        super(view);

        mContext = context;

        title = view.findViewById(R.id.event_item_title);
        mapView = view.findViewById(R.id.event_item_map);
        eventDate = view.findViewById(R.id.event_item_date);
        eventHour = view.findViewById(R.id.event_item_time);
        imageView1 = view.findViewById(R.id.image1);
        imageView2 = view.findViewById(R.id.image2);
        imageView3 = view.findViewById(R.id.image3);


        mapView.onCreate(null);
        mapView.getMapAsync(MapLocationViewHolder.this);
    }

    public void setMapLocation(EventItem mapLocation) {
        mMapLocation = mapLocation;

        // If the map is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents();
        }
    }

    protected void updateMapContents() {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap.clear();

        // Update the mapView feature data and camera position.
        mGoogleMap.addMarker(new MarkerOptions().position(mMapLocation.center));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation.center, 17f);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // If we have map data, update the map content.
        if (mMapLocation != null) {
            updateMapContents();
        }
    }
}
