package com.kkard.seoulroad.Plant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kkard.seoulroad.FragmentActivity;
import com.kkard.seoulroad.R;

/**
 * Created by KyungHWan on 2017-09-20.
 */

public class QRCameraActivity extends AppCompatActivity{
    private IntentIntegrator qrscanner;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private TextView toolbarTitle;
    private ImageButton toolbarBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcamera);
        toolbarBack = (ImageButton)findViewById(R.id.btn_toolbar_back);
        toolbarTitle = (TextView)findViewById(R.id.text_toolbar);
        qrscanner = new IntentIntegrator(this);
        qrscanner.setCaptureActivity(CaptureActivityAnyOrientation.class);
        qrscanner.setOrientationLocked(true);
        qrscanner.initiateScan();
        toolbarTitle.setText("식물 찾기");
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCameraActivity.this, FragmentActivity.class);
                intent.putExtra("pageNum",2);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(QRCameraActivity.this, FragmentActivity.class);
        intent.putExtra("pageNum",2);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0)finish();//카메라 상태에서 뒤로가기 버튼 눌렀을시 카메라 종료
        else {//결과값있을시 웹뷰에 url 값 주고 띄우기
            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result == null) {
                } else {
                    mWebView = (WebView) findViewById(R.id.qr_wv);
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebSettings = mWebView.getSettings();
                    mWebSettings.setJavaScriptEnabled(true);
                    mWebView.loadUrl(result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}