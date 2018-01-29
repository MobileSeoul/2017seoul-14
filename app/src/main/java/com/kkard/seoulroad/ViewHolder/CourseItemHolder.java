package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-10-21.
 */

public class CourseItemHolder extends RecyclerView.ViewHolder {
    public ImageView courseImage;
    public TextView courseTitle, courseContent;

    public CourseItemHolder(View itemView) {
        super(itemView);
        courseImage = (ImageView)itemView.findViewById(R.id.course_image);
        courseContent = (TextView)itemView.findViewById(R.id.course_content);
        courseTitle = (TextView)itemView.findViewById(R.id.course_title);
    }
}
