package com.example.songhyeonseok.confroomrsvsystem;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class NewTask extends AsyncTask<Map<String,String>,Integer,String> {

    @Override
    protected String doInBackground(Map<String, String>... maps) {
        String addr = maps[0].get("address");
        HttpClient.Builder http = new HttpClient.Builder("POST", addr);

// Parameter 를 전송한다.
        http.addAllParameters(maps[0]);


//Http 요청 전송
        HttpClient post = http.create();
        post.request();

// 응답 상태코드 가져오기
        int statusCode = post.getHttpStatusCode();

// 응답 본문 가져오기
        String body = post.getBody();

        Log.d(TAG, "doInBackground: "+addr);
        return body;
    }
}
