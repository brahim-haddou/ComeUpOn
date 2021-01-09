package com.example.comeupon;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
            Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<>(ImageLogo, "Logo_image");
            pairs[1] = new Pair<View,String>(TextLogo,"Logo_text");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
            startActivity(intent,options.toBundle());
            finish();
        }, TIME_OUT);

    }
}