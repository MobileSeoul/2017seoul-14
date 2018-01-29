package com.kkard.seoulroad.Circleindicator_C;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kkard.seoulroad.R;
import com.kkard.seoulroad.utils.DialogView_C;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KyungHWan on 2017-09-20.
 */

public class IndicatorAdapter extends PagerAdapter {
    private int mSize;
    private DialogView_C mDialog;
    private Context mcontext;
    private List<List<String>> pageritem;
    private ImageView imageView;

    public IndicatorAdapter(Context context, List<List<String>> pageritem) {
        mSize = 3;
        mcontext = context;
        this.pageritem=pageritem;
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, final int position) {
        imageView = new ImageView(view.getContext());
        Picasso.with(view.getContext())
                .load(view.getContext().getString(R.string.server_image)+pageritem.get(position).get(2)).fit()
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        View.OnClickListener mPagerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new DialogView_C(DialogView_C.DIA_TYPE_IMAGE,v.getContext(),pageritem.get(position).get(0)
                        ,pageritem.get(position).get(1),pageritem.get(position).get(2),
                        pageritem.get(position).get(3),pageritem.get(position).get(4),
                        pageritem.get(position).get(5)); // 페이저 눌렀을때 다이얼로그 전달
                mDialog.show();
                mDialog.setCanceledOnTouchOutside(false);
            }
        };
        imageView.setOnClickListener(mPagerListener);
        view.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }

}
