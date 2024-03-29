package com.example.lenovo.myapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.myapp.Activity.ReviewActivity;
import com.example.lenovo.myapp.Auxiliaries.Constants;
import com.example.lenovo.myapp.Classes.Activity;
import com.example.lenovo.myapp.Classes.Wishlist;
import com.example.lenovo.myapp.R;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;



public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.MyViewHolder> {

    DatabaseReference mUser;
    GoogleSignInAccount acct;
    SharedPreferences sharedPreferences;
    Context mContext;
    List<Activity> imageList;
    Button review;
    Activity destination_activity;
    boolean facebook,google,email;
    boolean flag=true;
    String fullId=null;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, rupee;
        public ImageView activity_image;
        public ImageButton heart;
        public RatingBar ratingBar;
        int position;
        int match=-1;
        String fullId;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.activity_name);
            rupee=view.findViewById(R.id.activity_rupee);
//            count = (TextView) view.findViewById(R.id.count);
            activity_image = (ImageView) view.findViewById(R.id.activityimage);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
            ratingBar=view.findViewById(R.id.actRating);
            review=view.findViewById(R.id.review);
            heart = view.findViewById(R.id.heart);
            mUser = FirebaseDatabase.getInstance().getReference(Constants.USERS_DATABASE_PATH_UPLOADS);
            acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            heart.setOnClickListener(this);
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, ReviewActivity.class);
                    intent.putExtra("id",imageList.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });



        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View view) {

            position=getAdapterPosition();

            facebook = sharedPreferences.getBoolean("Facebook",false);
            google = sharedPreferences.getBoolean("Google",false);
            email = sharedPreferences.getBoolean("Email",false);
            if(facebook){
                Profile profile =  Profile.getCurrentProfile();
                final String facebookID = profile.getId();
                Query query = mUser.orderByChild("email").equalTo(facebookID);
                query.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            if(match!=position){
                                flag=true;
                            }
                            for(DataSnapshot ds: dataSnapshot1.child("wishlist").getChildren()){

                                Wishlist wishlist = ds.getValue(Wishlist.class);
                                if(wishlist.getActId().equals(imageList.get(position).getActivityId())){
                                    if((wishlist.getOrgId().equals(imageList.get(position).getOrganizerId()))){
                                        Toast.makeText(mContext,"already added",Toast.LENGTH_SHORT).show();
                                        flag = false;
                                        match=position;
                                        break;
                                    }
                                }
                            }
//                            String id=imageList.get(position).getActivityId()+""+imageList.get(position).getOrganizerId();
                            if(flag) {
                                Wishlist wishlist = new Wishlist(imageList.get(position).getActivityId(), imageList.get(position).getOrganizerId());
                                String Key = mUser.push().getKey();
                                mUser.child(dataSnapshot1.getKey()).child("wishlist").child(Key).setValue(wishlist);
                                heart.setImageTintList(ColorStateList.valueOf(Color.RED));
                                Toast.makeText(mContext," added to wishlist",Toast.LENGTH_SHORT).show();
                                // fullId = imageList.get(position).getActivityId() + "" + imageList.get(position).getOrganizerId();
                                flag=false;
                                match=position;
                            }else{
                                //Toast.makeText(getApplicationContext(),"Already addedd wishlist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else if(google && acct!=null){

                final String googleMail = acct.getEmail();
                Query query = mUser.orderByChild("email").equalTo(googleMail);
                query.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            if(match!=position){
                                flag=true;
                            }
                            for(DataSnapshot ds: dataSnapshot1.child("wishlist").getChildren()){

                                Wishlist wishlist = ds.getValue(Wishlist.class);
                                if(wishlist.getActId().equals(imageList.get(position).getActivityId())){
                                    if((wishlist.getOrgId().equals(imageList.get(position).getOrganizerId()))){
                                        Toast.makeText(mContext,"already added",Toast.LENGTH_SHORT).show();

                                        flag = false;
                                        match=position;
                                        break;
                                    }
                                }
                            }
//                            String id=imageList.get(position).getActivityId()+""+imageList.get(position).getOrganizerId();
                            if( flag) {
                                Wishlist wishlist = new Wishlist(imageList.get(position).getActivityId(), imageList.get(position).getOrganizerId());
                                String Key = mUser.push().getKey();
                                mUser.child(dataSnapshot1.getKey()).child("wishlist").child(Key).setValue(wishlist);
                                heart.setImageTintList(ColorStateList.valueOf(Color.RED));
                                Toast.makeText(mContext," added to wishlist",Toast.LENGTH_SHORT).show();

                                // fullId = imageList.get(position).getActivityId() + "" + imageList.get(position).getOrganizerId();
                                flag=false;
                                match=position;
                            }else{
                                //Toast.makeText(getApplicationContext(),"Already addedd wishlist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{


            }

        }
    }

    public CompletedAdapter(Context mContext, List<Activity> activityList, SharedPreferences sharedPreferences) {

        this.mContext=mContext;
        this.imageList=activityList;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public CompletedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_card, parent, false);

        return new CompletedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CompletedAdapter.MyViewHolder holder, int position) {

        destination_activity = imageList.get(position);
        holder.name.setText(destination_activity.getActivityName());
        // holder.count.setText(album.getNumOfSongs() + " songs");
        //holder.activity_image.setImageURI(Uri.parse(destination_activity.getImages().getImg1()));

        holder.rupee.setText(destination_activity.getPrice()+"");
        // loading album cover using Glide library

        Glide.with(mContext)
                .load(destination_activity.getImages().getImg1())
                .placeholder(R.drawable.logo) // can also be a drawable
                .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.activity_image);

        holder.ratingBar.setRating((float)destination_activity.getRating());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
