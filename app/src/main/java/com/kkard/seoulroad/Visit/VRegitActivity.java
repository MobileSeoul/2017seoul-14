package com.kkard.seoulroad.Visit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkard.seoulroad.FragmentActivity;
import com.kkard.seoulroad.R;
import com.kkard.seoulroad.utils.DialogView_C;
import com.kkard.seoulroad.utils.JSONParser;
import com.kkard.seoulroad.utils.RequestHttpConnection;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by KyungHWan on 2017-10-06.
 */

public class VRegitActivity extends AppCompatActivity {
    private ImageView cameraSelect;
    private DialogView_C mdialog;
    private TextView toolbalTitle;
    private ImageButton backBtn;
    private Button registBtn;
    private EditText contentText;


    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222; //카메라 촬영으로 사진 가져오기
    private static final int REQUEST_TAKE_ALBUM = 3333; //앨범에서 사진 가져오기
    private static final int REQUEST_IMAGE_CROP = 4444; //가져온 사진을 자르기 위한 변수

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            setContentView(R.layout.activity_vregit);
            InitView();
            checkPermission();

            SharedPreferences pre = getSharedPreferences("UserInfo", MODE_PRIVATE);//user정보 저장 미니디비
            final String userId = pre.getString("userid", "id error");
            final String userName = pre.getString("username", "name error");
            final String user_index = pre.getString("userindex", "index error");

            toolbalTitle.setText("방문록 쓰기");

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(VRegitActivity.this, FragmentActivity.class));
                    finish();
                }
            });
            cameraSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdialog = new DialogView_C(DialogView_C.DIA_TYPE_CAMERA, v.getContext(), leftClickListener, middleClickListener, rightClickListener);
                    mdialog.show();
                }
            });
            registBtn = (Button) findViewById(R.id.regit_btn);
            registBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contentText.length() <= 0 || contentText.length() > 20) {
                        Toast.makeText(getApplicationContext(), "20자 이내로 작성해주세요.", Toast.LENGTH_SHORT).show();

                    } else {
                        final String content = contentText.getText().toString();
                        if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
                            // if(true){
                            new AsyncTask<Void, Integer, Boolean>() {
                                ProgressDialog progressDialog;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = new ProgressDialog(VRegitActivity.this);
                                    progressDialog.setMessage("잠시만 기다려 주세요");
                                    progressDialog.show();
                                }

                                @Override
                                protected Boolean doInBackground(Void... params) {
                                    try {
                                        RequestHttpConnection rhc = new RequestHttpConnection();
                                        String i_name = mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("/") + 1);
                                        rhc.upPictureName("http://stou2.cafe24.com/php/imagenameup.php", user_index, userId, i_name, content);//userindex 받아서 넣어야함 이메일도 내용도
                                        JSONObject jsonObject = JSONParser.uploadImage(mCurrentPhotoPath);
                                        if (jsonObject != null)
                                            return jsonObject.getString("result").equals("success");
                                    } catch (JSONException e) {
                                        Log.i("TAG", "Error : " + e.getLocalizedMessage());
                                    }
                                    return false;
                                }

                                @Override
                                protected void onPostExecute(Boolean aBoolean) {
                                    super.onPostExecute(aBoolean);
                                    if (progressDialog != null)
                                        progressDialog.dismiss();
                                    if (aBoolean) {
                                        Toast.makeText(getApplicationContext(), "사진이 등록되었습니다.", Toast.LENGTH_LONG).show();
                                    } else
                                        Toast.makeText(getApplicationContext(), "사진 등록을 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show();
                                    //imagePath = "";
                                    //imv.setVisibility(View.INVISIBLE);

                                    startActivity(new Intent(VRegitActivity.this, FragmentActivity.class));
                                    finish();
                                }
                            }.execute();
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VRegitActivity.this,FragmentActivity.class));
        finish();
    }
    private View.OnClickListener leftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mdialog.dismiss();
            captureCamera(); // 사진 찍기
        }
    };
    private View.OnClickListener middleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mdialog.dismiss();
            getAlbum(); // 앨범에서 고르기
        }
    };
    private View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mdialog.dismiss();
        }
    };
    private void InitView(){
        cameraSelect = (ImageView)findViewById(R.id.regit_image);
        toolbalTitle = (TextView)findViewById(R.id.text_toolbar);
        backBtn = (ImageButton)findViewById(R.id.btn_toolbar_back);
        contentText = (EditText)findViewById(R.id.regit_text);
    }
    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provider의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri =providerURI;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "접근이 불가능 합니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+ "/Pictures","tnrms");

        if(!storageDir.exists()){
            Log.e("mCurrentPhotoPath1",storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }
    private void getAlbum(){
        Log.e("getAlbum","Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }
    private void galleryAddPic(){
        Log.e("galleryAddPic","Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"사진이 앨범에 저장 되었음",Toast.LENGTH_SHORT).show();
    }
    public void cropSingleImage(Uri photoUriPath){
        Log.e("사진전용크랍","call");

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setDataAndType(photoUriPath,"image/*");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output",albumURI);
        Context context = getApplicationContext();
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(cropIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, photoUriPath, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent i = new Intent(cropIntent);
        ResolveInfo res = resInfoList.get(0);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        grantUriPermission(res.activityInfo.packageName,photoUriPath,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(i,REQUEST_IMAGE_CROP);

    }
    public void cropImage(){
        Log.e("cropImage","Call");
        Log.e("cropImage","photoURI : "+ photoURI + "/ albumURI : "+ albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI,"image/*");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output",albumURI);
        startActivityForResult(cropIntent,REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    try{
                        Log.e("사진 찍기","OK");
                        galleryAddPic();
                        File albumFile = null;
                        albumFile = createImageFile();
                        albumURI = Uri.fromFile(albumFile);
                        cropSingleImage(imageUri);

                    } catch (Exception e){
                        Log.e("사진찍기 오류",e.toString());
                    }
                } else {
                    Toast.makeText(this,"사진찍기 취소",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_ALBUM:
                if(resultCode == Activity.RESULT_OK){
                    if(data.getData() != null){
                        try{
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            cropImage();
                        }catch (Exception e){
                            Log.e("앨범찾기 오류",e.toString());
                        }
                    }
                }
                break;
            case REQUEST_IMAGE_CROP:
                if(resultCode == Activity.RESULT_OK){
                    galleryAddPic();
                    cameraSelect.setScaleType(ImageView.ScaleType.FIT_XY);
                    cameraSelect.setImageURI(albumURI);
                }
                break;
        }
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))){
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한 거부")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:"+getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_CAMERA:
                for(int i = 0; i< grantResults.length;i++){
                    if(grantResults[i]<0){
                        Toast.makeText(VRegitActivity.this,"카메라 권한을 켜주세요",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
        }
    }
}