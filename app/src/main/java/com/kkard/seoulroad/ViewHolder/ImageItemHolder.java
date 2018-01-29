package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-10-03.
 */

public class ImageItemHolder extends RecyclerView.ViewHolder {
    public ImageView imageView1, imageView2, imageView3;

    public ImageItemHolder(View itemView) {
        super(itemView);
        imageView1 = (ImageView) itemView.findViewById(R.id.list_image1);
        imageView2 = (ImageView) itemView.findViewById(R.id.list_image2);
        imageView3 = (ImageView) itemView.findViewById(R.id.list_image3);
    }
}
