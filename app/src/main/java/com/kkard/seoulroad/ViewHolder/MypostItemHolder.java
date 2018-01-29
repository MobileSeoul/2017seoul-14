package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-10-20.
 */

public class MypostItemHolder extends RecyclerView.ViewHolder {

    public TextView mypostUserid,mypostLike,
            mypostDate,mypostCom;
    public ImageView mypostImg;
    public LinearLayout modify;


    public MypostItemHolder(View view) {
        super(view);
        mypostUserid = (TextView) view.findViewById(R.id.mypost_userid);
        mypostLike = (TextView) view.findViewById(R.id.mypost_like);
        mypostDate = (TextView) view.findViewById(R.id.mypost_date);
        mypostCom = (TextView) view.findViewById(R.id.mypost_comment);
        mypostImg = (ImageView) view.findViewById(R.id.mypost_img);
        modify = (LinearLayout)view.findViewById(R.id.layout_modify);
    }
}
