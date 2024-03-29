package com.example.lenovo.myapp.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.myapp.Activity.DestinationClick;
import com.example.lenovo.myapp.Adapter.ActivityAdapter;
import com.example.lenovo.myapp.Auxiliaries.Constants;
import com.example.lenovo.myapp.Auxiliaries.RecyclerItemClickListener;
import com.example.lenovo.myapp.Classes.Destination;
import com.example.lenovo.myapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DestinationFragment extends Fragment {
    AdView adView;
    DatabaseReference mref;
    List<Destination> destinationList;
    RecyclerView recyclerView;
    ValueEventListener valueEventListener;
    ActivityAdapter adapter;


    public DestinationFragment() {
        // Required empty public constructor
    }

    public void onResume() {
        super.onResume();
        mref.addValueEventListener(valueEventListener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        adView = view.findViewById(R.id.adView);
        mref = FirebaseDatabase.getInstance().getReference(Constants.DESTINATION_DATABASE_PATH_UPLOADS);
        destinationList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.destintionRecycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DestinationFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        MobileAds.initialize(getActivity(), "ca-app-pub-4689037977247733~9439374585");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        // Inflate the layout for this fragment
        adView.loadAd(adRequest);
        adapter = new ActivityAdapter(getActivity(), destinationList);
        // Inflate the layout for this fragment
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                destinationList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Destination destination = dataSnapshot1.getValue(Destination.class);
                    destinationList.add(destination);
                }


                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), DestinationClick.class);
                i.putExtra("destinationName",destinationList.get(position).getDestName());
                i.putExtra("destinationImage",destinationList.get(position).getImage());

                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
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


}
