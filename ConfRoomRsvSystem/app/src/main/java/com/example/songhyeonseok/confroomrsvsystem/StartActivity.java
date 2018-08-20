package com.example.songhyeonseok.confroomrsvsystem;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private TextView icon;
    LinearLayout loading_cover;
    ConstraintLayout cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        cover = (ConstraintLayout) findViewById(R.id.cover);// 초기화
        loading_cover = (LinearLayout)findViewById(R.id.loading_cover);
        icon = (TextView) findViewById(R.id.appicon);

        icon.setTypeface(Typeface.createFromAsset(getAssets(),"Dosis-ExtraLight.ttf"));

        final Animation animtitle = AnimationUtils.loadAnimation(this,R.anim.anim_title);
        final Animation animtitle2 = AnimationUtils.loadAnimation(this,R.anim.anim_title2);
        final Animation tobigger = AnimationUtils.loadAnimation(this,R.anim.tobigger);

        icon.setVisibility(View.GONE);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                icon.startAnimation(animtitle);
                icon.setVisibility(View.VISIBLE);
            }
        },2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                icon.setVisibility(View.VISIBLE);
                icon.startAnimation(animtitle2);
            }
        },3000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSigninBtn.startAnimation(animtitle);
                mSigninBtn.setVisibility(View.VISIBLE);
            }
        },3500);
    }
}
