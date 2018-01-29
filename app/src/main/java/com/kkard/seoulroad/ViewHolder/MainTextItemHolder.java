package com.kkard.seoulroad.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-10-17.
 */

public class MainTextItemHolder extends RecyclerView.ViewHolder {

    public TextView mainTvTitle, mainTvContent1,
            mainTvContent2, mainTvContent3,
            mainTvContent5, mainTvContent4;

    public MainTextItemHolder(View view) {
        super(view);
        mainTvTitle = (TextView) view.findViewById(R.id.text_title_main);
        mainTvContent1 = (TextView) view.findViewById(R.id.text_content_main1);
        mainTvContent2 = (TextView) view.findViewById(R.id.text_content_main2);
        mainTvContent3 = (TextView) view.findViewById(R.id.text_content_main3);
        mainTvContent4 = (TextView) view.findViewById(R.id.text_content_main4);
        mainTvContent5 = (TextView) view.findViewById(R.id.text_content_main5);
    }
}
