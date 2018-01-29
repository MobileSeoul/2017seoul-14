package com.kkard.seoulroad.MyMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kkard.seoulroad.FragmentActivity;
import com.kkard.seoulroad.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

/**
 * Created by SuGeun on 2017-09-01.
 */

public class NoticeActivity extends AppCompatActivity{
    private ExpandableListView expandableListView;
    private ExpandableAdapter adapter;
    private TextView toolbarTitle;
    private ImageButton toolbarBack;
    private ArrayList<NoticeParentData> parentDatas;
    private ArrayList<NoticeChildData> childListDatas;
    private int pageNum;
    private Intent intent;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        intent = getIntent();
        pageNum = intent.getIntExtra("pageNum",0);
        expandableListView = (ExpandableListView)findViewById(R.id.expand_menu);
        toolbarTitle = (TextView)findViewById(R.id.text_toolbar);
        toolbarTitle.setText("게시판");
        toolbarBack = (ImageButton)findViewById(R.id.btn_toolbar_back);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(NoticeActivity.this, FragmentActivity.class);
                intent.putExtra("pageNum",pageNum);
                startActivity(intent);
                finish();
            }
        });
        setData();
        adapter = new ExpandableAdapter(this,parentDatas,childListDatas);
        expandableListView.setAdapter(adapter);
    }
    private void setData(){
        parentDatas = new ArrayList<NoticeParentData>();
        childListDatas = new ArrayList<NoticeChildData>();

        parentDatas.add(new NoticeParentData(ExpandableAdapter.TYPE_NOTICE,"꺼짐 오류 수정 버전 2.1 로 업데이트 되었습니다"));
        childListDatas.add(new NoticeChildData("꺼짐 오류 수정 버전 2.1 로 업데이트 되었습니다. 기존에 서울로드 자동로그인시 어플리케이션이 꺼지는 현상이 있었는데 오류 발견 후 수정하였습니다.\n감사합니다."));

        parentDatas.add(new NoticeParentData(ExpandableAdapter.TYPE_NOTICE,"페이스북 연동 기능 추가되었습니다. 로그인 창에서 페이스북 연동하기 버튼을 클릭하면"));
        childListDatas.add(new NoticeChildData("페이스북 연동 기능 추가되었습니다. 로그인 창에서 페이스북 연동하기 버튼을 클릭하면 자동으로 페이스북과 연결됩니다.\n많은 이용 바랍니다. 앞으로도 사용자들의 편의를 위하겠습니다.\n감사합니다."));

        parentDatas.add(new NoticeParentData(ExpandableAdapter.TYPE_QNA,"비밀번호를 수정하고 싶은데 어떻게 해야하나요?"));
        childListDatas.add(new NoticeChildData("서울로드에 회원가입 후 로그인을 하시면 우측 상단에 세줄 버튼이 있습니다. 누르시고 비밀번호 수정 메뉴를 선택하시면 됩니다.\n감사합니다."));

        parentDatas.add(new NoticeParentData(ExpandableAdapter.TYPE_QNA,"QR코드 인식이 안되요. 방법이 없나요?"));
        childListDatas.add(new NoticeChildData("주위가 어둡거나 카메라의 초점이 맞는지 확인해 보시길 바랍니다. 가끔 너무 어둡거나 너무 가까이에서 QR코드를 찍으려 하면 인식이 잘 되지 않는경우가 있습니다.\n감사합니다."));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(NoticeActivity.this, FragmentActivity.class);
        intent.putExtra("pageNum",pageNum);
        startActivity(intent);
        finish();
    }
}
