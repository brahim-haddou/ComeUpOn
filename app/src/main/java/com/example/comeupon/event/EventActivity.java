package com.example.comeupon.event;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Activity;
import com.example.comeupon.Models.Event;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.example.comeupon.VolleyApi.AppDataService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback, EventParticipantAdapter.OnParticipantListener {


    protected GoogleMap mMap;

    protected ImageView imageEvent;
    protected ImageView imageOwner;
    protected TextView usernameOwner;


    protected ImageView imagePart1;
    protected ImageView imagePart2;
    protected ImageView imagePart3;

    protected TextView title;
    protected TextView timeStartEnd;
    protected TextView timeMonth;
    protected TextView description;

    protected EventActivityAdapter activityAdapter;
    protected RecyclerView activityRecyclerView;

    protected EventParticipantAdapter participantAdapter;
    protected RecyclerView participantRecyclerView;

    Event mEvent;
    Profile owner;
    ArrayList<Profile> mParticipants;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        imageEvent = findViewById(R.id.event_image);
        imageOwner = findViewById(R.id.event_image_owner);
        usernameOwner = findViewById(R.id.event_username);
        title = findViewById(R.id.event_title);
        timeStartEnd = findViewById(R.id.event_timeStartEnd);
        timeMonth = findViewById(R.id.event_timeMonth);
        description = findViewById(R.id.event_description);
        imagePart1 = findViewById(R.id.image1);
        imagePart2 = findViewById(R.id.image2);
        imagePart3 = findViewById(R.id.image3);


        mEvent = (Event) getIntent().getSerializableExtra("Event");
        assert mEvent != null;
        owner = mEvent.getOwner();


        Picasso.get().load(mEvent.getImage()).into(imageEvent);

        Picasso.get().load(owner.getImage()).into(imageOwner);
        usernameOwner.setText(owner.getUser().getUsername());

        title.setText(mEvent.getTitle());
        description.setText(mEvent.getDescription());
        timeStartEnd.setText(mEvent.getStart_date().getHour()+"-"+mEvent.getEnd_date().getHour());
        timeMonth.setText(mEvent.getEnd_date().getMonth().toString().toLowerCase());

        activityRecyclerView = findViewById(R.id.event_activities);
        activityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        ArrayList<Activity> mEventItem = new ArrayList<>(mEvent.getActivityEvent().length);

        mEventItem.addAll(Arrays.asList(mEvent.getActivityEvent()));
        activityAdapter = new EventActivityAdapter(mEventItem,null);

        activityRecyclerView.setLayoutManager(mLayoutManager);
        activityRecyclerView.setAdapter(activityAdapter);


        participantRecyclerView = findViewById(R.id.event_participant_recyclerview);
        participantRecyclerView.setNestedScrollingEnabled(false);
        participantRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);

        mParticipants = new ArrayList<>();
        participantAdapter = new EventParticipantAdapter(mParticipants,this);

        participantRecyclerView.setLayoutManager(mLayoutManager1);
        participantRecyclerView.setAdapter(participantAdapter);




        AppDataService appDataService = new AppDataService(this);
        appDataService.getParticipant(mEvent.getId(), new AppDataService.ParticipantsResponseListener() {
            @Override
            public void onSuccess(ArrayList<Profile> participants) {
                mParticipants.addAll(participants);
                participantAdapter.notifyDataSetChanged();

                if(mParticipants.size() >= 3){
                    Picasso.get().load(mParticipants.get(0).getImage()).into(imagePart1);
                    Picasso.get().load(mParticipants.get(1).getImage()).into(imagePart2);
                    Picasso.get().load(mParticipants.get(2).getImage()).into(imagePart3);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mEvent.getPlace_App_event().getLat(),mEvent.getPlace_App_event().getLan());
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(mEvent.getPlace_App_event().getAddress()));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 17f);
        mMap.moveCamera(cameraUpdate);
    }

    ImageView profileImage;
    TextView profileFullName;
    TextView profileUserName;

    TextInputEditText profileEmail;
    TextInputEditText profilePhone;
    TextInputEditText profileBirthday;
    TextInputEditText profileAddress;

    @SuppressLint("SetTextI18n")
    @Override
    public void onParticipantClick(int position) {
        Profile profile = mParticipants.get(position);

        Log.e("user_placeObj", String.valueOf(profile.getPlaceApp()));
        Dialog mDialog = new Dialog(EventActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View newView = (View) inflater.inflate(R.layout.profile, null);
        mDialog.setContentView(newView);

        profileImage = newView.findViewById(R.id.profile_image);
        profileFullName = newView.findViewById(R.id.profile_full_name);
        profileUserName = newView.findViewById(R.id.profile_username);

        profileEmail = newView.findViewById(R.id.profile_email_input);
        profilePhone = newView.findViewById(R.id.profile_phone_input);
        profileBirthday = newView.findViewById(R.id.profile_birthday_input);
        profileAddress = newView.findViewById(R.id.profile_address_input);


        profileFullName.setText(profile.getUser().getFirst_name()+" "+profile.getUser().getLast_name());
        profileUserName.setText(profile.getUser().getUsername());
        Picasso.get().load(profile.getImage()).into(profileImage);

        profileEmail.setText(profile.getUser().getEmail());
        profilePhone.setText(profile.getPhone());
        profileBirthday.setText(profile.getBirthday());
        profileAddress.setText(profile.getPlaceApp().getCity()+", "+profile.getPlaceApp().getCountry());

        mDialog.show();
    }
}