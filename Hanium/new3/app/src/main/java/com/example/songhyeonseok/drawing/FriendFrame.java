package com.example.songhyeonseok.drawing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.Random;


public class FriendFrame extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.friend_frame);

        ScrollView scrollView = (ScrollView)findViewById(R.id.main_box_scrollview);
        ListView listView = (ListView)findViewById(R.id.main_box_text_listview);
        final Button okbtn = (Button)findViewById(R.id.okbtn);
        //----------------리스트뷰 임시 생성 영역 ------------------------ㄱ


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] testDatas = new String[3];
        for(int i = 0; i < 3; i++){
            switch(i){
                case 2: testDatas[i] = "송현석";
                    break;
                case 1: testDatas[i] = "송현석";
                    break;
                case 0: testDatas[i] = "SONG HYEONSEOK";
                    break;
            }


        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,testDatas);
        listView.setAdapter(arrayAdapter);


        //ㄴㄴ---------------------------------------------------------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                okbtn.setVisibility(View.VISIBLE);


            }
        });

    }
}
