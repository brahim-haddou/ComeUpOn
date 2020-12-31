package com.example.comeupon.eventHomeList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.AddEventActivity;
import com.example.comeupon.R;
import com.example.comeupon.UserProfileActivity;
import com.example.comeupon.event.EventActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EventMapListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected EventItemAdapter mListAdapter;
    protected RecyclerView mRecyclerView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        // menu
        contentView = findViewById(R.id.content);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        navigationDrawer();


        // events
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.mipmap.logo);
        images.add(R.mipmap.logo);
        images.add(R.mipmap.logo);

        EventItem[] LIST_LOCATIONS = new EventItem[]
                {
                       new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224),
                       new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224),
                       new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224),
                       new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224),
                        new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224),
                        new EventItem(1,"888888888888", "12-13", "21 hoafe", images,39.937795, 116.387224)
                };

        mRecyclerView = findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);


        ArrayList<EventItem> mEventItem = new ArrayList<>(LIST_LOCATIONS.length);
        mEventItem.addAll(Arrays.asList(LIST_LOCATIONS));
        mListAdapter = new EventItemAdapter();
        mListAdapter.setMapLocations(mEventItem);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);

        mRecyclerView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if(isLastItemDisplaying(mRecyclerView)){
                // get data from server
                mEventItem.addAll(Arrays.asList(LIST_LOCATIONS));
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
        menuIcon.setOnClickListener( view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });
        animateNavigartionDrawer();
    }
    static final float END_SCALE = 0.7f;
    LinearLayout contentView;
    private void animateNavigartionDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1- END_SCALE);
                final float offsetScale = 1-diffScaledOffset;
                 contentView.setScaleX(offsetScale);
                 contentView.setScaleY(offsetScale);

                 final float xOffset = drawerView.getWidth() * slideOffset;
                 final float xOffsetDiff = contentView.getWidth() * diffScaledOffset/2;
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
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();

    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    public void test(View view) {
        Intent intent = new Intent(EventMapListActivity.this, EventActivity.class);
        startActivity(intent);
    }

    public void test1(View view) {
        Intent intent = new Intent(EventMapListActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void test2(View view) {
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.profile);
        mDialog.show();
    }
    public void test3(View view) {
        Intent intent = new Intent(EventMapListActivity.this, AddEventActivity.class);
        startActivity(intent);
    }
}