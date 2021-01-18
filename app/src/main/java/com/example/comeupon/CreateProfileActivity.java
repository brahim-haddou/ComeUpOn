package com.example.comeupon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comeupon.Models.PlaceApp;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.eventHomeList.EventListActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateProfileActivity extends AppCompatActivity {

    String TOKEN;

    ImageView image;
    TextInputEditText phone;
    TextInputEditText address;
    TextInputEditText birthday;
    Button create_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);


        TOKEN = getIntent().getStringExtra("key");

        image = findViewById(R.id.create_profile_image);
        phone = findViewById(R.id.create_profile_phone);
        address = findViewById(R.id.create_profile_address);
        birthday = findViewById(R.id.create_profile_birthday);
        create_btn = findViewById(R.id.create_profile_btn);

        image.setOnClickListener(view1 -> selectImage(IMAGE_PICKER_REQUEST_PROFILE));
        address.setOnClickListener(view -> getAddress());
        create_btn.setOnClickListener(view -> CreateProfileJson());


    }

    private void selectImage(int REQUEST){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST);
    }
    public void getAddress(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(CreateProfileActivity.this), PLACE_PICKER_REQUEST_PROFILE);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(CreateProfileActivity.this,"ServiceRepair Exception",Toast.LENGTH_SHORT).show();
        }
        catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(CreateProfileActivity.this,"ServiceNotAvailable Exception",Toast.LENGTH_SHORT).show();
        }
    }
    private String bitmapToSting(Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream =new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] b=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    Bitmap bitmap;
    PlaceApp ProfilePlaceApp;
    int PLACE_PICKER_REQUEST_PROFILE = 14321;
    int IMAGE_PICKER_REQUEST_PROFILE = 2425;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_PICKER_REQUEST_PROFILE) {
            if(resultCode == RESULT_OK) {
                assert data != null;
                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == PLACE_PICKER_REQUEST_PROFILE){
            if(resultCode == RESULT_OK) {
                assert data != null;
                Place place = PlacePicker.getPlace(CreateProfileActivity.this, data);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            1);
                    Address address1 = addresses.get(0);
                    address.setText(address1.getAddressLine(0));
                    ProfilePlaceApp = new PlaceApp(
                            1,
                            address1.getAddressLine(0),
                            address1.getLocality(),
                            address1.getCountryName(),
                            place.getLatLng().latitude,
                            place.getLatLng().longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void CreateProfileJson() {
        try{

            image.setOnClickListener(null);
            phone.setFocusable(View.NOT_FOCUSABLE);
            phone.setFocusableInTouchMode(false);
            address.setFocusable(View.NOT_FOCUSABLE);
            address.setFocusableInTouchMode(false);
            birthday.setOnClickListener(null);
            birthday.setFocusableInTouchMode(false);

            JSONObject ProfileJson = new JSONObject();
            // place_event
            JSONObject addressJson = new JSONObject();
            addressJson.put("address", ProfilePlaceApp.getAddress());
            addressJson.put("city",ProfilePlaceApp.getCity());
            addressJson.put("country",ProfilePlaceApp.getCountry());
            addressJson.put("lat",ProfilePlaceApp.getLat());
            addressJson.put("lan",ProfilePlaceApp.getLan());

            ProfileJson.put("address", addressJson);
            // title
            ProfileJson.put("phone", phone.getText());
            // image
            Bitmap bitmapEvent = ((BitmapDrawable)image.getDrawable()).getBitmap();
            ProfileJson.put("image",bitmapToSting(bitmapEvent));
            // description
            ProfileJson.put("birthday", birthday.getText());
            // start_date

            AppDataService appDataService = new AppDataService(this);
            appDataService.CreateProfile(TOKEN, ProfileJson, new AppDataService.CreateProfileResponseListener() {
                @Override
                public void onSuccess(Profile profile) {
                    Intent intent = new Intent(CreateProfileActivity.this, EventListActivity.class);
                    intent.putExtra("key", TOKEN);
                    intent.putExtra("Profile", profile);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(CreateProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (JSONException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}