package com.example.comeupon.event;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback, EventParticipantAdapter.OnParticipantListener,EventParticipantAdapter.OnParticipantAcceptListener  {


    String TOKEN;
    Profile mProfile;

    Event mEvent;
    Profile owner;

    protected GoogleMap mMap;

    protected ImageView imageEvent;
    protected ImageView imageOwner;
    protected TextView usernameOwner;

    protected TextView title;
    protected TextView timeStartEnd;
    protected TextView timeMonth;
    protected TextView description;

    protected EventActivityAdapter activityAdapter;
    protected RecyclerView activityRecyclerView;

    protected LinearLayout participant_top_list;
    protected CardView cardView1;
    protected CardView cardView2;
    protected CardView cardView3;

    protected ImageView imagePart1;
    protected ImageView imagePart2;
    protected ImageView imagePart3;
    protected TextView ParticipantNumbersDescription;

    protected Button JoinBtn;

    protected EventParticipantAdapter participantAdapterAccepted;
    protected EventParticipantAdapter participantAdapterRequests;
    protected RecyclerView participantRecyclerView_accepted;
    protected RecyclerView participantRecyclerView_requests;

    ArrayList<Profile> mParticipantAccepted;
    ArrayList<Profile> mParticipantRequests;


    protected TextView Accepted_txt;
    protected TextView Requests_txt;

    AppDataService appDataService;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);


        TOKEN = getIntent().getStringExtra("key");
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");

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
        participant_top_list = findViewById(R.id.event_participant_top_list);
        cardView1 = findViewById(R.id.CardViewimage1);
        cardView2 = findViewById(R.id.CardViewimage2);
        cardView3 = findViewById(R.id.CardViewimage3);
        imagePart1 = findViewById(R.id.image1);
        imagePart2 = findViewById(R.id.image2);
        imagePart3 = findViewById(R.id.image3);
        ParticipantNumbersDescription = findViewById(R.id.event_list_participant_numbers);
        JoinBtn = findViewById(R.id.event_join_btn);

        Accepted_txt = findViewById(R.id.event_participant_Accepted);
        Requests_txt = findViewById(R.id.event_participant_Requests);


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


        participantRecyclerView_accepted = findViewById(R.id.event_participant_recyclerview_accepted);
        participantRecyclerView_accepted.setNestedScrollingEnabled(false);
        participantRecyclerView_accepted.setHasFixedSize(true);

        participantRecyclerView_requests = findViewById(R.id.event_participant_recyclerview_requests);
        participantRecyclerView_requests.setNestedScrollingEnabled(false);
        participantRecyclerView_requests.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        mParticipantAccepted = new ArrayList<>();
        participantAdapterAccepted = new EventParticipantAdapter(mParticipantAccepted,true,this,this);


        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mParticipantRequests = new ArrayList<>();
        participantAdapterRequests = new EventParticipantAdapter(mParticipantRequests,false,this,this);


        participantRecyclerView_accepted.setLayoutManager(mLayoutManager1);
        participantRecyclerView_requests.setLayoutManager(mLayoutManager2);


        participantRecyclerView_accepted.setAdapter(participantAdapterAccepted);
        participantRecyclerView_requests.setAdapter(participantAdapterRequests);


        appDataService = new AppDataService(this);
        appDataService.getParticipant(TOKEN, mEvent.getId(), 1, new AppDataService.ParticipantsResponseListener() {
            @Override
            public void onSuccess(ArrayList<Profile> participants) {
                mParticipantAccepted.addAll(participants);
                participantAdapterAccepted.notifyDataSetChanged();

                if(mParticipantAccepted.size() >= 3){
                    Picasso.get().load(mParticipantAccepted.get(0).getImage()).into(imagePart1);
                    Picasso.get().load(mParticipantAccepted.get(1).getImage()).into(imagePart2);
                    Picasso.get().load(mParticipantAccepted.get(2).getImage()).into(imagePart3);
                    if((mParticipantAccepted.size()-3) == 0) ParticipantNumbersDescription.setText(" are participating in this event");
                    else if((mParticipantAccepted.size()-3) == 1) ParticipantNumbersDescription.setText("And 1 other are participating");
                    else ParticipantNumbersDescription.setText("And "+ (mParticipantAccepted.size()-3) +" others are participating");
                }else{
                    participant_top_list.removeView(cardView1);
                    participant_top_list.removeView(cardView2);
                    participant_top_list.removeView(cardView3);
                    if(mParticipantAccepted.size() == 0) ParticipantNumbersDescription.setText("Nobody is participating yet");
                    else if(mParticipantAccepted.size()== 1) ParticipantNumbersDescription.setText("Now there is only one person participating now");
                    else ParticipantNumbersDescription.setText("only two person participating until now");
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });



        if(mProfile.getId() == owner.getId()){
            JoinBtn.setVisibility(View.INVISIBLE);
            appDataService.getParticipant(TOKEN, mEvent.getId(), 0,new AppDataService.ParticipantsResponseListener() {
                @Override
                public void onSuccess(ArrayList<Profile> participantRequests) {
                    mParticipantRequests.addAll(participantRequests);
                    participantAdapterRequests.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {

                }
            });
        }else {
            Requests_txt.setVisibility(View.INVISIBLE);
            Accepted_txt.setVisibility(View.INVISIBLE);
        }


        JoinBtn.setOnClickListener(view -> appDataService.RequestsParticipant(TOKEN, mEvent.getId(), new AppDataService.RequestsParticipantsResponseListener() {
            @Override
            public void onSuccess(Boolean accepted) {
                if(!accepted){
                    JoinBtn.setText("Waiting");
                    JoinBtn.setEnabled(false);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }));

        appDataService.ParticipantState(TOKEN, mEvent.getId(), new AppDataService.RequestsParticipantsResponseListener() {
            @Override
            public void onSuccess(Boolean accepted) {
                if(accepted){
                    Toast.makeText(EventActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    JoinBtn.setText("Accepted");
                    JoinBtn.setEnabled(false);
                } else if(!accepted){
                    Toast.makeText(EventActivity.this, "Waiting", Toast.LENGTH_SHORT).show();
                    JoinBtn.setText("Waiting");
                    JoinBtn.setEnabled(false);
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



    Button follow;
    ImageView profileImage;
    TextView profileFullName;
    TextView profileUserName;

    TextView follow_txt;
    TextView follower_txt;

    TextInputEditText profileEmail;
    TextInputEditText profilePhone;
    TextInputEditText profileBirthday;
    TextInputEditText profileAddress;

    @SuppressLint("SetTextI18n")
    @Override
    public void onParticipantClick(int position) {
        Profile profile = mParticipantAccepted.get(position);

        Log.e("user_placeObj", String.valueOf(profile.getPlaceApp()));
        Dialog mDialog = new Dialog(EventActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View newView = inflater.inflate(R.layout.profile, null);
        mDialog.setContentView(newView);

        follow = newView.findViewById(R.id.profile_btn_follow);

        profileImage = newView.findViewById(R.id.profile_image);
        profileFullName = newView.findViewById(R.id.profile_full_name);
        profileUserName = newView.findViewById(R.id.profile_username);

        follow_txt = newView.findViewById(R.id.profile_number_following);
        follower_txt = newView.findViewById(R.id.profile_number_followers);

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

        appDataService.AmIFollowing(TOKEN, profile.getId(), new AppDataService.AmIFollowingListener() {
            @Override
            public void onSuccess(Boolean bool) {
                if(bool){
                    follow.setText("UnFollow");
                    follow.setOnClickListener(view -> {
                        appDataService.UnFollow(TOKEN, profile.getId(), new AppDataService.AmIFollowingListener() {
                            @Override
                            public void onSuccess(Boolean bool) {
                                follow.setText("Follow");
                                follow.setEnabled(false);
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });
                    });
                }else {
                    follow.setOnClickListener(view -> {
                        appDataService.Follow(TOKEN, profile.getId(), new AppDataService.AmIFollowingListener() {
                            @Override
                            public void onSuccess(Boolean bool) {
                                follow.setText("UnFollow");
                                follow.setEnabled(false);
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        });
                    });
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        appDataService.FollowParticipant(TOKEN, profile.getId(), new AppDataService.FollowParticipantListener() {
            @Override
            public void onSuccess(int follow, int follower) {
                follow_txt.setText(String.valueOf(follow));
                follower_txt.setText(String.valueOf(follower));
            }

            @Override
            public void onFailure(String error) {

            }
        });
        mDialog.show();
    }

    @Override
    public void OnParticipantAcceptClick(int position) {

        appDataService.AcceptedParticipant(TOKEN, mEvent.getId(), mParticipantRequests.get(position).getId(), new AppDataService.AcceptedParticipantsResponseListener() {
            @Override
            public void onSuccess(Boolean accepted) {
                if(accepted){
                    Toast.makeText(EventActivity.this, mParticipantRequests.get(position).getUser().getUsername(), Toast.LENGTH_SHORT).show();

                    mParticipantAccepted.add(mParticipantRequests.get(position));
                    mParticipantRequests.remove(position);
                    participantAdapterRequests.notifyDataSetChanged();
                    participantAdapterAccepted.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        }
}