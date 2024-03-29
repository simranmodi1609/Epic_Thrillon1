package com.example.lenovo.myapp.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.myapp.Adapter.DestinationClickAdapter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DestinationClick extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DestinationClickAdapter adapter;
    private List<String> activityList;
    private List<Activity> OrgActList;
    private List<Activity> unique;
    private DatabaseReference mDatabase,mDest;
    String DestinationName;
    String DestinationImage;
    TextView DestName;
    private static final long GAME_LENGTH_MILLISECONDS = 1000;
    private boolean mGameIsInProgress;
    private long mTimerMilliseconds;
    private InterstitialAd mInterstitialAd;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_click);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DestName= findViewById(R.id.love_music);
        Intent i =getIntent();
        DestinationName= i.getStringExtra("destinationName");
        DestinationImage=i.getStringExtra("destinationImage");
        DestName.setText(DestinationName);
        initCollapsingToolbar();
//        MobileAds.initialize(this, "ca-app-pub-4689037977247733~9439374585");
//        // Create the InterstitialAd and set the adUnitId.
//        mInterstitialAd = new InterstitialAd(this);
//        // Defined in res/values/strings.xml
//        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
//        startGame();
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                startGame();
//            }
//        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        activityList = new ArrayList<>();
        OrgActList = new ArrayList<>();
        unique=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.ACIVITY_DATABASE_PATH_UPLOADS);
        mDatabase.keepSynced(true);
        mDest = FirebaseDatabase.getInstance().getReference(Constants.DESTINATION_DATABASE_PATH_UPLOADS);
        mDest.keepSynced(true);
        Log.e("<<<<",mDatabase+"");


        try {

            Glide.with(this).load(DestinationImage).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        //showInterstitial();
                        Intent intent=new Intent(DestinationClick.this,DestinationActivity.class);
                        intent.putExtra("ActivityName",unique.get(position).getActivityName());
                        intent.putExtra("Destination",unique.get(position).getDestination());
                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DestinationClick.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        mDatabase.orderByChild("destination").equalTo(DestinationName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                activityList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Activity activity = postSnapshot.getValue(Activity.class);

                    if(DestinationName.equalsIgnoreCase(activity.getDestination())){
                        activityList.add(activity.getActivityName());
                        OrgActList.add(activity);
                    }

                }

                Set<String> set = new HashSet<>();
                set.addAll(activityList);
                activityList.clear();
                activityList.addAll(set);
                for(int i = 0 ; i<activityList.size();i++){
                    for(int j = 0;j<OrgActList.size();j++){
                        if(OrgActList.get(j).getActivityName().equals(activityList.get(i))){
                            unique.add(OrgActList.get(j));
                            break;
                        }
                    }
                }
                adapter = new DestinationClickAdapter(getApplicationContext(), unique);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                OrgActList.clear();
//                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
//                    Activity activity = postsnapshot.getValue(Activity.class);
//
//                    OrgActList.add(activity);
//                }
//                for(int i=0;i<activityList.size();i++){
//                    for(int j=0;j<OrgActList.size();j++){
//                        if(activityList.get(i).){
//                            unique.add(OrgActList.get(j));
//                            break;
//                        }
//                    }
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    collapsingToolbar.setTitle(DestinationName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    isShow = false;
                }
            }
        });
    }



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        // Start or resume the game.
        super.onResume();

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
//            // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onStart() {
        super.onStart();
    }

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
