package com.example.comeupon;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity {

    TextInputEditText eventDate,startTime, endTime;
    int eYear, eMonth, eDay;
    int sHour, sMinute;
    int eHour, eMinute;
    int cYear, cMonth, cDay;
    int cHour, cMinute;


    TextInputEditText address;
    int PLACE_PICKER_REQEST = 121;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        startTime = findViewById(R.id.add_event_start_time);
        endTime = findViewById(R.id.add_event_end_time);
        eventDate = findViewById(R.id.add_event_date);
        address = findViewById(R.id.add_event_address);
        
        Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cHour = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);

        eventDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1, i2) -> {
                       eYear = i;
                       eMonth = i1;
                       eDay = i2;
                       String sDate = eDay+"-"+(eMonth+1)+"-"+eYear;
                       eventDate.setText(sDate);
                    },cYear,cMonth,cDay);
            datePickerDialog.updateDate(eYear,eMinute,eDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });
        startTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1) -> {
                        eHour = i;
                        eMinute = i1;
                        Calendar calendar1 = Calendar.getInstance();
                        String sDate = Objects.requireNonNull(eventDate.getText()).toString().trim();
                        String[] strings = sDate.split("-");
                        int sDay = Integer.parseInt(strings[0]);
                        calendar1.set(Calendar.DAY_OF_MONTH,sDay);
                        calendar1.set(Calendar.HOUR_OF_DAY,eHour);
                        calendar1.set(Calendar.MINUTE,eMinute);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                        startTime.setText(dateFormat.format(calendar1.getTime()));

                    },cHour,cMinute, false);
            timePickerDialog.show();
        });

        endTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                    (datePicker, i, i1) -> {
                        sHour = i;
                        sMinute = i1;
                        Calendar calendar1 = Calendar.getInstance();
                        String sDate = Objects.requireNonNull(eventDate.getText()).toString().trim();
                        String[] strings = sDate.split("-");
                        int sDay = Integer.parseInt(strings[0]);
                        calendar1.set(Calendar.DAY_OF_MONTH,sDay);
                        calendar1.set(Calendar.HOUR_OF_DAY,sHour);
                        calendar1.set(Calendar.MINUTE,sMinute);

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                        endTime.setText(dateFormat.format(calendar1.getTime()));

                    },cHour,cMinute, false);
            timePickerDialog.show();
        });
    }

    public void getAddress(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddEventActivity.this),PLACE_PICKER_REQEST);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(AddEventActivity.this,"ServiceRepaire Exception",Toast.LENGTH_SHORT).show();
        }
        catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(AddEventActivity.this,"SeerviceNotAvailable Exception",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQEST){
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addActivityToEvent(View view){
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.add_activity);
        mDialog.show();
    }
}