package com.example.songhyeonseok.taos_gps_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    Button sendbtn_json;
    String add = "http://bms.taos.kr/evraw1";
    StringBuffer tmp;
    HttpURLConnection httpURLConnection;
    ParsingBattData pbd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView txv = (TextView)findViewById(R.id.window);
        sendbtn_json = (Button)findViewById(R.id.send_json);
        Intent intent = getIntent();
        txv.setText(intent.getStringExtra("info").toString());




        tmp = new StringBuffer();
        tmp.append("\"Arr0\" :{ \"H1\": \"AB\",  \"H2\": \"AB\", \"A\": \"AB\", \"B\": \"AB\", \"C\": \"AB\", \"D\": \"AB\" },\n" +
                "        \"Arr1\" :{ \"E\": \"CC\", \"F\": \"CC\", \"G\": \"CC\", \"H\": \"CC\", \"I\": \"CC\", \"J\": \"CC\", \"K\": \"CC\" },\n" +
                "        \"Arr2\" :{ \"L\": \"AB\", \"M\": \"AB\", \"N\": \"AB\", \"O\": \"AB\", \"P\": \"AB\", \"Q\": \"AB\", \"R\": \"AB\" },\n" +
                "        \"Arr3\" :{ \"S\": \"AB\", \"T\": \"AB\", \"U\": \"AB\", \"V\": \"AB\", \"W\": \"AB\", \"X\": \"AB\", \"Y\": \"AB\" },\n" +
                "        \"Arr4\" :{ \"Z\": \"AB\", \"AA\": \"AB\", \"AB\": \"AB\", \"AC\": \"AB\", \"AD\": \"AB\", \"AE\": \"AB\",  \"AF\": \"AB\" },\n" +
                "        \"Arr5\" :{ \"AG\": \"AB\", \"AH\": \"AB\", \"AI\": \"AB\", \"AJ\": \"AB\", \"AK\": \"AB\", \"AL\": \"AB\", \"AM\": \"AB\" },\n" +
                "        \"Arr6\" :{ \"AN\": \"AB\", \"AO\": \"AB\", \"AP\": \"AB\", \"AQ\": \"AB\", \"AR\": \"AB\", \"AS\": \"AB\", \"AT\": \"AB\" },\n" +
                "        \"Arr7\" :{ \"AU\": \"AB\", \"AV\": \"AB\", \"AW\": \"AB\", \"AX\": \"AB\", \"AY\": \"AB\", \"AZ\": \"AB\", \"BA\": \"AB\" },\n" +
                "        \"Arr8\" :{ \"BB\": \"AB\", \"BC\": \"AB\", \"BD\": \"AB\", \"BE\": \"AB\", \"BF\": \"AB\", \"BG\": \"AB\", \"BH\": \"AB\" }");


        sendbtn_json.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void sendToTheNetwork(String s){
        new HttpPostTrans().execute(add);
    }


}
