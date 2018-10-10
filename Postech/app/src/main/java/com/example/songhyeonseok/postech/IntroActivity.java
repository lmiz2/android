package com.example.songhyeonseok.postech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    ImageView logo;
    Animation animAlpha;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        handler = new Handler();
        logo = (ImageView)findViewById(R.id.logo);
        logo.setVisibility(View.GONE);
        animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    logo.startAnimation(animAlpha);
                    logo.setVisibility(View.VISIBLE);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this,WebViewAct.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
