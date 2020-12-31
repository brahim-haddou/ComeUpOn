package com.example.comeupon.event;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

//
public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected EventActivityAdapter mListAdapter;
    protected EventParticipantAdapter mListAdapter1;
    protected RecyclerView mRecyclerView;
    protected RecyclerView mRecyclerView1;
    protected GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ImageView image = new ImageView(this);
        image.setImageResource(R.mipmap.logo);

        Activity_event a = new Activity_event("888888888888",
                "12-13",
                12, 21,
                ((BitmapDrawable) image.getDrawable()).getBitmap());
        Activity_event[] LIST_ACTIVITY = new Activity_event[]{ a, a, a, a, a, a, a, a};
        mRecyclerView = findViewById(R.id.event_activities);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        ArrayList<Activity_event> mEventItem = new ArrayList<>(LIST_ACTIVITY.length);

        mEventItem.addAll(Arrays.asList(LIST_ACTIVITY));
        mListAdapter = new EventActivityAdapter(mEventItem);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        // par
        Participant b = new Participant("88888888", ((BitmapDrawable) image.getDrawable()).getBitmap());
        Participant[] LIST_Participant = new Participant[]{ b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b};

        mRecyclerView1 = findViewById(R.id.event_participant_recyclerview);
        mRecyclerView1.setNestedScrollingEnabled(false);
        mRecyclerView1.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);

        ArrayList<Participant> mEventItem2 = new ArrayList<>(LIST_Participant.length);
        mEventItem2.addAll(Arrays.asList(LIST_Participant));
        mListAdapter1 = new EventParticipantAdapter(mEventItem2);

        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView1.setAdapter(mListAdapter1);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(33.6832336,-5.3794212);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 17f);
        mMap.moveCamera(cameraUpdate);
    }



}