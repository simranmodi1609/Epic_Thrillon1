package com.example.lenovo.myapp.Adapter;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.myapp.Classes.Destination;
import com.example.lenovo.myapp.R;

import java.util.List;


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context context;
    private List<Destination> destinations;
    Destination destination;
    ViewHolder viewHolder;
    View v;


    public ActivityAdapter(Context context, List<Destination> destinations) {
        this.destinations = destinations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_dest_card, parent, false);
         viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        destination = destinations.get(position);
        viewHolder =holder;
        viewHolder.textViewName.setText(destination.getDestName());

        //Glide.with(context).load(destination.getImage()).into(holder.imageView);
        Glide.with(context)
                .load(destination.getImage())
                .placeholder(R.drawable.logo) // can also be a drawable
                .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(viewHolder.imageView);


    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}