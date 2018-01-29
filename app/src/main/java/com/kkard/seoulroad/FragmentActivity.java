package com.kkard.seoulroad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkard.seoulroad.MyMenu.ModifyActivity;
import com.kkard.seoulroad.MyMenu.MyLikeActivity;
import com.kkard.seoulroad.MyMenu.MyPostActivity;
import com.kkard.seoulroad.MyMenu.NoticeActivity;
import com.tsengvn.typekit.TypekitContextWrapper;


public class FragmentActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private LinearLayout drawerLayout;
    private ImageView Mymenu;
    private String userId, userName, user_index;
    private TextView drawerName, drawerId, drawerWrite, drawerLike, drawerNotice, drawerModify, drawerLogout;
    ///////////////Back 버튼 2번 종료 관련 변수////////////
    private final long FINISH_INTERVAL_TIME = 2000; //2초안에 Back 버튼 누르면 종료
    private long backPressedTime = 0;
    private int pageNum;
    private Intent intent;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isNetWork()){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("인터넷 연결을 확인해주세요");
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {finish();}});
            AlertDialog alert = alert_confirm.create();
            alert.setIcon(R.mipmap.icon);
            alert.setTitle("네트워크 연결 알림");
            alert.show();}
        else {
            setContentView(R.layout.activity_fragment);
            InitView();
            pageNum = new Intent(getIntent()).getIntExtra("pageNum", 0);

            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

            SharedPreferences pre = getSharedPreferences("UserInfo", MODE_PRIVATE);//user정보 저장 미니디비
            userId = pre.getString("userid", "id error");
            userName = pre.getString("username", "name error");
            user_index = pre.getString("userindex", "index error");

            tabLayout.addTab(tabLayout.newTab().setText("방문록"));
            tabLayout.addTab(tabLayout.newTab().setText("공연/행사"));
            tabLayout.addTab(tabLayout.newTab().setText("식물찾기"));
            tabLayout.addTab(tabLayout.newTab().setText("지도보기"));

            Typeface fontTypeFace = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

            for (int i = 0; i < tabLayout.getChildCount(); ++i) {
                View nextChild = tabLayout.getChildAt(i);
                if (nextChild instanceof TextView) {
                    TextView textViewToConvert = (TextView) nextChild;
                    textViewToConvert.setTypeface(fontTypeFace);
                    textViewToConvert.setScaleY((float) 1.05);
                }
            }

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            viewPager.setCurrentItem(pageNum);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Mymenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            });
            drawerId.setText(userId);
            drawerName.setText(userName);
            drawerWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(FragmentActivity.this, MyPostActivity.class);
                    intent.putExtra("pageNum", tabLayout.getSelectedTabPosition());
                    startActivity(intent);
                    finish();
                }
            });
            drawerLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(FragmentActivity.this, MyLikeActivity.class);
                    intent.putExtra("pageNum", tabLayout.getSelectedTabPosition());
                    startActivity(intent);
                    finish();
                }
            });
            drawerNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(FragmentActivity.this, NoticeActivity.class);
                    intent.putExtra("pageNum", tabLayout.getSelectedTabPosition());
                    startActivity(intent);
                    finish();
                }
            });
            drawerModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(FragmentActivity.this, ModifyActivity.class);
                    intent.putExtra("pageNum", tabLayout.getSelectedTabPosition());
                    startActivity(intent);
                    finish();
                }
            });
            drawerLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sh = getSharedPreferences("AutoINFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("isAuto", "false");
                    editor.apply();
                    startActivity(new Intent(FragmentActivity.this, LoginActivity.class));
                    finish();
                }
            });
            drawerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Nothing Do
                }
            });
        }
    }

    private void InitView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        Mymenu = (ImageView) findViewById(R.id.myMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerId = (TextView) findViewById(R.id.drawer_id);
        drawerName = (TextView) findViewById(R.id.drawer_name);
        drawerWrite = (TextView) findViewById(R.id.drawer_write);
        drawerLike = (TextView) findViewById(R.id.drawer_like);
        drawerModify = (TextView) findViewById(R.id.drawer_modify);
        drawerNotice = (TextView) findViewById(R.id.drawer_notice);
        drawerLogout = (TextView) findViewById(R.id.drawer_logout);
        drawerLayout = (LinearLayout) findViewById(R.id.layout_right_drawer);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "종료를 원하시면 뒤로 버튼을 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
