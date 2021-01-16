package com.example.comeupon.eventHomeList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Event;
import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.example.comeupon.SignInActivity;
import com.example.comeupon.UserProfileActivity;
import com.example.comeupon.VolleyApi.AppDataService;
import com.example.comeupon.event.AddEventActivity;
import com.example.comeupon.event.EventActivity;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class EventListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EventItemAdapter.OnEventListener {



    private String TOKEN;
    private Profile mProfile;

    protected EventItemAdapter mListAdapter;
    protected RecyclerView mRecyclerView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageView menuIcon;
    ImageView user_image;
    TextView user_full_name;

    ArrayList<Event> LIST_EVENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);


        TOKEN = getIntent().getStringExtra("key");
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");


        // menu
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        user_image     = navigationView.findViewById(R.id.menu_header_image);
        user_full_name = navigationView.findViewById(R.id.menu_header_full_name);

        menuIcon = findViewById(R.id.menu_icon);

        navigationDrawer();

        // event

        mRecyclerView = findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        LIST_EVENT = new ArrayList<>();
        mListAdapter = new EventItemAdapter(LIST_EVENT,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);



        AppDataService appDataService = new AppDataService(this);
        appDataService.getEvents(TOKEN,new AppDataService.EventResponseListener() {
            @Override
            public void onSuccess(ArrayList<Event> events) {
                LIST_EVENT.clear();
                LIST_EVENT.addAll(events);
                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EventListActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (isLastItemDisplaying(mRecyclerView)) {
                // get data from server
                LIST_EVENT.addAll(LIST_EVENT);
                mListAdapter.notifyDataSetChanged();
                Toast.makeText(this, "wsenlaa", Toast.LENGTH_SHORT).show();
                //
            }
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menuIcon.setOnClickListener(view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else{
                drawerLayout.openDrawer(GravityCompat.START);
                user_image     = drawerLayout.findViewById(R.id.menu_header_image);
                user_full_name = drawerLayout.findViewById(R.id.menu_header_full_name);

                Picasso.get().load(mProfile.getImage()).into(user_image);
                user_full_name.setText(mProfile.getUser().getFirst_name()+" "+mProfile.getUser().getLast_name());
            }
        });
        animateNavigationDrawer();
    }

    static final float END_SCALE = 0.7f;
    LinearLayout contentView;

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();

    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    int ADD_EVENT = 321;
    public void AddEvent(View view) {
        Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
        intent.putExtra("Profile", mProfile);
        intent.putExtra("key", TOKEN);
        startActivityForResult(intent, ADD_EVENT);
    }

    public void UserProfile(View view) {
        Intent intent = new Intent(EventListActivity.this, UserProfileActivity.class);
        intent.putExtra("Profile", mProfile);
        intent.putExtra("key", TOKEN);
        startActivity(intent);
    }

    public void test2(View view) {
        AppDataService appDataService = new AppDataService(EventListActivity.this);
        appDataService.LogOut(TOKEN, new AppDataService.LogOutResponseListener() {
            @Override
            public void onSuccess(String token) {
                Intent intent = new Intent(EventListActivity.this, SignInActivity.class);
                startActivity(intent);
                SharedPreferences preferences = EventListActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                preferences.edit().putString("TOKEN",null).apply();
                finish();
            }
            @Override
            public void onFailure(String error) {

            }
            });
    }

    public void test3(View view) {
        Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(EventListActivity.this, EventActivity.class);
        intent.putExtra("Event", LIST_EVENT.get(position));
        intent.putExtra("Profile", mProfile);
        intent.putExtra("key", TOKEN);
        Toast.makeText(EventListActivity.this, mProfile.getUser().getUsername(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EVENT) {
            if(resultCode == RESULT_OK) {
                assert data != null;
                Event event = (Event) data.getSerializableExtra("Event");
                LIST_EVENT.add(event);
                mListAdapter.notifyDataSetChanged();

            }
        }
    }
}