package com.kkard.seoulroad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kkard.seoulroad.utils.RequestHttpConnection;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.regex.Pattern;

/**
 * Created by SuGeun on 2017-08-08.
 */

public class RegistActivity extends AppCompatActivity {
    private EditText password, passConf, email, name;
    private ConstraintLayout backLayout;
    private Button joinBtn;
    private ImageButton backBtn;
    private TextView toolbarTitle, checkName, checkEmail, checkPass, checkPassC;

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
            setContentView(R.layout.activity_regist);

            joinBtn = (Button) findViewById(R.id.joinEnter); // 가입하기 버튼
            password = (EditText) findViewById(R.id.joinPass); // 비밀번호
            passConf = (EditText) findViewById(R.id.joinPassConf); // 비밀번호 확인
            email = (EditText) findViewById(R.id.joinEmail); // 이메일
            name = (EditText) findViewById(R.id.joinName); // 이름
            backLayout = (ConstraintLayout) findViewById(R.id.joinBack);// 뒷배경
            backBtn = (ImageButton) findViewById(R.id.btn_toolbar_back); // 툴바 내 뒤로가기 이미지 버튼
            toolbarTitle = (TextView) findViewById(R.id.text_toolbar); // 툴바 제목
            checkName = (TextView) findViewById(R.id.checkName); // 이름 경고창
            checkEmail = (TextView) findViewById(R.id.checkEmail); // 이메일 경고창
            checkPass = (TextView) findViewById(R.id.checkPass);  // 비밀번호 경고창
            checkPassC = (TextView) findViewById(R.id.checkPassC); // 비밀번호 확인 경고창

            toolbarTitle.setText("회원가입");
            checkEmail.setVisibility(View.INVISIBLE);
            checkName.setVisibility(View.INVISIBLE);
            checkPass.setVisibility(View.INVISIBLE);
            checkPassC.setVisibility(View.INVISIBLE);

            backBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                    finish();
                    return false;
                }
            });

            backLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(passConf.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    checkInput();
                    return false;
                }
            });
            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    checkInput();
                }
            });
            email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    checkInput();
                }
            });
            password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    checkInput();
                }
            });
            passConf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    checkInput();
                }
            });
            passConf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
                    if (v.getId() == R.id.joinPassConf && actionId == EditorInfo.IME_ACTION_DONE) {
                        checkInput();
                    }
                    return false;
                }
            });
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Integer>() {
                        String nameContent;
                        String passContent;
                        String emailContent;
                        String passConfContent;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            nameContent = name.getText().toString().trim();
                            emailContent = email.getText().toString().trim();
                            passContent = password.getText().toString().trim();
                            passConfContent = passConf.getText().toString().trim();

                        }

                        @Override
                        protected Integer doInBackground(Void... voids) {
                            if (nameContent.getBytes().length <= 0) {//빈값이 넘어올때의 처리
                                return 2;
                            } else if (emailContent.getBytes().length <= 0) {
                                return 3;
                            } else if (passContent.getBytes().length <= 0) {
                                return 4;
                            } else if (passConfContent.getBytes().length <= 0) {
                                return 5;
                            } else if (!checkInput()) {
                                return 6;
                            } else if ((new RequestHttpConnection().loginConfirm("http://stou2.cafe24.com/php/idcheck.php", emailContent, passContent)).equals("1")) {
                                return 0;
                            } else {
                                RequestHttpConnection rhq = new RequestHttpConnection();
                                rhq.registUserForm("http://stou2.cafe24.com/php/registuserform.php", nameContent, emailContent, passContent);
                                return 1;
                            }
                        }

                        @Override
                        protected void onPostExecute(Integer aVoid) {
                            super.onPostExecute(aVoid);
                            switch (aVoid) {
                                case 0:
                                    Toast.makeText(getApplicationContext(), "ID가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                    email.setText("");
                                    email.requestFocus();
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(), "가입되었습니다", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(getApplicationContext(), "이름를 입력하세요.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 4:
                                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 5:
                                    Toast.makeText(getApplicationContext(), "비밀번호를 한번더 입력하세요.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "가입 정보를 정확하게 입력해 주세요", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }.execute();
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegistActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }

    private boolean checkInput() {
        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String pass = this.password.getText().toString();
        String passC = this.passConf.getText().toString();
        int cnt = 0;
        if (!(Pattern.matches("^[a-zA-Z]*$", name) || Pattern.matches("^[가-힣]*$", name))) { // 한글 또는 영어가 아닐경우
            checkName.setVisibility(View.VISIBLE);
            cnt++;
        } else {
            checkName.setVisibility(View.INVISIBLE);
        }
        if (!(LoginActivity.checkEmail(email) || email.getBytes().length == 0)) { // 이메일 형식이 아닐경우
            checkEmail.setVisibility(View.VISIBLE);
            cnt++;
        } else {
            checkEmail.setVisibility(View.INVISIBLE);
        }
        if (pass.length() != 4 && pass.length() != 0) { // 비밀번호가 4자리 이상 인 경우
            checkPass.setVisibility(View.VISIBLE);
            cnt++;
        } else {
            checkPass.setVisibility(View.INVISIBLE);
        }
        if (!passC.equals(password.getText().toString())) { // 비밀번호와 일치하지 않을 경우
            checkPassC.setVisibility(View.VISIBLE);
            cnt++;
        } else {
            checkPassC.setVisibility(View.INVISIBLE);
        }
        if (cnt == 0) return true;
        else return false;
    }
}
