package com.example.songhyeonseok.drawing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PlannerIn extends AppCompatActivity implements TabFragment1.tab1Listener,TabFragment2.tab2Listener{
    int booknum;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;
    int nowTab;
    private FirebaseDatabase db;
    DatabaseReference refDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_planbook);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title = (TextView)findViewById(R.id.plnbookname);
        //----------------------------정보 받아오기 시작----------
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        booknum = intent.getIntExtra("Here",0);
        //----------------------------정보 받아오기 끝------------


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("TO DO"));
        tabLayout.addTab(tabLayout.newTab().setText("DOING"));
        tabLayout.addTab(tabLayout.newTab().setText("DONE"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);


        db = FirebaseDatabase.getInstance();
        refDB = db.getReference("message");


        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                nowTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pagerAdapter.notifyDataSetChanged();


    }
    @Override
    public void communication(String a) {
        pagerAdapter.notifyDataSetChanged();
    }
    @Override
    public String communication2(String a) {
        if(a == "submit") {
            //pagerAdapter.sendDatalist(0,item);
            Log.d("서브밋","");
   //         refDB.push().setValue(item);
        }else if(a == "remove"){
            refDB.removeValue();
            Log.d("리무브","");
        }

        Log.d("버튼명령 실행완료","");
        pagerAdapter.notifyDataSetChanged();

        return "ok";
    }
}
