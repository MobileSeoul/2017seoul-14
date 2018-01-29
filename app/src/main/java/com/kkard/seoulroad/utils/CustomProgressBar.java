package com.kkard.seoulroad.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.kkard.seoulroad.R;

import java.util.List;

/**
 * Created by user on 2017-10-31.
 */

public class CustomProgressBar extends Dialog {
    public CustomProgressBar(Activity activity){
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_bar);
    }
    public CustomProgressBar(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_bar);
    }

}
