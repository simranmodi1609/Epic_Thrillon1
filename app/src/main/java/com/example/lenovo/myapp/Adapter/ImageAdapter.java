package com.example.lenovo.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.myapp.Auxiliaries.CircleTransform;
import com.example.lenovo.myapp.Classes.Activities;
import com.example.lenovo.myapp.R;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final List<Activities> activityList;
    ImageView imageview;


    public ImageAdapter(Context c,List<Activities> activityList) {
        mContext = c;
        this.activityList = activityList;
    }

    public int getCount() {
        return activityList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid= new View(mContext);
            grid = inflater.inflate(R.layout.homepage_buttons,null);

            TextView textView = (TextView)grid.findViewById(R.id.home_text);
             imageview = (ImageView)grid.findViewById(R.id.home_image);
            textView.setText(activityList.get(position).getName());
            // imageview.setImageResource(Imageid[position]);

            Glide.with(getApplicationContext()).load(activityList.get(position).getImage()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(getApplicationContext())).into(imageview);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }







}