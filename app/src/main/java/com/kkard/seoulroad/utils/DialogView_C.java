package com.kkard.seoulroad.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkard.seoulroad.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by KyungHWan on 2017-10-05.
 */

public class DialogView_C extends Dialog {

    private TextView mTitleView;
    private TextView mContentView;
    private TextView mIdView;
    private TextView mLikeCountView;
    private ImageView mImageView;
    private ImageView mXBtn;
    private TextView dateTv;

    private ImageButton mLikeButton;
    private ImageButton mMiddleButton;
    private Button confBtn;
    private Button cancleBtn;

    private RelativeLayout back;
    private LinearLayout inside;

    private String mImage;
    private String mTitle;
    private String mContent;
    private String mCount;
    private String mId;
    private String mDate;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private View.OnClickListener mMiddleClickListener;

    public final static int DIA_TYPE_IMAGE = 90;
    public final static int DIA_TYPE_CAMERA = 91;
    public final static int DIA_TYPE_MOD = 92;
    public final static int DIA_TYPE_MOD_CONF = 93;
    public final static int DIA_TYPE_MAP = 94;

    private int mcnt, pre;
    private int type ,img;
    private boolean isClickLike = false;
    private boolean isFirstTime = false;
    private String user_index;
    String serverURL, heart_toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        switch (type) {
            case DIA_TYPE_IMAGE:
                setContentView(R.layout.activity_c_dialogview);
                setLayout(type);
                setTitle(mTitle);
                setContent(mContent);
                setId(mId);
                setCount(mCount);
                setDate(mDate);
                setImage(mImage);
                break;
            case DIA_TYPE_CAMERA:
                setContentView(R.layout.activity_regit_dialog);
                setLayout(type);
                setClickListener(mLeftClickListener, mMiddleClickListener, mRightClickListener);
                break;
            case DIA_TYPE_MOD:
                setContentView(R.layout.layout_dialog_modify);
                setLayout(type);
                break;
            case DIA_TYPE_MOD_CONF:
                setContentView(R.layout.layout_dialog_modify_conf);
                setLayout(type);
                break;
            case DIA_TYPE_MAP:
                setContentView(R.layout.layout_dialog_map);
                setLayout(type);
                break;
        }
    }

    // 이미지 클릭시 생성자
    public DialogView_C(int type, final Context context, final String index,
                        String email, String image, String count,
                        String date, final String content) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        SharedPreferences pre = getContext().getSharedPreferences("UserInfo", MODE_PRIVATE);//user정보 저장 미니디비
        user_index = pre.getString("userindex", "index error");
        Log.e("유저 인덱스", user_index);
        this.type = type;
        mImage = image;
        mTitle = index;
        mId = email;
        mDate = date;
        mContent = content;
        new AsyncTask<Void, Void, List<String>>() {
            ProgressDialog progressDialog;
            String errorString = null;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getContext(),
                        "Please Wait", null, true, true);
            }

            @Override
            protected List<String> doInBackground(Void... param) {
                String serverURL = context.getString(R.string.server_php) + "requestlike.php";
                try {
                    List<String> re = new ArrayList<String>();
                    RequestHttpConnection rhc = new RequestHttpConnection();
                    BufferedReader br = rhc.requestImageInfo(serverURL, user_index, index);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    re.add(sb.toString().trim());
                    rhc = new RequestHttpConnection();
                    br = rhc.requestImageInfo(context.getString(R.string.server_php) + "requestcount.php", user_index, index);
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
            protected void onPostExecute(List<String> result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                Log.e("좋아요 눌러봤냐", result.get(0));
                Log.e("좋아요 몇개냐", result.get(1));
                mCount = result.get(1)+"명";
                setCount(mCount);
                switch (result.get(0)) {
                    case "null":
                        isFirstTime = true;
                        isClickLike = false;
                        break;
                    case "0":
                        isFirstTime = false;
                        isClickLike = false;
                        break;
                    case "1":
                        isFirstTime = false;
                        isClickLike = true;
                        break;
                }
                if (isClickLike) {
                    mLikeButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.love_btn));
                } else {
                    mLikeButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.love_btn_black));
                }
            }
        }.execute();
    }

    // 버튼 3개 생성자
    public DialogView_C(int type, Context context, View.OnClickListener photoClick, View.OnClickListener galleryClick, View.OnClickListener cancelClick) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.type = type;
        this.mLeftClickListener = photoClick;
        this.mMiddleClickListener = galleryClick;
        this.mRightClickListener = cancelClick;
    }

    // 수정 미완료 생성자, 수정 완료 생성자
    public DialogView_C(int type, Context context, View.OnClickListener confClick) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.type = type;
        this.mLeftClickListener = confClick;
    }
    // 구글 맵 다이얼로그
    public DialogView_C(int type, Context context,String title,String sub, int image, View.OnClickListener exitClick,View.OnClickListener phoneClick) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.type = type;
        this.mLeftClickListener = exitClick;
        this.mRightClickListener = phoneClick;
        this.mTitle = title;
        this.mContent =sub;
        this.img = image;
    }
    private void setDate(String date) {
        date = date.substring(0, 10);
        dateTv.setText(date);
    }

    private void setTitle(String title) {
        mTitleView.setText(title + "번째 방문자입니다 !");
    }

    private void setContent(String content) {
        mContentView.setText(content);
    }

    private void setCount(String count) {
        mLikeCountView.setText(count);
    }

    private void setId(String id) {
        mIdView.setText(id);
    }

    private void setImage(String image) {
        Picasso.with(getContext()).load(getContext().getString(R.string.server_image) + image).fit()
                .into(mImageView);
    }

    private void setClickListener(View.OnClickListener left, View.OnClickListener middle, View.OnClickListener right) {
        mLikeButton.setOnClickListener(left);
        mMiddleButton.setOnClickListener(middle);
        mXBtn.setOnClickListener(right);
    }

    /*
     * Layout
     */
    // 이미지 클릭 초기화 함수
    private void setLayout(int type) {
        switch (type) {
            case DIA_TYPE_IMAGE:
                dateTv = (TextView) findViewById(R.id.date_tv);
                mTitleView = (TextView) findViewById(R.id.dia_title);
                mContentView = (TextView) findViewById(R.id.dia_content);
                mLikeCountView = (TextView) findViewById(R.id.cnt_like);
                mIdView = (TextView) findViewById(R.id.dia_id);
                mImageView = (ImageView) findViewById(R.id.image_dialog);
                mLikeButton = (ImageButton) findViewById(R.id.btn_heart);
                mLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cnt = mLikeCountView.getText().toString();
                        cnt = cnt.substring(0, cnt.length() - 1);
                        mcnt = Integer.parseInt(cnt);
                        pre = mcnt;
                        if (isClickLike) { // 좋아요가 눌려있으면
                            mLikeButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.love_btn_black));
                            isClickLike = false;
                            mcnt--;
                            mLikeCountView.setText(Integer.toString(mcnt) + "명");
                        } else {
                            mLikeButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.love_btn));
                            isClickLike = true;
                            mcnt++;
                            mLikeCountView.setText(Integer.toString(mcnt) + "명");
                        }
                    }
                });
                mXBtn = (ImageView) findViewById(R.id.dia_x_btn);
                back = (RelativeLayout) findViewById(R.id.dialog_back);
                inside = (LinearLayout) findViewById(R.id.dialog_inside);
                inside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 아무것도 안함
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateLike();
                        dismiss();
                    }
                });
                mXBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateLike();
                        dismiss();
                    }
                });
                break;
            case DIA_TYPE_CAMERA:
                mLikeButton = (ImageButton) findViewById(R.id.takepicture);
                mMiddleButton = (ImageButton) findViewById(R.id.fromgallery);
                mXBtn = (ImageView) findViewById(R.id.camera_x_btn);
                back = (RelativeLayout) findViewById(R.id.camera_back);
                inside = (LinearLayout) findViewById(R.id.layout_camera);
                inside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 아무것도 안함
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                break;
            case DIA_TYPE_MOD:
                confBtn = (Button) findViewById(R.id.mod_conf);
                confBtn.setOnClickListener(mLeftClickListener);
                cancleBtn = (Button) findViewById(R.id.mod_cancle);
                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                break;
            case DIA_TYPE_MOD_CONF:
                confBtn = (Button) findViewById(R.id.modconf_conf);
                confBtn.setOnClickListener(mLeftClickListener);
                break;
            case DIA_TYPE_MAP:
                mTitleView = (TextView)findViewById(R.id.map_title);
                mTitleView.setText(mTitle);
                mContentView = (TextView)findViewById(R.id.map_sub_title);
                mContentView.setText(mContent);
                mImageView = (ImageView)findViewById(R.id.map_img);
                mImageView.setImageResource(img);
                mXBtn = (ImageView)findViewById(R.id.map_x_btn);
                mXBtn.setOnClickListener(mLeftClickListener);
                dateTv = (TextView)findViewById(R.id.map_phone);
                dateTv.setOnClickListener(mRightClickListener);
                break;
        }
    }

    private void updateLike() {
        Log.e("@@@@", "@@@@");
        if (isClickLike) heart_toggle = "1";
        else heart_toggle = "0";
        serverURL = "null";
        Log.e("isFirtTime", "" + isFirstTime + " pre : " + pre + " mcnt : " + mcnt);
        if (isFirstTime && pre != mcnt) { // 테이블에 없고, 좋아요 상태가 변했을때
            serverURL = getContext().getString(R.string.server_php) + "insertlike.php";
        }
        if (!isFirstTime && pre != mcnt) { //테이블에 있고, 좋아요 상태가 변했을때
            serverURL = getContext().getString(R.string.server_php) + "likeupdate.php";
        }
        if (!serverURL.equals("null")) {
            new AsyncTask<String, Void, String>() {
                ProgressDialog progressDialog;
                String errorString = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(getContext(),
                            "Please Wait", null, true, true);
                }

                @Override
                protected String doInBackground(String... param) {
                    try {
                        RequestHttpConnection rhc = new RequestHttpConnection();
                        rhc.updateLike(param[0], user_index, mTitle, heart_toggle, String.valueOf(mcnt));
                        Log.e(param[0], user_index + "/" + mTitle + "/" + heart_toggle + "/" + String.valueOf(mcnt));
                        return null;
                    } catch (Exception e) {
                        errorString = e.toString();
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressDialog.dismiss();
                    dismiss();
                }
            }.execute(serverURL);
            serverURL = "null";
        }
    }

    @Override
    public void onBackPressed() {
        updateLike();
        dismiss();
    }
}
