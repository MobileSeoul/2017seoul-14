package com.kkard.seoulroad;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kkard.seoulroad.Festival.FActivity;
import com.kkard.seoulroad.Map.MapActivity;
import com.kkard.seoulroad.Plant.PlantActivity;
import com.kkard.seoulroad.Visit.VActivity;

/**
 * Created by KyungHWan on 2017-08-03.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    public TabPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                VActivity vActivity = new VActivity();
                return vActivity;
            case 1:
                FActivity factivity = new FActivity();
                return factivity;
            case 2:
                PlantActivity plantActivity = new PlantActivity();
                return plantActivity;
            case 3:
                MapActivity mapActivity = new MapActivity();
                return mapActivity;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
