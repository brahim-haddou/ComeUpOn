package com.example.comeupon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comeupon.Models.Profile;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.eventHomeList.EventListActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    TextView TextLogo, DescText;
    View ImageLogo;
    TextInputLayout FirstName, LastName ,UserName, PassWord, Email;
    Button SignIn, SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        ImageLogo = findViewById(R.id.imageLogo2);
        TextLogo = findViewById(R.id.textLogo2);
        DescText = findViewById(R.id.DescText1);


        FirstName = findViewById(R.id.SignUp_FirstName);
        LastName = findViewById(R.id.SignUp_LastName);
        UserName = findViewById(R.id.SignUp_UserName);
        Email = findViewById(R.id.SignUp_Email);
        PassWord = findViewById(R.id.SignUp_PassWord);


        SignUp = findViewById(R.id.SignUp1);
        SignIn = findViewById(R.id.SignIn1);

        SignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);

            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<>(ImageLogo, "Logo_image");
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

        SignUp.setOnClickListener(view -> {
            AppDataService appDataService = new AppDataService(SignUpActivity.this);
            appDataService.SignUp(
                    FirstName.getEditText().getText().toString(),
                    LastName.getEditText().getText().toString(),
                    UserName.getEditText().getText().toString(),
                    PassWord.getEditText().getText().toString(),
                    Email.getEditText().getText().toString(),
                    new AppDataService.SignUpResponseListener() {
                        @Override
                        public void onSuccess(String token) {
                            SharedPreferences preferences = SignUpActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            preferences.edit().putString("TOKEN",token).apply();
                            Intent intent = new Intent(SignUpActivity.this, CreateProfileActivity.class);
                            intent.putExtra("key", token);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}