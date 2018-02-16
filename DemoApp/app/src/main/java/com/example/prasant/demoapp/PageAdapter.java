package com.example.prasant.demoapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int noOfTabs;

    public PageAdapter(FragmentManager fm,int noOfTabs) {
        super(fm);
        this.noOfTabs=noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:{
                tab1 t1=new tab1();
                return t1;
            }
            case 1:{
                tab2 t2=new tab2();
                return t2;
            }
            case 2:{
                tab3 t3=new tab3();
                return t3;
            }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
