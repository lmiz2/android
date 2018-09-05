package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Planner extends AppCompatActivity {

    int planbookcount = 0;
    final Context context = this;
    String com;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.plan_startpage);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TextView starttext = (TextView)findViewById(R.id.startText);
        final LinearLayout l1 = (LinearLayout)findViewById(R.id.appear_section);
        final EditText planbookName = new EditText(this);
        final PlanBook[] planBooks = new PlanBook[20];
        final Button addBook = new Button(this);
        addBook.setBackgroundResource(R.drawable.newfile);
        addBook.setWidth(toDip(140));
        addBook.setHeight(toDip(200));

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

//        Toast.makeText(this,"안녕하세요 "+mFirebaseUser.getDisplayName()+"님, \n이메일은 "+
//                mFirebaseUser.getEmail()+" 입니다.",Toast.LENGTH_LONG).show();




        final View.OnClickListener plnBookclick= new View.OnClickListener() {//각 각 생성된 플랜북icon을 클릭했을때 호출하는 리스너
            @Override
            public void onClick(View v) { //만들어진 플랜북아이콘 터치 리스너
                Intent intent = new Intent(getApplicationContext(),PlannerIn.class);
                intent.putExtra("Here",v.getId());
                com = planBooks[(v.getId()-1000)].planbookName;
                intent.putExtra("title", com);
                startActivityForResult(intent,v.getId());
            }
        };

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog;


        planbookName.setHint("이곳에 제목을 입력하세요.");

        // 제목셋팅
        alertDialogBuilder.setTitle("플랜북 생성하기");
        alertDialogBuilder.setView(planbookName); // 플랜북 이름 입력창

        // AlertDialog 셋팅
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("생성",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                //만들기 버튼 누르면 할일
                                if(planbookcount == 0) {
                                    starttext.setVisibility(View.GONE);
                                }
                                if(planbookcount > 0){
                                    l1.removeAllViews();
                                }

                                planBooks[planbookcount] = new PlanBook(context,planbookcount,
                                        planbookName.getText().toString(),getResources().getDisplayMetrics().density);

                                planBooks[planbookcount].planbookicon.setOnClickListener(plnBookclick);

                                for(int i=0; i<=planbookcount;i++){
                                    l1.addView(planBooks[i].planbookicon);
                                }

                                l1.addView(addBook);
                                planbookcount++;
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                                //               LogInPopUp.this.finish();
                            }
                        });

        // 다이얼로그 생성
        alertDialog = alertDialogBuilder.create();

        starttext.setText(Html.fromHtml("<u>화면을 터치하여 새로운 플랜북을 만들어보세요.</u>"));

        View.OnTouchListener addviewListenner = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                    //    l1.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        planbookName.setText("");
                        alertDialog.show();
                   //     l1.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return true;
            }
        };


        starttext.setOnTouchListener(addviewListenner);
        addBook.setOnTouchListener(addviewListenner);

    }

    private int toDip(int d){
        float mScale = getResources().getDisplayMetrics().density;
        final int calHeight = (int)(d*mScale);
        return calHeight;
    }

}

