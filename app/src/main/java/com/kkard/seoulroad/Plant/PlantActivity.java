package com.kkard.seoulroad.Plant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.kkard.seoulroad.R;

/**
 * Created by SuGeun on 2017-08-30.
 */

public class PlantActivity extends Fragment {
    private ImageButton qrcbtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_plant,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrcbtn = (ImageButton)view.findViewById(R.id.qrcbtn);
        qrcbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(v.getContext(), QRCameraActivity.class);
                startActivityForResult(itn,0);
            }
        });
    }
}
