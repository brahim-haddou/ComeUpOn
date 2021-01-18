package com.example.comeupon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.comeupon.Models.Profile;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.event.FollowersActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {


    private String TOKEN;
    private Profile mProfile;

    ImageView user_image;
    TextView user_full_name;
    TextView user_username;
    TextView user_followers;
    TextView user_following;
    TextInputEditText full_name;
    TextInputEditText user_email;
    TextInputEditText user_phone;
    TextInputEditText user_birthday;
    TextInputEditText user_address;


    ArrayList<Profile> mFollowers;
    ArrayList<Profile> mFollowing;

    CardView followers;
    CardView followings;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        TOKEN = getIntent().getStringExtra("key");
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");

        followers     = findViewById(R.id.cardView_followers);
        followers.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, FollowersActivity.class);
            intent.putExtra("key", TOKEN);
            intent.putExtra("Profile", mProfile);
            intent.putExtra("type", "Followers");
            intent.putExtra("profiles", mFollowers);
            startActivity(intent);
        });
        followings     = findViewById(R.id.cardView_followings);
        followings.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, FollowersActivity.class);
            intent.putExtra("key", TOKEN);
            intent.putExtra("Profile", mProfile);
            intent.putExtra("type", "Followings");
            intent.putExtra("profiles", mFollowing);
            startActivity(intent);
        });


        user_image     = findViewById(R.id.user_profile_image);
        user_full_name = findViewById(R.id.user_profile_full_name);
        user_username  = findViewById(R.id.user_profile_username);
        user_followers = findViewById(R.id.user_profile_number_followers);
        user_following = findViewById(R.id.user_profile_number_following);
        full_name     = findViewById(R.id.user_profile_full_name_input);
        user_email     = findViewById(R.id.user_profile_email_input);
        user_phone     = findViewById(R.id.user_profile_phone_input);
        user_birthday  = findViewById(R.id.user_profile_birthday_input);
        user_address   = findViewById(R.id.user_profile_address_input);


        mFollowers = new ArrayList<>();
        mFollowing = new ArrayList<>();

        Picasso.get().load(mProfile.getImage()).into(user_image);
        user_full_name.setText(mProfile.getUser().getFirst_name()+" "+mProfile.getUser().getLast_name());
        user_username.setText(mProfile.getUser().getUsername());
        full_name.setText(mProfile.getUser().getFirst_name()+" "+mProfile.getUser().getLast_name());
        user_email.setText(mProfile.getUser().getEmail());
        user_phone.setText(mProfile.getPhone());
        user_birthday.setText(mProfile.getBirthday());
        user_address.setText(mProfile.getPlaceApp().getCity()+", "+mProfile.getPlaceApp().getCountry());


        AppDataService appDataService = new AppDataService(this);
        appDataService.MyFollowers(TOKEN, new AppDataService.FollowersResponseListener() {
            @Override
            public void onSuccess(ArrayList<Profile> followers) {
                mFollowers.clear();
                mFollowers.addAll(followers);
                user_followers.setText(String.valueOf(mFollowers.size()));
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(UserProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        appDataService.MeFollowing(TOKEN, new AppDataService.FollowersResponseListener() {
            @Override
            public void onSuccess(ArrayList<Profile> following) {
                mFollowing.clear();
                mFollowing.addAll(following);
                user_following.setText(String.valueOf(mFollowing.size()));
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(UserProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}