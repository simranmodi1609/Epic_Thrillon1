package com.example.lenovo.myapp.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.lenovo.myapp.Activity.ActivityClick;
import com.example.lenovo.myapp.Activity.ActivityDetails;
import com.example.lenovo.myapp.Activity.DestinationClick;
import com.example.lenovo.myapp.Adapter.ActivityOfTheDayAdapter;
import com.example.lenovo.myapp.Adapter.ImageAdapter;
import com.example.lenovo.myapp.Adapter.NearByYouAdapter;
import com.example.lenovo.myapp.Adapter.PopularDestinationAdapter;
import com.example.lenovo.myapp.Auxiliaries.Constants;
import com.example.lenovo.myapp.Auxiliaries.MyService;
import com.example.lenovo.myapp.Auxiliaries.RecyclerItemClickListener;
import com.example.lenovo.myapp.Classes.Activities;
import com.example.lenovo.myapp.Classes.Activity;
import com.example.lenovo.myapp.Classes.Destination;
import com.example.lenovo.myapp.R;
import com.example.lenovo.myapp.Upload;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.lenovo.myapp.Activity.Home_page.flagForLocation;
import static com.example.lenovo.myapp.Activity.Home_page.latitude;
import static com.example.lenovo.myapp.Activity.Home_page.longitude;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewfirst, recyclerViewsecond, recyclerViewthird;
    AdView adView;
    List<Activities> activityList;
    List<Activity> activityofthedayList;
    List<Activity> nearByActivityList;
    List<Destination> destinationList;
    Date currentDate;
    private BroadcastReceiver broadcastReceiver;
    Activity activity;
    com.example.lenovo.myapp.Classes.Location location;
    Upload u;
    DatabaseReference mref, mdatabse, mdestination;
    GridView gridView;
    ValueEventListener valueEventListener, activityValueEventListener, destinationValueEventListner;
    String formattedDate;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                latitude = intent.getExtras().getDouble("latitude");
                longitude = intent.getExtras().getDouble("longitude");
                mdatabse.addValueEventListener(activityValueEventListener);
            }
        };

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

        mref.addValueEventListener(valueEventListener);
        mdestination.addValueEventListener(destinationValueEventListner);
        mdatabse.addValueEventListener(activityValueEventListener);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },0000)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mref = FirebaseDatabase.getInstance().getReference(Constants.ACIVITIES_DATABASE_PATH_UPLOADS);
        mdatabse = FirebaseDatabase.getInstance().getReference(Constants.ACIVITY_DATABASE_PATH_UPLOADS);
        mdestination = FirebaseDatabase.getInstance().getReference(Constants.DESTINATION_DATABASE_PATH_UPLOADS);
        mref.keepSynced(true);
        mdatabse.keepSynced(true);
        mdestination.keepSynced(true);
        if (flagForLocation) {
            statusCheck();
        }
        currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(currentDate);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        adView = view.findViewById(R.id.adView);
//            MobileAds.initialize(getActivity(),"ca-app-pub-4689037977247733~9439374585");
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    .build();
//            adView.loadAd(adRequest);
        gridView = view.findViewById(R.id.gridview);
        activityList = new ArrayList<>();
        activityofthedayList = new ArrayList<>();
        nearByActivityList = new ArrayList<>();
        destinationList = new ArrayList<>();


        recyclerViewfirst = (RecyclerView) view.findViewById(R.id.recycler_view_first);
        recyclerViewfirst.setHasFixedSize(true);
        recyclerViewfirst.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewsecond = (RecyclerView) view.findViewById(R.id.recycler_view_second);
        recyclerViewsecond.setHasFixedSize(true);
        recyclerViewsecond.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewthird = (RecyclerView) view.findViewById(R.id.recycler_view_third);
        recyclerViewthird.setHasFixedSize(true);
        recyclerViewthird.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //  getSupportActionBar().setTitle(MainActivity.class.getSimpleName());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activityList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Activities activity = dataSnapshot1.getValue(Activities.class);
                    activityList.add(activity);
                }
                gridView.setAdapter(new ImageAdapter(getActivity(), activityList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), ActivityClick.class);
                intent.putExtra("ActivityName", activityList.get(position).getName());
                intent.putExtra("ActivityImage", activityList.get(position).getImage());
                startActivity(intent);
            }
        });

        activityValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activityofthedayList.clear();
                nearByActivityList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    activity = dataSnapshot1.getValue(Activity.class);

                    if (activity.getActivityDate().equals(formattedDate)) {
                        activityofthedayList.add(activity);
                    }

                    location = activity.getLocation();

                    double latDiff = Math.abs((location.getLatitude()) - latitude);
                    double longDiff = Math.abs((location.getLongitude()) - longitude);
                    if ((latDiff < 20) && (longDiff < 20)) {
                        nearByActivityList.add(activity);
                    }

                }
                recyclerViewfirst.setAdapter(new ActivityOfTheDayAdapter(getActivity(), activityofthedayList));
                NearByYouAdapter adapter = new NearByYouAdapter(getActivity(), nearByActivityList);
                recyclerViewsecond.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        destinationValueEventListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                destinationList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Destination destination = dataSnapshot1.getValue(Destination.class);
                    destinationList.add(destination);
                }
                recyclerViewthird.setAdapter(new PopularDestinationAdapter(getActivity(), destinationList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        recyclerViewfirst.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewfirst, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), ActivityDetails.class);
                i.putExtra("activityId", activityofthedayList.get(position).getId());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewsecond.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewsecond, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), ActivityDetails.class);
                i.putExtra("activityId", nearByActivityList.get(position).getId());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        recyclerViewthird.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewthird, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), DestinationClick.class);
                i.putExtra("destinationName", destinationList.get(position).getDestName());
                i.putExtra("destinationImage", destinationList.get(position).getImage());
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        flagForLocation = false;
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent i = new Intent(getActivity(), MyService.class);
        getActivity().stopService(i);
    }
}


