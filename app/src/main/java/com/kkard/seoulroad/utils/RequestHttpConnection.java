package com.kkard.seoulroad.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KyungHWan on 2017-10-28.
 */

public class RequestHttpConnection {
    HttpURLConnection con;
    OutputStream os;
    URL url;
    BufferedReader br = null;
    String param;
    int responseStatusCode;
    InputStream inputStream;
    InputStreamReader inputStreamReader;
    StringBuilder sb;

    public void upPictureName(String url, String u_index, String userid, String picname, String content) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "uindex=" + u_index + "&userid=" + userid + "&picname=" + picname + "&content=" + content;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registUserForm(String url, String name, String userid, String pass) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "userid=" + userid + "&username=" + name + "&pass=" + pass;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }
    }

    public String loginConfirm(String url, String user_id, String user_pass) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
            con.setDoInput(true);
            con.setDoOutput(true);

            param = "user_id=" + user_id + "&user_pass=" + user_pass;
            Log.d("param", param);
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();


            responseStatusCode = con.getResponseCode();
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {//http response상태 확인후 inputstream에 값넣어주기(오류있는걸로 나오는데 값은 잘 들어옴 이유몰라)
                inputStream = con.getInputStream();
            } else {
                inputStream = con.getErrorStream();
            }
            //Log.d("kkk",inputStream.toString());
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(inputStreamReader);
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            //Log.d("zxc",sb.toString().trim());
            return sb.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public BufferedReader requestImageInfo(String url, String u_index_id, String photo_id) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "u_index_id=" + u_index_id + "&photo_id=" + photo_id;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }
        return br;
    }

    public BufferedReader requestCourseInfo(String url, String index) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");


            param = "index=" + index;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }
        return br;
    }
    public BufferedReader requestVInfo(String url) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }
        return br;
    }
    public void updateImageContent(String url , String photo_id, String content){
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "photo_id=" + photo_id + "&content=" + content;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }

    }
    public void updateUserPass(String url, String u_email_id, String user_pass) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "user_id=" + u_email_id + "&user_pass=" + user_pass;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
        }
    }

    public void updateLike(String url, String user_index_id, String photo_id, String created, String count) {
        try {
            this.url = new URL(url);
            con = (HttpURLConnection) this.url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            param = "user_id=" + user_index_id + "&photo_id=" + photo_id + "&created=" + created + "&count=" + count;
            os = con.getOutputStream();
            os.write(param.getBytes("UTF-8"));
            os.flush();
            os.close();

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
