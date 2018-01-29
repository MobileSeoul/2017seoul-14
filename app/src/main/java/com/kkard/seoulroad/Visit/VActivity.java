package com.kkard.seoulroad.Visit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.kkard.seoulroad.R;
import com.kkard.seoulroad.Recycler.Data;
import com.kkard.seoulroad.Recycler.ViewAdapter;
import com.kkard.seoulroad.utils.CustomProgressBar;
import com.kkard.seoulroad.utils.RequestHttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuGeun on 2017-08-30.
 */

public class VActivity extends Fragment {
    private FloatingActionButton fab1, fab2;
    private FloatingActionMenu fam;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Data> tmp;
    private static final String TAG_JSON = "whtnrms";
    private static final String TAG_INDEX = "index";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_COUNT = "count";
    private static final String TAG_DATE = "date";
    private static final String TAG_COMMENT = "content";

    CustomProgressBar cpb;

    private SwipeRefreshLayout swl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_v,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        cpb = new CustomProgressBar(context);
        cpb.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        fab1 = (FloatingActionButton)view.findViewById(R.id.material_design_floating_action_menu_item1);
        recyclerView = (RecyclerView)getView().findViewById(R.id.v_recycle_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ViewAdapter(tmp,context);
        recyclerView.setAdapter(adapter);
        swl = (SwipeRefreshLayout)getView().findViewById(R.id.swipe_layout);
        swl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData task = new GetData();
                task.execute(getString(R.string.server_php)+"v.php");
                swl.setRefreshing(false);
            }
        });
        GetData task = new GetData();
        task.execute(getString(R.string.server_php)+"v.php");
//////// 플로팅 액션 메뉴 //////////
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn = new Intent(v.getContext(), VRegitActivity.class);
                startActivity(itn);
                ((Activity)context).finish();

            }
        });
    }


    private class GetData extends AsyncTask<String,Void,List<String>> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cpb.show();
//            progressDialog = ProgressDialog.show(context,
//                    "Please Wait", null, true, true);
        }

        @Override
        protected List<String> doInBackground(String... param) {
            String serverURL = param[0];
            try {
                List<String> re = new ArrayList<String>();
                RequestHttpConnection rhc = new RequestHttpConnection();
                BufferedReader br = rhc.requestVInfo(serverURL);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                re.add(sb.toString().trim());
                rhc = new RequestHttpConnection();
                br = rhc.requestVInfo(getString(R.string.server_php) + "pager.php");
                sb = new StringBuilder();
                String liner;
                while ((liner = br.readLine()) != null) {
                    sb.append(liner);
                }
                re.add(sb.toString().trim());
                br.close();
                return re;
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            cpb.dismiss();
//            progressDialog.dismiss();
            adapter = new ViewAdapter(getData(s), context);
            recyclerView.setAdapter(adapter);
        }
    }
    private List<Data> getData(List<String> json) { // 방문록 부분 데이터 받아오는 곳 // 1 페이저 0 이미지

        List<Data> finalList = new ArrayList<>();
        Data data;
        List<List<String>> main;
        List<String> imageItems;
        // 페이저 부분
        try {
            JSONObject jsonObject = new JSONObject(json.get(1));
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            main = new ArrayList<>();
            data = new Data();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String index = item.getString(TAG_INDEX);
                Log.e("인덱스",index);
                String email = item.getString(TAG_EMAIL);
                Log.e("이메일",email);
                String image = item.getString(TAG_IMAGE);
                Log.e("이미지",image);
                String count = item.getString(TAG_COUNT);
                Log.e("좋아요",count);
                String date = item.getString(TAG_DATE);
                Log.e("날짜",date);
                String content = item.getString(TAG_COMMENT);
                Log.e("코멘트",content);
                imageItems = new ArrayList<>();
                imageItems.add(index);
                imageItems.add(email);
                imageItems.add(image);
                imageItems.add(count);
                imageItems.add(date);
                imageItems.add(content);
                main.add(imageItems);
            }
        data.setViewType(ViewAdapter.VIEW_TYPE_PAGER);
            data.setPagerImageList(main);
        finalList.add(data);
        }catch (JSONException e) {
            Log.d("@@@@@@@@", "showResult : ", e);}
        try {
            JSONObject jsonObject = new JSONObject(json.get(0));
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                main = new ArrayList<>();
                data = new Data();
                for (int j = 0; j<3;j++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    String index = item.getString(TAG_INDEX);
                    Log.e("인덱스",index);
                    String email = item.getString(TAG_EMAIL);
                    Log.e("이메일",email);
                    String image = item.getString(TAG_IMAGE);
                    Log.e("이미지",image);
                    String count = item.getString(TAG_COUNT);
                    Log.e("좋아요",count);
                    String date = item.getString(TAG_DATE);
                    Log.e("날짜",date);
                    String content = item.getString(TAG_COMMENT);
                    Log.e("코멘트",content);
                    imageItems = new ArrayList<>();
                    imageItems.add(index);
                    imageItems.add(email);
                    imageItems.add(image);
                    imageItems.add(count);
                    imageItems.add(date);
                    imageItems.add(content);
                    main.add(imageItems);
                    i++;
                    if(i==jsonArray.length())break;
                }
                data.setViewType(ViewAdapter.VIEW_TYPE_IMAGE);
                data.setvImageList(main);
                finalList.add(data);
                i=i-1;
            }
        }catch (JSONException e) {
            Log.d("@@@@@@@@", "showResult : ", e);}
        return finalList;
    }
}