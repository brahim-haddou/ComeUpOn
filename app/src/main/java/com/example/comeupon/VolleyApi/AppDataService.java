package com.example.comeupon.VolleyApi;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comeupon.Models.Activity;
import com.example.comeupon.Models.Event;
import com.example.comeupon.Models.PlaceApp;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppDataService {


    public static final String BASE_URL = "http://192.168.1.7:8000/API/v1";

    Context ctx;

    public AppDataService(Context ctx) {
        this.ctx = ctx;
    }

    public interface EventResponseListener{
        void onSuccess(ArrayList<Event> events);
        void onFailure(String error);
    }
    public void getEvents(EventResponseListener eventResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL + "/Event/", null,
                response -> {
                    ArrayList<Event> LIST_EVENT = new ArrayList<>();
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            JSONObject eventInfo = response.getJSONObject(i);
                            JSONObject event_place = eventInfo.getJSONObject("place_event");
                            PlaceApp event_placeAppObj = new PlaceApp( event_place.getInt("id"),
                                    event_place.getString("address"),
                                    event_place.getString("city"),
                                    event_place.getString("country"),
                                    event_place.getDouble("lat"),
                                    event_place.getDouble("lan"));

                            JSONArray event_activities = eventInfo.getJSONArray("activityEvent");
                            List<Activity> event_activity = new ArrayList<>();
                            for (int j = 0; j <event_activities.length() ; j++) {
                                JSONObject activityInfo = event_activities.getJSONObject(j);
                                Activity activityObj =  new Activity(
                                        activityInfo.getInt("id"),
                                        activityInfo.getString("name"),
                                        activityInfo.getString("category"),
                                        "http://192.168.1.7:8000"+activityInfo.getString("image"),
                                        activityInfo.getInt("number_Activity"),
                                        activityInfo.getInt("number_Participant"));
                                event_activity.add(activityObj);
                            }
                            JSONObject event_owner = eventInfo.getJSONObject("owner");
                            JSONObject owner_place = event_owner.getJSONObject("address");
                            PlaceApp user_placeAppObj = new PlaceApp(owner_place.getInt("id"),
                                    owner_place.getString("address"),
                                    owner_place.getString("city"),
                                    owner_place.getString("country"),
                                    owner_place.getDouble("lat"),
                                    owner_place.getDouble("lan"));
                            JSONObject user = event_owner.getJSONObject("user");
                            User user_Obj = new User(
                                    user.getInt("id"),
                                    user.getString("username"),
                                    user.getString("first_name"),
                                    user.getString("last_name"),
                                    user.getString("email"));

                            Profile mOwner = new Profile(
                                    event_owner.getInt("id"),
                                    user_placeAppObj,
                                    user_Obj,
                                    "http://192.168.1.7:8000"+event_owner.getString("image"),
                                    event_owner.getString("phone"),
                                    event_owner.getString("birthday"));

                            Event event_api = new Event(
                                    eventInfo.getInt("id"),
                                    eventInfo.getString("title"),
                                    "http://192.168.1.7:8000"+eventInfo.getString("image"),
                                    eventInfo.getString("description"),
                                    LocalDateTime.parse(eventInfo.getString("start_date"), DateTimeFormatter.ISO_DATE_TIME),
                                    LocalDateTime.parse(eventInfo.getString("end_date"), DateTimeFormatter.ISO_DATE_TIME),
                                    mOwner,
                                    event_placeAppObj,
                                    event_activity.toArray(new Activity[0]));
                            LIST_EVENT.add(event_api);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        eventResponseListener.onSuccess(LIST_EVENT);
                    }
                },
                error -> eventResponseListener.onFailure(error.getMessage())
        );
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface CreateEventResponseListener{
        void onSuccess(Event event);
        void onFailure(String error);
    }
    public void setEvent(JSONObject body, CreateEventResponseListener createEventResponseListener){
        Log.e("eventJson------------------------->\n", body.toString());
        Log.d("eventJson------------------------->\n", body.toString());
        Log.i("eventJson------------------------->\n", body.toString());
        Log.v("eventJson------------------------->\n", body.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/Event/",body,
                response -> {
                    try {
                        JSONObject event_place = response.getJSONObject("place_event");
                        Log.e("event_place",event_place.toString());
                        PlaceApp event_placeAppObj = new PlaceApp( event_place.getInt("id"),
                                event_place.getString("address"),
                                event_place.getString("city"),
                                event_place.getString("country"),
                                event_place.getDouble("lat"),
                                event_place.getDouble("lan"));

                        Log.e("------>event_place", event_placeAppObj.toString());
                        JSONArray event_activities = response.getJSONArray("activityEvent");
                        List<Activity> event_activity = new ArrayList<>();
                        for (int j = 0; j <event_activities.length() ; j++) {
                            JSONObject activityInfo = event_activities.getJSONObject(j);
                            Activity activityObj =  new Activity(
                                    activityInfo.getInt("id"),
                                    activityInfo.getString("name"),
                                    activityInfo.getString("category"),
                                    "http://192.168.1.7:8000"+activityInfo.getString("image"),
                                    activityInfo.getInt("number_Activity"),
                                    activityInfo.getInt("number_Participant"));
                            event_activity.add(activityObj);
                        }
                        JSONObject event_owner = response.getJSONObject("owner");
                        JSONObject owner_place = event_owner.getJSONObject("address");
                        PlaceApp user_placeAppObj = new PlaceApp(owner_place.getInt("id"),
                                owner_place.getString("address"),
                                owner_place.getString("city"),
                                owner_place.getString("country"),
                                owner_place.getDouble("lat"),
                                owner_place.getDouble("lan"));
                        JSONObject user = event_owner.getJSONObject("user");
                        User user_Obj = new User(
                                user.getInt("id"),
                                user.getString("username"),
                                user.getString("first_name"),
                                user.getString("last_name"),
                                user.getString("email"));

                        Profile mOwner = new Profile(
                                event_owner.getInt("id"),
                                user_placeAppObj,
                                user_Obj,
                                "http://192.168.1.7:8000"+event_owner.getString("image"),
                                event_owner.getString("phone"),
                                event_owner.getString("birthday"));

                        Event event_api = new Event(
                                response.getInt("id"),
                                response.getString("title"),
                                "http://192.168.1.7:8000"+response.getString("image"),
                                response.getString("description"),
                                LocalDateTime.parse(response.getString("start_date"), DateTimeFormatter.ISO_DATE_TIME),
                                LocalDateTime.parse(response.getString("end_date"), DateTimeFormatter.ISO_DATE_TIME),
                                mOwner,
                                event_placeAppObj,
                                event_activity.toArray(new Activity[0]));

                        createEventResponseListener.onSuccess(event_api);
                    } catch (JSONException e) {
                        Log.e("JSONException---------------------------","--");
                        createEventResponseListener.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> createEventResponseListener.onFailure(error.getMessage()));
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }


//    {
//        @Override
//        public byte[] getBody() {
//        String mRequestBody = body.toString();
//        return mRequestBody == null ? null : mRequestBody.getBytes(StandardCharsets.UTF_8);
//    }
//
//        @Override
//        public String getBodyContentType() {
//        return "application/json; charset=utf-8";
//    }
//    }


    public interface ProfileResponseListener{
        void onSuccess(Profile profile);
        void onFailure(String error);
    }
    public void getProfiles(int id, ProfileResponseListener profileResponseListener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://192.168.1.7:8000/API/v1/Profile/"+id+"/",null,
                response -> {
                        try {
                            JSONObject user_place = response.getJSONObject("address");
                            PlaceApp user_placeAppObj = new PlaceApp(
                                    user_place.getInt("id"),
                                    user_place.getString("address"),
                                    user_place.getString("city"),
                                    user_place.getString("country"),
                                    user_place.getDouble("lat"),
                                    user_place.getDouble("lan"));
                            JSONObject user = response.getJSONObject("user");
                            User user_Obj = new User(
                                    user.getInt("id"),
                                    user.getString("username"),
                                    user.getString("first_name"),
                                    user.getString("last_name"),
                                    user.getString("email"));

                            Profile profile = new Profile(
                                    response.getInt("id"),
                                    user_placeAppObj,
                                    user_Obj,
                                    "http://192.168.1.7:8000"+response.getString("image"),
                                    response.getString("phone"),
                                    response.getString("birthday"));
                            profileResponseListener.onSuccess(profile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }, error -> profileResponseListener.onFailure(error.getMessage()));
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface ParticipantsResponseListener{
        void onSuccess(ArrayList<Profile> participants);
        void onFailure(String error);
    }

    public void getParticipant(int id,ParticipantsResponseListener participantsResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL + "/Event/"+id+"/Participant/", null,
                response -> {
                    ArrayList<Profile> Participants = new ArrayList<>();
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            JSONObject participant = response.getJSONObject(i);
                            JSONObject user_place = participant.getJSONObject("address");
                            PlaceApp user_placeAppObj = new PlaceApp(
                                    user_place.getInt("id"),
                                    user_place.getString("address"),
                                    user_place.getString("city"),
                                    user_place.getString("country"),
                                    user_place.getDouble("lat"),
                                    user_place.getDouble("lan"));
                            JSONObject user = participant.getJSONObject("user");
                            User user_Obj = new User(
                                    user.getInt("id"),
                                    user.getString("username"),
                                    user.getString("first_name"),
                                    user.getString("last_name"),
                                    user.getString("email"));

                            Profile profile = new Profile(
                                    participant.getInt("id"),
                                    user_placeAppObj,
                                    user_Obj,
                                    "http://192.168.1.7:8000"+participant.getString("image"),
                                    participant.getString("phone"),
                                    participant.getString("birthday"));
                            Participants.add(profile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    participantsResponseListener.onSuccess(Participants);
                },
                error -> participantsResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }



}
