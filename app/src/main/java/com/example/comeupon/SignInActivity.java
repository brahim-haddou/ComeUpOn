package com.example.comeupon;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.HttpResponse;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.Models.User;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.eventHomeList.EventListActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {

    TextView TextLogo, DescText;
    View ImageLogo;
    TextInputLayout UserName, PassWord;
    Button SignIn, SignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ImageLogo = findViewById(R.id.imageLogo1);
        TextLogo = findViewById(R.id.textLogo1);
        DescText = findViewById(R.id.descText);
        UserName = findViewById(R.id.UserName);
        PassWord = findViewById(R.id.PassWord);
        SignIn = findViewById(R.id.SignIn);
        SignUp = findViewById(R.id.SignUp);

        SignUp.setOnClickListener(view -> {


            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<>(ImageLogo, "Logo_image");
            pairs[1] = new Pair<View,String>(TextLogo,"Logo_text");
            pairs[2] = new Pair<View,String>(DescText,"Desc_text");
            pairs[3] = new Pair<View,String>(UserName,"UserName");
            pairs[4] = new Pair<View,String>(PassWord,"PassWord");
            pairs[5] = new Pair<View,String>(SignIn,"SignButton1");
            pairs[6] = new Pair<View,String>(SignUp,"SignButton2");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this, pairs);
            startActivity(intent,options.toBundle());
            finish();
        });

        SignIn.setOnClickListener(view -> {
            if( !LoginUserName() || !LoginPassWord()){
                return;
            }
            AppDataService appDataService = new AppDataService(SignInActivity.this);
            appDataService.logIn(UserName.getEditText().getText().toString(),
                    PassWord.getEditText().getText().toString(),
                    new AppDataService.LogInResponseListener() {
                        @Override
                        public void onSuccess(String token) {
                            SharedPreferences preferences = SignInActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                            preferences.edit().putString("TOKEN",token).apply();

                            appDataService.getMyUserProfile(token, new AppDataService.ProfileResponseListener() {
                                @Override
                                public void onSuccess(Profile profile) {
                                    Intent intent = new Intent(SignInActivity.this, EventListActivity.class);
                                    intent.putExtra("key", token);
                                    intent.putExtra("Profile", profile);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(String error) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(SignInActivity.this, "userName or password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }

    public boolean LoginUserName(){
        String username = UserName.getEditText().getText().toString();
        if(username.isEmpty()){
            UserName.setError("Field cannot be empty");
            return false;
        }else {
            UserName.setError(null);
            UserName.setErrorEnabled(false);
            return true;
        }
    }
    public boolean LoginPassWord(){
        String password = PassWord.getEditText().getText().toString();
        if(password.isEmpty()){
            PassWord.setError("Field cannot be empty");
            return false;
        }else {
            PassWord.setError(null);
            UserName.setErrorEnabled(false);
            return true;
        }
    }
}