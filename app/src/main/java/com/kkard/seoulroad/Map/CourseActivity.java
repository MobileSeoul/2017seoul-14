package com.kkard.seoulroad.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kkard.seoulroad.FragmentActivity;
import com.kkard.seoulroad.R;
import com.kkard.seoulroad.Recycler.Data;
import com.kkard.seoulroad.Recycler.ViewAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuGeun on 2017-10-24.
 */

public class CourseActivity extends AppCompatActivity {
    private ImageButton backBtn;
    private TextView toolbarTitle;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Intent intent;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(CourseActivity.this, FragmentActivity.class);
        intent.putExtra("pageNum",3);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course);
    toolbarTitle = (TextView)findViewById(R.id.text_toolbar);
        toolbarTitle.setText("추천코스");
    backBtn = (ImageButton) findViewById(R.id.btn_toolbar_back);

    context = getApplicationContext();
    recyclerView = (RecyclerView)findViewById(R.id.course_recycle_view);
        recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    adapter = new ViewAdapter(getData(),context);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(CourseActivity.this, FragmentActivity.class);
            intent.putExtra("pageNum",3);
            startActivity(intent);
            finish();
        }
    });
}
    private List<Data> getData() {
        List<Data> finalList = new ArrayList<>();

        Data data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_MAIN_COURSE);
        data.setmImageId(R.drawable.course1);
        finalList.add(data);
        data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_MAIN_COURSE);
        data.setmImageId(R.drawable.course2);
        finalList.add(data);
        data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_MAIN_COURSE);
        data.setmImageId(R.drawable.course3);
        finalList.add(data);
        data = new Data();
        data.setViewType(ViewAdapter.VIEW_TYPE_MAIN_COURSE);
        data.setmImageId(R.drawable.course4);
        finalList.add(data);
        return finalList;
    }
}

