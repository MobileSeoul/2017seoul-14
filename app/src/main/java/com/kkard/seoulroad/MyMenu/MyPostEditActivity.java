package com.kkard.seoulroad.MyMenu;


        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.net.ConnectivityManager;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.kkard.seoulroad.R;
        import com.kkard.seoulroad.utils.DialogView_C;
        import com.kkard.seoulroad.utils.RequestHttpConnection;
        import com.squareup.picasso.Picasso;
        import com.tsengvn.typekit.TypekitContextWrapper;


/**
 * Created by SuGeun on 2017-10-25.
 */

public class MyPostEditActivity extends AppCompatActivity {
    private ImageButton toolbarBack;
    private TextView toolbarTitle, userId, like, date;
    private DialogView_C mdialog;
    private EditText comment;
    private ImageView img;
    private Intent intent;
    private Button enter;
    private int lineColor;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private Boolean isNetWork() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetWork()) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("인터넷 연결을 확인해주세요");
            alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alert = alert_confirm.create();
            alert.setIcon(R.mipmap.icon);
            alert.setTitle("네트워크 연결 알림");
            alert.show();
        } else {
            setContentView(R.layout.layout_my_post_edit);
            intent = getIntent();
            InitView();
            lineColor = Color.parseColor("#E73A62");
            comment.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
            SetListener();
        }
    }

    @Override
    public void onBackPressed() {
        mdialog = new DialogView_C(DialogView_C.DIA_TYPE_MOD, MyPostEditActivity.this, confListener);
        mdialog.show();
    }

    private void InitView() {
        toolbarBack = (ImageButton) findViewById(R.id.btn_toolbar_back);
        toolbarTitle = (TextView) findViewById(R.id.text_toolbar);
        userId = (TextView) findViewById(R.id.mypostedit_userid);
        like = (TextView) findViewById(R.id.mypostedit_like);
        date = (TextView) findViewById(R.id.mypostedit_date);
        comment = (EditText) findViewById(R.id.mypostedit_comment);
        img = (ImageView) findViewById(R.id.mypostedit_img);
        enter = (Button) findViewById(R.id.mypostedit_enter);
    }

    private void SetListener() {
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog = new DialogView_C(DialogView_C.DIA_TYPE_MOD, MyPostEditActivity.this, confListener);
                mdialog.show();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //디비 부분 : comment update 해야함
                mdialog = new DialogView_C(DialogView_C.DIA_TYPE_MOD_CONF, MyPostEditActivity.this, confListener);
                mdialog.show();
            }
        });
        toolbarTitle.setText("수정하기");
        userId.setText(intent.getStringExtra("userId"));
        like.setText(intent.getStringExtra("like"));
        date.setText(intent.getStringExtra("date").substring(0,10));
        comment.setText(intent.getStringExtra("comment"));
        Picasso.with(getApplicationContext())
                .load(getString(R.string.server_image) + intent.getStringExtra("img")).fit()
                .into(img);
    }

    private View.OnClickListener confListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            new AsyncTask<Void, Void, Void>() {
                String up_content;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    up_content = comment.getText().toString();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    RequestHttpConnection rhc = new RequestHttpConnection();
                    rhc.updateImageContent("http://stou2.cafe24.com/php/imagecontentupdate.php", intent.getStringExtra("img"), up_content);
                    Log.e(intent.getStringExtra("img"), up_content);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    mdialog.dismiss();
                    finish();
                }
            }.execute();

        }
    };
}

