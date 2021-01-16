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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppDataService {


    public static final String BASE_URL_API = "http://192.168.1.4:8000/API/v1";
    public static final String BASE_URL = "http://192.168.1.4:8000";

    Context ctx;

    public AppDataService(Context ctx) {
        this.ctx = ctx;
    }

    public interface SignUpResponseListener{
        void onSuccess(String token);
        void onFailure(String error);
    }

    public void SignUp(String firstName, String lastName, String username, String password,String email, SignUpResponseListener signUpResponseListener) {
        JSONObject Json = new JSONObject();
        try {
            Json.put("email", email);
            Json.put("first_name", firstName);
            Json.put("last_name", lastName);
            Json.put("username", username);
            Json.put("password1", password);
            Json.put("password2", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL+"/rest-auth/registration/",
                Json,
                response -> {
                    try {
                        signUpResponseListener.onSuccess(response.getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> signUpResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }


    public interface LogInResponseListener{
        void onSuccess(String token);
        void onFailure(String error);
    }

    public void logIn(String username, String password, LogInResponseListener logInResponseListener) {
        JSONObject Json = new JSONObject();
        try {
            Json.put("username", username);
            Json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL+"/rest-auth/login/",
                Json,
                response -> {
                    try {
                        logInResponseListener.onSuccess(response.getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> logInResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface LogOutResponseListener{
        void onSuccess(String token);
        void onFailure(String error);
    }

    public void LogOut(String tk, LogOutResponseListener logOutResponseListener) {
        JSONObject token = new JSONObject();
        try {
            token.put("key", tk);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL+"/rest-auth/logout/",
                token,
                response -> logOutResponseListener.onSuccess(response.toString()),
                error -> logOutResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface CreateProfileResponseListener{
        void onSuccess(Profile profile);
        void onFailure(String error);
    }

    public void CreateProfile(String Key,JSONObject profile , CreateProfileResponseListener createProfileResponseListener) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL_API+"/Profile/me/",
                profile,
                response -> createProfileResponseListener.onSuccess(jsonObjectToProfile(response)),
                error -> createProfileResponseListener.onFailure(error.getMessage()))
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ Key);
                return headers;
            }
        };

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }


    public interface EventResponseListener{
        void onSuccess(ArrayList<Event> events);
        void onFailure(String error);
    }
    public void getEvents(String key, EventResponseListener eventResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL_API + "/Event/", null,
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
                                        BASE_URL+activityInfo.getString("image"),
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
                                    BASE_URL+event_owner.getString("image"),
                                    event_owner.getString("phone"),
                                    event_owner.getString("birthday"));

                            Event event_api = new Event(
                                    eventInfo.getInt("id"),
                                    eventInfo.getString("title"),
                                    BASE_URL+eventInfo.getString("image"),
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
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }



    public interface CreateEventResponseListener{
        void onSuccess(Event event);
        void onFailure(String error);
    }
    public void setEvent(String key, JSONObject body, CreateEventResponseListener createEventResponseListener){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL_API + "/Event/",body,
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
                                    BASE_URL+activityInfo.getString("image"),
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
                                BASE_URL+event_owner.getString("image"),
                                event_owner.getString("phone"),
                                event_owner.getString("birthday"));

                        Event event_api = new Event(
                                response.getInt("id"),
                                response.getString("title"),
                                BASE_URL+response.getString("image"),
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
                error -> createEventResponseListener.onFailure(error.getMessage())){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }




    public interface ProfileResponseListener{
        void onSuccess(Profile profile);
        void onFailure(String error);
    }
    public void getMyUserProfile(String key, ProfileResponseListener profileResponseListener){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL_API +"/Profile/me/",
                null,
                response -> profileResponseListener.onSuccess(jsonObjectToProfile(response)),
                error -> profileResponseListener.onFailure(error.getMessage())){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };
        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }


    public interface ParticipantsResponseListener{
        void onSuccess(ArrayList<Profile> participants);
        void onFailure(String error);
    }
    public void getParticipant(String key, int id, int state, ParticipantsResponseListener participantsResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL_API + "/Event/"+id+"/Participant/"+state+"/", null,
                response -> {
                    ArrayList<Profile> Participants = new ArrayList<>();
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            Participants.add(jsonObjectToProfile(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    participantsResponseListener.onSuccess(Participants);
                },
                error -> participantsResponseListener.onFailure(error.getMessage())){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface AcceptedParticipantsResponseListener{
        void onSuccess(Boolean participants);
        void onFailure(String error);
    }
    public void AcceptedParticipant(String key, int id_event,int id_Profile, AcceptedParticipantsResponseListener acceptedParticipantsResponseListener){
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("event_participant_id", id_event);
            JSON.put("user_participant_id", id_Profile);
            JSON.put("stat", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL_API + "/Event/"+id_event+"/Participant/"+id_Profile+"/",
                JSON,
                response -> {
                    Boolean state = Boolean.FALSE;
                        try {
                            state = response.getBoolean("stat");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    acceptedParticipantsResponseListener.onSuccess(state);
                },
                error -> acceptedParticipantsResponseListener.onFailure(error.getMessage())){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public interface RequestsParticipantsResponseListener{
        void onSuccess(Boolean participants);
        void onFailure(String error);
    }
    public void RequestsParticipant(String key, int id_event,int id_Profile, RequestsParticipantsResponseListener requestsParticipantsResponseListener){
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("event_participant_id", id_event);
            JSON.put("user_participant_id", id_Profile);
            JSON.put("stat", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL_API + "/Event/"+id_event+"/Participant/"+id_Profile+"/",
                JSON,
                response -> {
                    Boolean state = Boolean.FALSE;
                    try {
                        state = response.getBoolean("stat");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestsParticipantsResponseListener.onSuccess(state);
                },
                error -> requestsParticipantsResponseListener.onFailure(error.getMessage())){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+ key);
                return headers;
            }
        };

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }



    public interface FollowersResponseListener{
        void onSuccess(ArrayList<Profile> followers);
        void onFailure(String error);
    }
    public void getFollowers(int id,FollowersResponseListener followersResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL_API + "/Profile/"+id+"/Followers/",
                null,
                response -> {
                    ArrayList<Profile> followers = new ArrayList<>();
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            followers.add(jsonObjectToProfile(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    followersResponseListener.onSuccess(followers);
                },
                error -> followersResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }



    public interface FollowingResponseListener{
        void onSuccess(ArrayList<Profile> following);
        void onFailure(String error);
    }
    public void getFollowing(int id,FollowingResponseListener followingResponseListener){
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL_API + "/Profile/"+id+"/Follow/",
                null,
                response -> {
                    ArrayList<Profile> following = new ArrayList<>();
                    for (int i = 0; i < response.length() ; i++) {
                        try {
                            following.add(jsonObjectToProfile(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    followingResponseListener.onSuccess(following);
                },
                error -> followingResponseListener.onFailure(error.getMessage()));

        ApiCallSingleton.getInstance(ctx).addToRequestQueue(request);
    }



    private static Profile jsonObjectToProfile(JSONObject participant){
        try {
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

            return new Profile(
                    participant.getInt("id"),
                    user_placeAppObj,
                    user_Obj,
                    BASE_URL+participant.getString("image"),
                    participant.getString("phone"),
                    participant.getString("birthday"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
