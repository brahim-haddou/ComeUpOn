package com.example.comeupon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    TextView TextLogo, DescText;
    View ImageLogo;
    TextInputLayout UserName, PassWord;
    Button SignIn, SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        ImageLogo = findViewById(R.id.imageLogo2);
        TextLogo = findViewById(R.id.textLogo2);
        DescText = findViewById(R.id.DescText1);
        UserName = findViewById(R.id.UserName2);
        PassWord = findViewById(R.id.PassWord2);
        SignUp = findViewById(R.id.SignUp1);
        SignIn = findViewById(R.id.SignIn1);

        SignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View,String>(ImageLogo, "Logo_image");
            pairs[1] = new Pair<View,String>(TextLogo,"Logo_text");
            pairs[2] = new Pair<View,String>(DescText,"Desc_text");
            pairs[3] = new Pair<View,String>(UserName,"UserName");
            pairs[4] = new Pair<View,String>(PassWord,"PassWord");
            pairs[5] = new Pair<View,String>(SignIn,"SignButton1");
            pairs[6] = new Pair<View,String>(SignUp,"SignButton2");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
            startActivity(intent,options.toBundle());
            finish();
        });
    }

}