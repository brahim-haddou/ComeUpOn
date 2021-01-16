package com.example.comeupon;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comeupon.Models.Profile;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.eventHomeList.EventListActivity;

public class WelcomeActivity extends AppCompatActivity {

    Animation topAnimation, bottomAnimation, middleAnimation;
    TextView TextLogo, TextStart;
    View ImageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_amination);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        ImageLogo = findViewById(R.id.imageLogo);
        TextLogo = findViewById(R.id.textLogo);
        TextStart = findViewById(R.id.textStart);

        ImageLogo.setAnimation(topAnimation);
        TextLogo.setAnimation(middleAnimation);
        TextStart.setAnimation(bottomAnimation);

        int TIME_OUT = 1800;
        new Handler().postDelayed(() -> {

            //Retrieve token wherever necessary
            SharedPreferences preferences = WelcomeActivity.this.getSharedPreferences("MY_APP",Context.MODE_PRIVATE);
            String token  = preferences.getString("TOKEN",null);//second parameter default value.

            if(token == null){
                toSignIn();
            }else {

                AppDataService appDataService = new AppDataService(WelcomeActivity.this);
                appDataService.getMyUserProfile(token, new AppDataService.ProfileResponseListener() {
                    @Override
                    public void onSuccess(Profile profile) {
                        toEventList(token, profile);
                    }

                    @Override
                    public void onFailure(String error) {
                        toCreateProfile(token);
                    }
                });
            }

        }, TIME_OUT);


    }

    public void toSignIn(){
        Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<>(ImageLogo, "Logo_image");
        pairs[1] = new Pair<View,String>(TextLogo,"Logo_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }
    public void toEventList(String to, Profile pro){
        Intent intent = new Intent(WelcomeActivity.this, EventListActivity.class);
        intent.putExtra("key", to);
        intent.putExtra("Profile", pro);
        startActivity(intent);
        finish();
    }

    public void toCreateProfile(String to){
        Intent intent = new Intent(WelcomeActivity.this, CreateProfileActivity.class);
        intent.putExtra("key", to);
        startActivity(intent);
        finish();
    }
}