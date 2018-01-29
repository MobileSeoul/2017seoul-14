package com.kkard.seoulroad;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by SuGeun on 2017-10-26.
 */

public class ApplicationBase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .add("Regular",Typekit.createFromAsset(this,"NotoSansKR-Regular-Hestia.otf"))
                .add("Bold",Typekit.createFromAsset(this,"NotoSansKR-Bold-Hestia.otf"))
                .add("DemiLight",Typekit.createFromAsset(this,"NotoSansKR-DemiLight-Hestia.otf"))
                .add("Light",Typekit.createFromAsset(this,"NotoSansKR-Light-Hestia.otf"))
                .add("Medium",Typekit.createFromAsset(this,"NotoSansKR-Medium-Hestia.otf"));
    }
}
