package com.example.songhyeonseok.drawing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by lmiz2 on 2017-09-04.
 */

public class RecordQ extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rec_q);
        Button ok = (Button)findViewById(R.id.rec_go);
        Button cncl = (Button)findViewById(R.id.button2);

        cncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordQ.this,MediaProjectionDemo.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
