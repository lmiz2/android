package com.example.songhyeonseok.taos_gps_test;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SongHyeonSeok on 2017-09-18.
 */



public class HttpPostTrans extends AsyncTask<String, Void, String> {

    StringBuffer tmp;
    int a;
    String batt_add = "http://bms.taos.kr/evraw1";
    @Override
    protected String doInBackground(String... s) {
        tmp = new StringBuffer();
        tmp.append(s);
        try {
            // 연결 url 설정
            URL url = new URL(batt_add);
            // 커넥션 객체 생성
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 연결되었으면.
            if (conn != null) {
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(tmp.toString().getBytes("UTF-8"));
                os.flush();
                os.close();
                Log.d("tkf","실행됨");
                // 연결되었음 코드가 리턴되면.
                a = conn.getResponseCode();
                Log.d("",a+"");
                conn.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return String.valueOf(a);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
