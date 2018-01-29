package com.kkard.seoulroad.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kkard.seoulroad.R;
import com.kkard.seoulroad.Recycler.Data;
import com.kkard.seoulroad.Recycler.ViewAdapter;
import com.kkard.seoulroad.utils.RequestHttpConnection;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuGeun on 2017-10-22.
 */

public class CourseDetailActivity extends AppCompatActivity{
    private ImageButton backBtn;
    private TextView toolbarTitle;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Intent intent;
    private int courseNum; // 0 : 남산회현, 1 : 중림중천, 2 : 청파효창 3 : 서울역통합
    private String imgUri;
    private static final String TAG_JSON="whtnrms";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT ="content";
    List<Data> a;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        imgUri= getString(R.string.server_course_hdpi);
        intent = getIntent();
        context = getApplicationContext();
        if(context.getResources().getDisplayMetrics().densityDpi>240) {
            imgUri= getString(R.string.server_course_xhdpi);
            if(context.getResources().getDisplayMetrics().densityDpi>320){
                imgUri= getString(R.string.server_course_xxhdpi);
            }
        }
        toolbarTitle = (TextView)findViewById(R.id.text_toolbar);
        courseNum = intent.getIntExtra("courseNum",-1);
        switch (courseNum){
            case 0:
                toolbarTitle.setText("남산회현 코스");
                break;
            case 1:
                toolbarTitle.setText("중림중천 코스");
                break;
            case 2:
                toolbarTitle.setText("청파효창 코스");
                break;
            case 3:
                toolbarTitle.setText("서울역 통합 코스");
                break;
            default:
                toolbarTitle.setText("오류");
        }
        courseNum++;
        imgUri = imgUri+"course"+String.valueOf(courseNum)+"-";
        backBtn = (ImageButton) findViewById(R.id.btn_toolbar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
    });
        recyclerView = (RecyclerView)findViewById(R.id.course_recycle_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ViewAdapter(a,context);
        recyclerView.setAdapter(adapter);
        GetData task = new GetData();
        task.execute(getString(R.string.server_php)+"course.php");

    }
    private class GetData extends AsyncTask<String,Void,String>{ // AsyncTask<excute 인자(back 인자),onProgressUpdate인자,Backgro 리턴(Post인자)>
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() { // 메인 스레드 처음 부분 로딩 프로그래스 바 등등
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CourseDetailActivity.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected String doInBackground(String... param) { // 뒷 부분
            String serverURL = param[0];
            try {
                RequestHttpConnection rhc = new RequestHttpConnection();
                BufferedReader br = rhc.requestCourseInfo(serverURL,String.valueOf(courseNum));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
                br.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String datas) { // 메인 스레드 마무리
            super.onPostExecute(datas);
            progressDialog.dismiss();
            adapter = new ViewAdapter(getData(datas),context);
            recyclerView.setAdapter(adapter);
        }
    }
    private List<Data> getData(String json) { // 코스 번호에 따라서 다르게 디비 가져와야함
        List<Data> finalList = new ArrayList<>();
        Data data;
        List<String> contentList;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString(TAG_TITLE);
                Log.e("제목",title);
                String content = item.getString(TAG_CONTENT);
                data = new Data();
                contentList = new ArrayList<>(); // 이미지, 제목 , 내용 순서
                data.setViewType(ViewAdapter.VIEW_TYPE_COURSE);
                contentList.add(imgUri+String.valueOf(i+1)+".png");
                contentList.add(title);
                contentList.add(content);
                data.setmCourseContent(contentList);
                finalList.add(data);
            }
        }catch (JSONException e) {

            Log.d("@@@@@@@@", "showResult : ", e);
        }
        return finalList;
    }
}
