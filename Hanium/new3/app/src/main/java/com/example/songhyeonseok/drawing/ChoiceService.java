package com.example.songhyeonseok.drawing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSessionToken;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.FirebaseDatabase;


public class ChoiceService extends AppCompatActivity {
    final int PAINTING_ROOM = 2;
    final int PLANNER = 3;
    LinearLayout ll;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Bitmap bitmap_back,proom_btn,planner_btn,time_btn,news_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_activity);

//--------------------------------------------------------------------------------------------------
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        myToolbar.setNavigationIcon(R.drawable.ic_action_name);// 타이틀 왼쪽
//        myToolbar.setLogo(R.drawable.ic_action_name); //맨 왼쪽




//--------------------------------------------------------------------------------------------------




        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        Toast.makeText(this,"안녕하세요 "+mFirebaseUser.getDisplayName()+"님, \n이메일은 "+
                mFirebaseUser.getEmail()+" 입니다.",Toast.LENGTH_LONG).show();

        View l2 = (View)findViewById(R.id.painting);
        View l3 = (View)findViewById(R.id.goto_planner);
        View l4 = (View)findViewById(R.id.goto_timeline);
        View l5 = (View)findViewById(R.id.goto_news);
        ll = (LinearLayout)findViewById(R.id.bagyeong);


        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 2;
        bitmap_back = BitmapFactory.decodeResource(getResources(),R.drawable.zz,bo);
        Drawable d = new BitmapDrawable(getResources(),bitmap_back);
        ll.setBackground(d);
        bo.inSampleSize = 2;
        proom_btn = BitmapFactory.decodeResource(getResources(),R.drawable.liveboard,bo);
        l2.setBackground(new BitmapDrawable(getResources(),proom_btn));
        news_btn =  BitmapFactory.decodeResource(getResources(),R.drawable.newsfeedbtnimage,bo);
        l5.setBackground(new BitmapDrawable(getResources(),news_btn));
        time_btn =  BitmapFactory.decodeResource(getResources(),R.drawable.timelinebtnimage,bo);
        l4.setBackground(new BitmapDrawable(getResources(),time_btn));
        planner_btn =  BitmapFactory.decodeResource(getResources(),R.drawable.plannerbtnimage,bo);
        l3.setBackground(new BitmapDrawable(getResources(),planner_btn));

        //------------------------------------------------
        WindowManager winM = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = winM.getDefaultDisplay();
        Point point = new Point();

        display.getSize(point);



        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(Color.CYAN);
        builder.setShowTitle(true);
        builder.enableUrlBarHiding();
        builder.setInstantAppsEnabled(true);
        builder.setSecondaryToolbarColor(Color.WHITE);
        final CustomTabsIntent ct_intent = builder.build();

        //------------------------------------------------
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 칠판섹션용 리스너
                Intent intent = new Intent(getApplicationContext(),PaintingRoom.class);
                startActivityForResult(intent,PAINTING_ROOM);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {//플래너 섹션용 리스너
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Planner.class);
                startActivityForResult(intent,PLANNER);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String uri = "https://hanium-draw-commu.firebaseapp.com/";
//                ct_intent.launchUrl(ChoiceService.this,Uri.parse(uri));
                Intent intent = new Intent(getApplicationContext(),WebViewActivity.class);
                intent.putExtra("Here",1);
                startActivityForResult(intent,4);

            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String uri = "https://hanium-draw-commu.firebaseapp.com/Newsfeed.html";
//                ct_intent.launchUrl(ChoiceService.this,Uri.parse(uri));
                Intent intent = new Intent(getApplicationContext(),WebViewActivity.class);
                intent.putExtra("Here",2);
                startActivityForResult(intent,5);
            }
        });
    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        // 기본으로 들어가는 setting 메뉴
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_nav:
//                // User chose the "Settings" item, show the app settings UI...
//
//                return true;
//
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
}
