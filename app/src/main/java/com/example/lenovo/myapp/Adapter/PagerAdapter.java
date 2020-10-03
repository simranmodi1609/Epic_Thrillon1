package com.example.lenovo.myapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.lenovo.myapp.Fragments.Completed;
import com.example.lenovo.myapp.Fragments.Upcoming;



public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }
    @Override
    public int getCount() {
        return mNoOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                Upcoming upcoming=new Upcoming();
                return upcoming;
            case 1:
                Completed completed=new Completed();
                return completed;
            default:
                return null;
        }
    }
}
