package com.kkard.seoulroad.Festival;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.kkard.seoulroad.Calendar_C.DatePickerController;
import com.kkard.seoulroad.Calendar_C.DayPickerView;
import com.kkard.seoulroad.Calendar_C.SimpleMonthAdapter;
import com.kkard.seoulroad.R;

import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by KyungHWan on 2017-09-19.
 */

public class DayPickerView_C extends DayPickerView {
    public static final String TAG = DayPickerView_C.class.getSimpleName();

    protected Context mContext;
    protected SimpleMonthAdapter mAdapter;
    private DatePickerController mController;
    protected int mCurrentScrollState = 0;
    protected long mPreviousScrollPosition;
    protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;

    public DayPickerView_C(Context context)
    {
        this(context, null);
    }

    public DayPickerView_C(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DayPickerView_C(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.setOverScrollMode(OVER_SCROLL_NEVER);
            init(context);
        }
    }

    public void setController(DatePickerController mController)
    {
        this.mController = mController;
        setUpAdapter();
        setAdapter(mAdapter);

        scrollToPosition(12 + Calendar.getInstance().get(Calendar.MONTH));

    }

    public void scrollToToday() {
        smoothScrollToPosition(12 + Calendar.getInstance().get(Calendar.MONTH));
        adjustHeight();
    }


    public void init(Context paramContext) {
        super.init(paramContext);
        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING) {
                    mAdapter.setDragging(true);
                } else if (newState == SCROLL_STATE_IDLE) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setDragging(false);
                        }
                    }, 150);


                    View firstChild = getChildAt(0);
                    float offset = getY() - firstChild.getY();
                    if (offset > firstChild.getMeasuredHeight() / 2) {
                        stopScroll();
                        smoothScrollBy(0, (int) (getChildAt(1).getY() - getY()+30));
                    } else {
                        stopScroll();
                        smoothScrollBy(0, -(int) offset+30);
                    }

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int firstItemHeight = 490;
                            if (getMeasuredHeight() != firstItemHeight) {
                              //  ResizeAnimation resizeAnimation = new ResizeAnimation(DayPickerView_C.this, firstItemHeight);
                             //   resizeAnimation.setDuration(300);
                              //  startAnimation(resizeAnimation);
                            }
                        }
                    }, 400);
                }
            }
        };
        setLayoutManager(new LinearLayoutManager(paramContext));
        mContext = paramContext;
        setUpListView();
    }


    protected void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new SimpleMonthAdapter(getContext(), mController, typedArray);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void adjustHeight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getChildAt(0) != null) {
                    int firstItemHeight = 520;
                   // ResizeAnimation resizeAnimation = new ResizeAnimation(DayPickerView_C.this, firstItemHeight);
                  //  resizeAnimation.setDuration(300);
                  //  startAnimation(resizeAnimation);
                }
            }
        }, 200);
    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);
    }

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays()
    {
        return mAdapter.getSelectedDays();
    }

    public void clearSelectedDays() {
        mAdapter.setSelectedDay(null);
    }

    public void setCountMap(HashMap<SimpleMonthAdapter.CalendarDay, Integer> countMap) {
        mAdapter.setCountMap(countMap);
        this.invalidate();
    }

    protected DatePickerController getController()
    {
        return mController;
    }

    protected TypedArray getTypedArray()
    {
        return typedArray;
    }
}
