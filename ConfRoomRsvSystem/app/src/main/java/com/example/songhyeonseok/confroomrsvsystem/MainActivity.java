package com.example.songhyeonseok.confroomrsvsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    NewTask task;
    Map params;
    String addrStr;
    EditText edt;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt = (EditText)findViewById(R.id.test1);
        btn = (Button)findViewById(R.id.btbt);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doit();
            }
        });

    }

    public void doit(){
        try {
            addrStr = edt.getText().toString();
            Log.d("addr", addrStr);
            task = new NewTask();
            params = new HashMap<String, String>();
            params.put("address", "http://" + addrStr + "/final/androidTest");
            params.put("test1", addrStr);
            params.put("test2", "test2Ap");
            task.execute(params);
            btn.setText("Send Success");
        }catch (Exception e){
            btn.setText("Error Occured");
        }
    }
}
