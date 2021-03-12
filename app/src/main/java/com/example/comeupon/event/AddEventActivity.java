package com.example.comeupon.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Activity;
import com.example.comeupon.Models.Event;
import com.example.comeupon.Models.PlaceApp;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.eventHomeList.EventListActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {

    private String TOKEN;
    private Profile mProfile;

    int eYear, eMonth, eDay;
    int EndHour, EndMinute;
    int StartHour, StartMinute;
    int cYear, cMonth, cDay;
    int cHour, cMinute;
    int cHour1, cMinute1;

    ImageView image;
    TextInputEditText title;
    TextInputEditText description;

    TextInputEditText eventDate;
    TextInputEditText startTime;
    TextInputEditText endTime;

    TextInputEditText address;


    TextInputLayout titleT;
    TextInputLayout descriptionT;

    TextInputLayout eventDateT;
    TextInputLayout startTimeT;
    TextInputLayout endTimeT;

    TextInputLayout addressT;


    Button add_activity_to_event;
    Button add_event;

    ProgressBar progressBar;

    protected EventActivityAdapter activityAdapter;
    protected RecyclerView activityRecyclerView;

    int PLACE_PICKER_REQUEST = 121;
    int IMAGE_PICKER_REQUEST_ACTIVITY = 131;
    int IMAGE_PICKER_REQUEST_EVENT = 111;


    PlaceApp eventPlaceApp;
    ArrayList<Activity> mEventActivities;
    ArrayList<Bitmap> mEventImagesActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_event);


        TOKEN = getIntent().getStringExtra("key");
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");

        image = findViewById(R.id.add_event_image);
        image.setOnClickListener(view1 -> selectImage(IMAGE_PICKER_REQUEST_EVENT));

        title = findViewById(R.id.add_event_title);
        description = findViewById(R.id.add_event_description);

        startTime = findViewById(R.id.add_event_start_time);
        endTime = findViewById(R.id.add_event_end_time);
        eventDate = findViewById(R.id.add_event_date);

        address = findViewById(R.id.add_event_address);

        titleT = findViewById(R.id.add_event_title_t);
        descriptionT = findViewById(R.id.add_event_description_t);

        startTimeT = findViewById(R.id.add_event_start_time_t);
        endTimeT = findViewById(R.id.add_event_end_time_t);
        eventDateT = findViewById(R.id.add_event_date_t);

        addressT = findViewById(R.id.add_event_address_t);

        add_activity_to_event = findViewById(R.id.add_event_add_activity);

        add_event = findViewById(R.id.add_event_add_event);
        add_event.setOnClickListener(view1 ->  addEventJson());
        progressBar = findViewById(R.id.add_event_progress_bar);


        Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cHour = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);
        cHour1 = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute1 = calendar.get(Calendar.MINUTE);

        eventDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1, i2) -> {
                       eYear = i;
                       eMonth = i1;
                       eDay = i2;
                       String sDate = eDay+"-"+(eMonth+1)+"-"+eYear;
                       eventDate.setText(sDate);
                    },cYear,cMonth,cDay);
            datePickerDialog.updateDate(eYear,eMonth,eDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });
        startTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1) -> {
                        StartHour = i;
                        StartMinute = i1;
                        Calendar calendar1 = Calendar.getInstance();
                        String sDate = Objects.requireNonNull(eventDate.getText()).toString().trim();
                        String[] strings = sDate.split("-");
                        int sDay = Integer.parseInt(strings[0]);
                        calendar1.set(Calendar.DAY_OF_MONTH,sDay);
                        calendar1.set(Calendar.HOUR_OF_DAY, StartHour);
                        calendar1.set(Calendar.MINUTE, StartMinute);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                        startTime.setText(dateFormat.format(calendar1.getTime()));

                    },cHour,cMinute, false);
            timePickerDialog.show();
        });

        endTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1) -> {
                        EndHour = i;
                        EndMinute = i1;
                        Calendar calendar1 = Calendar.getInstance();
                        String sDate = Objects.requireNonNull(eventDate.getText()).toString().trim();
                        String[] strings = sDate.split("-");
                        int sDay = Integer.parseInt(strings[0]);
                        calendar1.set(Calendar.DAY_OF_MONTH,sDay);
                        calendar1.set(Calendar.HOUR_OF_DAY, EndHour);
                        calendar1.set(Calendar.MINUTE, EndMinute);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                        endTime.setText(dateFormat.format(calendar1.getTime()));

                    },cHour1,cMinute1, false);
            timePickerDialog.show();
        });

        activityRecyclerView = findViewById(R.id.add_event_activities);
        activityRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mEventActivities = new ArrayList<>();
        mEventImagesActivities = new ArrayList<>();
        activityAdapter = new EventActivityAdapter(mEventActivities,mEventImagesActivities);
        activityRecyclerView.setLayoutManager(mLayoutManager);
        activityRecyclerView.setAdapter(activityAdapter);

    }

    public void getAddress(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(AddEventActivity.this,"ServiceRepair Exception",Toast.LENGTH_SHORT).show();
        }
        catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(AddEventActivity.this,"ServiceNotAvailable Exception",Toast.LENGTH_SHORT).show();
        }
    }
     Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                assert data != null;
                Place place = PlacePicker.getPlace(AddEventActivity.this, data);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            1);
                    Address address1 = addresses.get(0);
                    address.setText(address1.getAddressLine(0));
                    eventPlaceApp = new PlaceApp(
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
        else if(requestCode == IMAGE_PICKER_REQUEST_ACTIVITY) {
            if(resultCode == RESULT_OK){
                assert data != null;
                Uri path = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    add_activity_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == IMAGE_PICKER_REQUEST_EVENT) {
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
    }

    ImageView add_activity_image;
    public void addActivityToEvent(View view){

        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.add_activity);

        TextInputEditText add_activity_category;
        TextInputEditText add_activity_name;
        TextInputEditText activities_number;
        TextInputEditText participants_number;
        Button add_activity;


        add_activity_image = mDialog.findViewById(R.id.add_activity_image_item);
        add_activity_image.setOnClickListener(view1 -> selectImage(IMAGE_PICKER_REQUEST_ACTIVITY));

        add_activity_category = mDialog.findViewById(R.id.add_activity_category_item);
        add_activity_name = mDialog.findViewById(R.id.add_activity_name_item);
        activities_number = mDialog.findViewById(R.id.add_activity_activities_number);
        participants_number = mDialog.findViewById(R.id.add_activity_participants_number);

        add_activity = mDialog.findViewById(R.id.add_activity_add_btn);
        add_activity.setOnClickListener(v -> {
            mDialog.dismiss();
            Activity a = new Activity(1, Objects.requireNonNull(add_activity_name.getText()).toString(),
                    Objects.requireNonNull(add_activity_category.getText()).toString(),
                    "https://cdn.airplane-pictures.net/images/uploaded-images/2016/5/3/713405.jpg",
                    Integer.parseInt(Objects.requireNonNull(activities_number.getText()).toString()),
                    Integer.parseInt(Objects.requireNonNull(participants_number.getText()).toString()));
            mEventActivities.add(a);
            Bitmap bitmapActivity = ((BitmapDrawable)add_activity_image.getDrawable()).getBitmap();
            mEventImagesActivities.add(bitmapActivity);
            activityAdapter.notifyDataSetChanged();
        });

        mDialog.show();
    }

    void addEventJson() {
        if(!ProfileImage() || !EventTitle() || !EventDescription() || !EventDate() || !EventStartTime() || !EventEndTime() || !EventAddress()){
            return;
        }
        try{
            progressBar.setVisibility(View.VISIBLE);

            image.setOnClickListener(null);
            title.setFocusable(View.NOT_FOCUSABLE);
            title.setFocusableInTouchMode(false);
            description.setFocusable(View.NOT_FOCUSABLE);
            description.setFocusableInTouchMode(false);
            startTime.setOnClickListener(null);
            endTime.setOnClickListener(null);
            eventDate.setOnClickListener(null);
            address.setOnClickListener(null);
            add_activity_to_event.setOnClickListener(null);
            add_event.setOnClickListener(null);

            JSONObject eventJson = new JSONObject();
            // place_event
            JSONObject placeJson = new JSONObject();
            placeJson.put("address", eventPlaceApp.getAddress());
            placeJson.put("city",eventPlaceApp.getCity());
            placeJson.put("country",eventPlaceApp.getCountry());
            placeJson.put("lat",eventPlaceApp.getLat());
            placeJson.put("lan",eventPlaceApp.getLan());

            eventJson.put("place_event", placeJson);
            // activityEvent
            JSONArray activitiesJson = new JSONArray();

            int cpt=0;
            for (Activity activity:mEventActivities) {
                JSONObject activityJson = new JSONObject();
                activityJson.put("name", activity.getName());
                activityJson.put("category",activity.getCategory());
                activityJson.put("image",bitmapToSting(mEventImagesActivities.get(cpt)));
                activityJson.put("number_Activity",activity.getNumber_Activity());
                activityJson.put("number_Participant",activity.getNumber_Participant());
                activitiesJson.put(activityJson);
                cpt++;
            }

            eventJson.put("activityEvent", activitiesJson);
            // title
            eventJson.put("title", title.getText());
            // image
            Bitmap bitmapEvent = ((BitmapDrawable)image.getDrawable()).getBitmap();
            eventJson.put("image",bitmapToSting(bitmapEvent));
            // description
            eventJson.put("description", description.getText());
            // start_date
            eventJson.put("start_date", String.valueOf(LocalDateTime.of(eYear,(eMonth+1),eDay, StartHour, StartMinute,0)));
            // end_date
            eventJson.put("end_date", String.valueOf(LocalDateTime.of(eYear,(eMonth+1),eDay, EndHour, EndMinute,0)));

            Log.e("eventJson------------------------->\n", ":"+ eventJson.toString());

            AppDataService appDataService = new AppDataService(this);
            appDataService.setEvent(TOKEN ,eventJson, new AppDataService.CreateEventResponseListener() {
                @Override
                public void onSuccess(Event event) {
                    Toast.makeText(AddEventActivity.this, event.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("Event", event);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(AddEventActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (JSONException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void selectImage(int REQUEST){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST);
    }

    private String bitmapToSting(Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream =new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] b=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public boolean ProfileImage(){
        if(image.getDrawable() ==  null){
            image.setColorFilter(32);
            return false;
        }else {
            return true;
        }
    }
    public boolean EventTitle(){
        String password = title.getText().toString();
        if(password.isEmpty()){
            titleT.setError("Field cannot be empty");
            return false;
        }else {
            titleT.setError(null);
            titleT.setErrorEnabled(false);
            return true;
        }
    }
    public boolean EventDescription(){
        String password = descriptionT.getEditText().getText().toString();
        if(password.isEmpty()){
            descriptionT.setError("Field cannot be empty");
            return false;
        }else {
            descriptionT.setError(null);
            descriptionT.setErrorEnabled(false);
            return true;
        }
    }
    public boolean EventDate(){
        String password = eventDateT.getEditText().getText().toString();
        if(password.isEmpty()){
            eventDateT.setError("Field cannot be empty");
            return false;
        }else {
            eventDateT.setError(null);
            eventDateT.setErrorEnabled(false);
            return true;
        }
    }
    public boolean EventStartTime(){
        String password = startTimeT.getEditText().getText().toString();
        if(password.isEmpty()){
            startTimeT.setError("Field cannot be empty");
            return false;
        }else {
            startTimeT.setError(null);
            startTimeT.setErrorEnabled(false);
            return true;
        }
    }
    public boolean EventEndTime(){
        String password = endTimeT.getEditText().getText().toString();
        if(password.isEmpty()){
            endTimeT.setError("Field cannot be empty");
            return false;
        }else {
            endTimeT.setError(null);
            endTimeT.setErrorEnabled(false);
            return true;
        }
    }
    public boolean EventAddress(){
        String password = addressT.getEditText().getText().toString();
        if(password.isEmpty()){
            addressT.setError("Field cannot be empty");
            return false;
        }else {
            addressT.setError(null);
            addressT.setErrorEnabled(false);
            return true;
        }
    }
}