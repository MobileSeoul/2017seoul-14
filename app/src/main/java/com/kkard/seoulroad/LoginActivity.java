package com.kkard.seoulroad;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kkard.seoulroad.utils.CustomProgressBar;
import com.kkard.seoulroad.utils.RequestHttpConnection;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SuGeun on 2017-08-08.
 */

public class LoginActivity extends Activity {
    ///////////////Back 버튼 2번 종료 관련 변수////////////
    private final long FINISH_INTERVAL_TIME = 2000; //2초안에 Back 버튼 누르면 종료
    private long   backPressedTime = 0;
    private String userId, userPassword;
    /////////////// 뷰 객체 선언 /////////////
    EditText email, pass;
    Button loginBtn, joinBtn;
    ConstraintLayout backLayout;
    CheckBox saveID, autoLogin;
    SharedPreferences sh;
    SharedPreferences miniDB;
    SharedPreferences.Editor editor;
    TextView emailError,passError;

    /////////////////////// 변수 선언 //////////////////////
    private boolean isSave; // 아이디 저장 체크박스
    private boolean saveExist; // 아이디 저장 설정 여부
    private boolean isAuto;
    private int lineColor;

    private String idContent;
    private String passContent;

    private String intentID;
    private String intentEMAIL;
    private String intentNAME;
    private CustomProgressBar cpb;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cpb = new CustomProgressBar(this);
        cpb.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
            setContentView(R.layout.activity_login);
            sh = getSharedPreferences("AutoINFO", MODE_PRIVATE);
            miniDB = getSharedPreferences("UserInfo", MODE_PRIVATE);
            if (sh.getString("isAuto", "false").equals("false")) {
                editor = sh.edit();
                editor.putString("isAuto", "false");
                editor.apply();
            } else { // 자동 로그인 할 경우
                userId = sh.getString("UserAutoId", "None"); // 글로벌 아이디 저장
                userPassword = sh.getString("UserAutoPass", "None"); // 글로벌 비밀번호 저장
                Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                intent.putExtra("pageNum", 0);
                startActivity(intent);
                finish();
            }
            InitView();
            lineColor = Color.parseColor("#FFFFFF");
            email.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
            email.setSelection(email.length());
            pass.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
            userId = sh.getString("UserSaveId", "None"); // 자동 저장 되어 있는 ID 가져오기
            if (userId.equals("None")) { // 자동 저장이 되어있지 않을 경우
                saveExist = false;
                isSave = false;
            } else {
                saveExist = true;
                saveID.setChecked(true);
                isSave = true;
                email.setText(userId);
            }

            backLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
                    return false;
                }
            });

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    finish();
                }
            });
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            cpb.show();
                            idContent = email.getText().toString().trim();
                            passContent = pass.getText().toString().trim();
//                        miniDB = getSharedPreferences("DB",MODE_PRIVATE);
//                        userId = miniDB.getString("G_ID","none");
//                        userPassword = miniDB.getString("G_PASS","none");
                            if (idContent.getBytes().length <= 0) {//빈값이 넘어올때의 처리
                                Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else if (passContent.getBytes().length <= 0) {
                                Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else if (!checkEmail(idContent)) {
                                Toast.makeText(getApplicationContext(), "아이디를 이메일 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else if (passContent.length() != 4) {
                                Toast.makeText(getApplicationContext(), "비밀번호는 4자리 입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }

                        @Override
                        protected Integer doInBackground(Void... voids) {
                            RequestHttpConnection rhc = new RequestHttpConnection();
                            String result = rhc.loginConfirm("http://stou2.cafe24.com/php/login.php", idContent, passContent);
                            if (result.contains(idContent)) {
                                getuserinfo(result);
                                editor = sh.edit();
                                if (isAuto) {
                                    editor.putString("UserAutoId", idContent);
                                    editor.putString("UserAutoPass", passContent);
                                    editor.putString("isAuto", "true");
                                    editor.apply();
                                }
                                //  첫 저장  -> 입력
                                if (isSave || saveExist) {                        //  isSave = true  isExist = false
                                    //  저장되어있는상태 -> 입력
                                    if (isSave) {                                   //  isSave = true  isExist = true
                                        editor.putString("UserSaveId", idContent);     //  저장 해제 -> 삭제
                                    } else {                                        //  isSave = false   isExist = true
                                        editor.remove("UserSaveId");                //  저장하지않음 -> 아무것도 안함
                                    }                                               //  isSave = false   isExist = false
                                    editor.apply();
                                }
                                return 1;
                            } else {
                                return 0;
                            }
                        }

                        @Override
                        protected void onPostExecute(Integer integer) {
                            super.onPostExecute(integer);
                            if (integer == 1) {
                                editor = miniDB.edit();
                                editor.putString("userindex", intentID);
                                editor.putString("userid", intentEMAIL);
                                editor.putString("username", intentNAME);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                            }
                            cpb.dismiss();
                        }
                    }.execute();
                }
            });
            saveID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isSave = true;
                    } else {
                        isSave = false;
                    }
                }
            });
            autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        isAuto = true;
                    } else {
                        isAuto = false;
                    }
                }
            });

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!checkEmail(email.getText().toString().trim())) {
                        lineColor = Color.parseColor("#E73A62");
                        email.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
                        emailError.setVisibility(View.VISIBLE);
                    } else {
                        lineColor = Color.parseColor("#FFFFFF");
                        email.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
                        emailError.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (pass.length() > 4) {
                        lineColor = Color.parseColor("#E73A62");
                        pass.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
                        passError.setVisibility(View.VISIBLE);
                    } else {
                        lineColor = Color.parseColor("#FFFFFF");
                        pass.getBackground().setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP);
                        passError.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "종료를 원하시면 뒤로 버튼을 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    /////////////// 이메일 포맷 체크 ////////////////
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email.trim());
        boolean isNormal = m.matches();
        if(email.length()==0)isNormal =true;
        return isNormal;
    }
    private void InitView(){
        email = (EditText) findViewById(R.id.emailInput);
        pass = (EditText) findViewById(R.id.passwordInput);
        loginBtn = (Button) findViewById(R.id.loginButton);
        joinBtn = (Button) findViewById(R.id.joinButton);
        backLayout = (ConstraintLayout) findViewById(R.id.backGround);
        saveID = (CheckBox)findViewById(R.id.saveID);
        autoLogin = (CheckBox)findViewById(R.id.autoLogin);
        emailError = (TextView)findViewById(R.id.emailError);
        passError = (TextView)findViewById(R.id.passError);
    }

    private void getuserinfo(String myJson){
        try{
            JSONObject jsonObject = new JSONObject(myJson);
            JSONArray res = jsonObject.getJSONArray("result");
            for(int i =0 ; i<res.length();i++){
                JSONObject c = res.getJSONObject(i);
                switch (i){
                    case 0:
                        intentID = c.getString("u_index_id");
                        break;
                    case 1:
                        intentEMAIL = c.getString("u_email_id");
                        break;
                    case 2:
                        intentNAME = c.getString("u_name");
                        break;
                }
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }
    }

}

