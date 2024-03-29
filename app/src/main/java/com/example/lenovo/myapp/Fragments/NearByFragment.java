package com.example.lenovo.myapp.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lenovo.myapp.Auxiliaries.Constants;
import com.example.lenovo.myapp.Classes.Activity;
import com.example.lenovo.myapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.lenovo.myapp.Activity.Home_page.latitude;
import static com.example.lenovo.myapp.Activity.Home_page.longitude;


public class NearByFragment extends Fragment {
    private GoogleMap mMap;
    private MapView mapView;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressBar progressBar;
    LocationManager lm;

    ValueEventListener activityValueEventListener;
    DatabaseReference mref;
    List<Activity> activityList;
    List<com.example.lenovo.myapp.Classes.Location> locationList;



    ProgressDialog b;


    public NearByFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_near_by, container, false);
        progressBar= rootView.findViewById(R.id.progressBar2);
        mref = FirebaseDatabase.getInstance().getReference(Constants.ACIVITY_DATABASE_PATH_UPLOADS);
        mref.keepSynced(true);
        locationList = new ArrayList<>();
        activityList = new ArrayList<>();
         lm= (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        mapView = (MapView) rootView.findViewById(R.id.map);

        if(mapView!= null) {
            mapView.onCreate(savedInstanceState);
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }






                activityValueEventListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            locationList.clear();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Activity activity =dataSnapshot1.getValue(Activity.class);
                            com.example.lenovo.myapp.Classes.Location location = activity.getLocation();
                            double latDiff = Math.abs((location.getLatitude()) - latitude);
                            double longDiff = Math.abs((location.getLongitude()) - longitude);
                            if((latDiff < 20) && (longDiff < 20)){
                                locationList.add(location);
                                activityList.add(activity);
                            }
                        }

                                mapView.getMapAsync(new OnMapReadyCallback() {
                                    @SuppressLint("MissingPermission")
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        mMap = googleMap;

                                        // For showing a move to my location button
                                        mMap.setMyLocationEnabled(true);
                                        // For dropping a marker at a point on the Map
                                        LatLng sydney = new LatLng(latitude, longitude);
                                        mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location").snippet("You are Here !!!!!!!"));

                                        for(int i = 0; i<locationList.size();i++){
                                            LatLng loc = new LatLng(locationList.get(i).getLatitude(),locationList.get(i).getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(loc).title(activityList.get(i).getActivityName()).snippet(activityList.get(i).getDestination()));
                                        }

                                        // For zooming automatically to the location of the marker
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                    }
                                });




                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };


            } else {
                progressBar.isShown();

            }



        return rootView;

    }









    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        mref.addValueEventListener(activityValueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}