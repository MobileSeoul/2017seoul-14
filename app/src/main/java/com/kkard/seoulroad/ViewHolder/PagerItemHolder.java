package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kkard.seoulroad.Circleindicator_C.LoopViewPager;
import com.kkard.seoulroad.R;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by SuGeun on 2017-10-03.
 */

public class PagerItemHolder extends RecyclerView.ViewHolder {

    public LoopViewPager viewPager;
    public CircleIndicator indicator;

    public PagerItemHolder(View view) {
        super(view);
        viewPager = (LoopViewPager) view.findViewById(R.id.slidesPager);
        indicator  = (CircleIndicator)view.findViewById(R.id.indicator);

    }
}
