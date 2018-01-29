package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-10-24.
 */

public class MainCourseItemHolder extends RecyclerView.ViewHolder {
public ImageView mainCourseImage;

public MainCourseItemHolder(View itemView) {
        super(itemView);
        mainCourseImage = (ImageView)itemView.findViewById(R.id.course_main_image);
        }
}
