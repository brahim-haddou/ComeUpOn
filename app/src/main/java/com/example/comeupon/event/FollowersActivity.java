package com.example.comeupon.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.event.EventParticipantAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity implements EventParticipantAdapter.OnParticipantListener, EventParticipantAdapter.OnParticipantAcceptListener{


    private String TOKEN;
    private Profile mProfile;

    TextView text;
    ImageView img;
    RecyclerView rcy;


    ArrayList<Profile> profiles;
    EventParticipantAdapter profilesAdapter;


    AppDataService appDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);


        profiles = new ArrayList<>();
        appDataService = new AppDataService(this);

        TOKEN = getIntent().getStringExtra("key");
        String type = getIntent().getStringExtra("type");
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");
        profiles = (ArrayList<Profile>) getIntent().getSerializableExtra("profiles");


        text = findViewById(R.id.followers_txt);
        text.setText(type);

        img = findViewById(R.id.followers_back_btn);
        img.setOnClickListener(view -> finish());

        rcy = findViewById(R.id.followers_recyclerview);
        rcy.setNestedScrollingEnabled(false);
        rcy.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        profilesAdapter = new EventParticipantAdapter(profiles,true,this,this);


        rcy.setLayoutManager(mLayoutManager1);

        rcy.setAdapter(profilesAdapter);



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
        Profile profile = profiles.get(position);

        Log.e("user_placeObj", String.valueOf(profile.getPlaceApp()));
        Dialog mDialog = new Dialog(FollowersActivity.this);
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

    }
}