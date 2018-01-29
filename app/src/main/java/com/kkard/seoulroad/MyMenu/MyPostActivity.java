package com.kkard.seoulroad.MyMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kkard.seoulroad.FragmentActivity;
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
 * Created by KyungHWan on 2017-10-06.
 */

public class MyPostActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextView toolbarTitle;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int pageNum;
    private Intent intent;
    private SharedPreferences miniDB;
    private String userIndex, userid;
    private static final String TAG_JSON = "whtnrms";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_LIKE = "like";
    private static final String TAG_DATE = "date";
    private static final String TAG_COMMENT = "comment";
    private List<Data> tmp;
    SwipeRefreshLayout srl;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    private Boolean isNetWork(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        srl = (SwipeRefreshLayout)findViewById(R.id.my_refresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData task = new GetData();
                task.execute(getString(R.string.server_php)+"mypost.php");
                srl.setRefreshing(false);
            }
        });
        intent = getIntent();
        miniDB = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userIndex = miniDB.getString("userindex", "유저 인덱스 오류");
        userid = miniDB.getString("userid", "유저 아이디 오류");
        Log.e("@@@@@@@@@@@@@@@" + userIndex, userid);

        pageNum = intent.getIntExtra("pageNum", 0); // 프래그먼트 기억
        toolbarTitle = (TextView) findViewById(R.id.text_toolbar);
        toolbarTitle.setText("내가 쓴 글");
        backBtn = (ImageButton) findViewById(R.id.btn_toolbar_back);
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.mypost_recycle_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ViewAdapter(tmp, context);
        recyclerView.setAdapter(adapter);
        GetData task = new GetData();
        task.execute(getString(R.string.server_php)+"mypost.php");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MyPostActivity.this, FragmentActivity.class);
                intent.putExtra("pageNum", pageNum);
                startActivity(intent);
                finish();
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MyPostActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... param) {
            String serverURL = param[0];
            try {
                RequestHttpConnection rhc = new RequestHttpConnection();
                BufferedReader br = rhc.requestCourseInfo(serverURL, userIndex);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            adapter = new ViewAdapter(getData(s), context);
            recyclerView.setAdapter(adapter);
        }
    }


    private List<Data> getData(String json) {
        List<Data> finalList = new ArrayList<>();
        Data data;
        List<String> content;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String image = item.getString(TAG_IMAGE);
                Log.e("이미지",image);
                String like = item.getString(TAG_LIKE);
                Log.e("좋아요",like);
                String date = item.getString(TAG_DATE);
                Log.e("날짜",date);
                String comment = item.getString(TAG_COMMENT);
                Log.e("코멘트",comment);
                data = new Data();
                content = new ArrayList<>();
                data.setViewType(ViewAdapter.VIEW_TYPE_POST);
                content.add(userid);
                content.add(image);
                content.add(like);
                content.add(date);
                content.add(comment);
                content.add("수정 가능");
                data.setmPostContent(content);
                finalList.add(data);
            }
        }catch (JSONException e) {
            Log.d("@@@@@@@@", "showResult : ", e);
        }
        return finalList;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(MyPostActivity.this, FragmentActivity.class);
        intent.putExtra("pageNum",pageNum);
        startActivity(intent);
        finish();
    }
}
