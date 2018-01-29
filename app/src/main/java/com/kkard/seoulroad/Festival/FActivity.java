package com.kkard.seoulroad.Festival;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkard.seoulroad.Calendar_C.DatePickerController;
import com.kkard.seoulroad.Calendar_C.DayPickerView;
import com.kkard.seoulroad.Calendar_C.SimpleMonthAdapter;
import com.kkard.seoulroad.R;
import com.kkard.seoulroad.Recycler.Data;
import com.kkard.seoulroad.Recycler.ViewAdapter;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by SuGeun on 2017-08-21.
 */

public class FActivity extends Fragment implements DatePickerController {

    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DayPickerView calendarView;
    List<Data> main,sub;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_f,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setData();
        context = getContext();

        calendarView = (DayPickerView) getView().findViewById(R.id.calendar_view);
        calendarView.setController(FActivity.this);

        recyclerView = (RecyclerView)getView().findViewById(R.id.fest_recycle_view);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ViewAdapter(selectData(5),context);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public int getMaxYear() {
        return 0;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        final int time = (month + day)%10;
        new AsyncTask<String,Void,Void>(){
            @Override
            protected void onProgressUpdate(Void... values) {
                recyclerView.swapAdapter(adapter,true);
            }

            @Override
            protected Void doInBackground(String... voids) {
                adapter = new ViewAdapter(selectData(time),context);
                publishProgress();
                return null;
            }
        }.execute();

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }
    private void setData() {

        main = new ArrayList<>();
        Data data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_TEXT_MAIN);
        List<String> content = new ArrayList<>();
        content.add("대한민국 환경조경대전 '광장의 재발견'");
        content.add("김형학 화훼 작가의 '서울로 자연의 철학'");
        content.add("서울로 100일의 꽃 세밀화전");
        content.add("서울로 가드너의 '정원이 놀다'");
        content.add("어린이 참가작 전시");
        data.setTextList("주요전시",content);
        main.add(data);
        data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_TEXT_MAIN);
        content = new ArrayList<>();
        content.add("서울로 아빠와 놀다(디자인파크개발");
        content.add("서울로 자연색이 놀다(7인의 컬러디자이너)");
        content.add("서울로 꽃이 놀다(송미진 박사)");
        content.add("서울로 같이 놀다(서울로 개발팀");
        content.add("성울로 나와 놀다(정규열 박사)");
        data.setTextList("참여전시",content);
        main.add(data);
        // 부수적인 것
        sub = new ArrayList<>();
        sub.add(getSub("10 : 00","100일의 식물이야기 산책 (개막)"));
        sub.add(getSub("10 : 00","100일의 식물이야기 산책"));
        sub.add(getSub("10 : 00","100년의 역사놀이"));
        sub.add(getSub("10 : 00","100일의 식물이야기 산책"));
        sub.add(getSub("10 : 00","100일의 꽃 그리기"));
        sub.add(getSub("10 : 00","서울로 가드닝 (폐막)"));
        sub.add(getSub("11 : 00","100일의 꽃 그리기"));  //6
        sub.add(getSub("13 : 00","100일의 인증사진"));
        sub.add(getSub("14 : 00","100일의 꽃 그리기"));
        sub.add(getSub("15 : 00","100일의 인증사진"));
        sub.add(getSub("16 : 00","100년의 역사놀이")); //10
        sub.add(getSub("16 : 00","100일의 식물이야기 산책"));
        sub.add(getSub("16 : 00","'오늘이' 인형체험"));
        sub.add(getSub("17 : 00","서울로 100일 잔치 및 오늘이 공연")); //13
        sub.add(getSub("17 : 00","'오늘이' 인형체험"));
        sub.add(getSub("19 : 00","서울로 담아가기"));

    }
    private List<Data> selectData(int time){
        List<Data> con = new ArrayList<>();
        con.add(main.get(time%2));
        con.add(sub.get(time%6));
        con.add(sub.get((time%4)+6));
        con.add(sub.get((time%3)+10));
        con.add(sub.get((time%3)+13));
        return con;
    }
    private Data getSub(String date,String content){
        Data data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_TEXT);
        data.setTextList(date,content);
        return data;
    }
}
