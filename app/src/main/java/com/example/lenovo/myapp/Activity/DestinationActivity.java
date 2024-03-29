package com.example.lenovo.myapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.lenovo.myapp.Adapter.Activity_DestinationAdapter;
import com.example.lenovo.myapp.Auxiliaries.Constants;
import com.example.lenovo.myapp.Auxiliaries.RecyclerItemClickListener;
import com.example.lenovo.myapp.Classes.Activity;
import com.example.lenovo.myapp.R;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DestinationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Activity_DestinationAdapter adapter;
    private List<Activity> imageList;

    private static final long GAME_LENGTH_MILLISECONDS = 1000;
    private boolean mGameIsInProgress;
    private long mTimerMilliseconds;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;
    private DatabaseReference mref;
    private String activityName,destinationName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        MobileAds.initialize(this, "ca-app-pub-4689037977247733~9439374585");

        mref= FirebaseDatabase.getInstance().getReference(Constants.ACIVITY_DATABASE_PATH_UPLOADS);
        // Create the InterstitialAd and set the adUnitId.
//        mInterstitialAd = new InterstitialAd(this);
//        // Defined in res/values/strings.xml
//        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
//        startGame();
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                startGame();
//            }
//        });

        sharedPreferences = getSharedPreferences(AskForSignin.My_pref, Context.MODE_PRIVATE);



        activityName=getIntent().getStringExtra("ActivityName");
        destinationName=getIntent().getStringExtra("Destination");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Activity activity=dataSnapshot1.getValue(Activity.class);
                    if(activity.getActivityName().equalsIgnoreCase(activityName) && activity.getDestination().equalsIgnoreCase(destinationName)){
                        imageList.add(activity);
                    }
                }
                adapter = new Activity_DestinationAdapter(getApplicationContext(), imageList,sharedPreferences);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.recycleview);
        imageList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //prepareImage();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                       // showInterstitial();
                        // do whatever
                        Intent i = new Intent(DestinationActivity.this,ActivityDetails.class);
                        i.putExtra("activityId",imageList.get(position).getId());
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }
//
//    private void createTimer(final long milliseconds) {
//        // Create the game timer, which counts down to the end of the level
//        // and shows the "retry" button.
//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//        }
//
//
//
//        mCountDownTimer = new CountDownTimer(milliseconds, 50) {
//            @Override
//            public void onTick(long millisUnitFinished) {
//                mTimerMilliseconds = millisUnitFinished;
//
//            }
//
//            @Override
//            public void onFinish() {
//                mGameIsInProgress = false;
//
//            }
//        };
//    }

    @Override
    public void onResume() {
        // Start or resume the game.
        super.onResume();
//
//        if (mGameIsInProgress) {
//            resumeGame(mTimerMilliseconds);
//        }
    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
     //   mCountDownTimer.cancel();
        super.onPause();
    }

//    private void showInterstitial() {
//        // Show the ad if it's ready. Otherwise toast and restart the game.
//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
//            startGame();
//        }
//    }
//
//    private void startGame() {
//        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
//        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mInterstitialAd.loadAd(adRequest);
//        }
//
//
//        resumeGame(GAME_LENGTH_MILLISECONDS);
//    }
//
//    private void resumeGame(long milliseconds) {
//        // Create a new timer for the correct length and start it.
//        mGameIsInProgress = true;
//        mTimerMilliseconds = milliseconds;
//        createTimer(milliseconds);
//        mCountDownTimer.start();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


