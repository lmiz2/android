package com.example.songhyeonseok.geoquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    final static String KEY_INDEX = "index_key";

    Button mtrueBtn;
    Button mfalseBtn;
    Button mNextBtn;
    Button mPrvBtn;
    Button mChBtn;
    TextView mTv;
    TextView mPageDisp;
    IT1047Indicator idc;
    View.OnClickListener midcListener;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtrueBtn = findViewById(R.id.tbtn);
        mfalseBtn= findViewById(R.id.fbtn);
        mNextBtn= findViewById(R.id.nxtbtn);
        mPrvBtn = findViewById(R.id.prvbtn);
        mChBtn = findViewById(R.id.chBtn);
        mTv = findViewById(R.id.tv);
        mPageDisp = findViewById(R.id.pageDisp);
        idc = findViewById(R.id.circleAnimIndicator);
        idc.setItemMargin(15);
        idc.setAnimDuration(300);
        midcListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedIndex = Integer.parseInt(v.getTransitionName());
                if(clickedIndex != mCurrentIndex){
                    mCurrentIndex = clickedIndex;
                    updateQuestion();
                }
            }
        };
        idc.createDotPanel(mQuestionBank.length, R.drawable.indicator_non , R.drawable.indicator_on,midcListener);

        if(savedInstanceState != null) {
            if(savedInstanceState.get(KEY_INDEX) != null) {
                mCurrentIndex = (Integer) savedInstanceState.get(KEY_INDEX);
            }
        }
        updateQuestion();
        mPageDisp.setText((mCurrentIndex+1)+" / "+mQuestionBank.length);

        mfalseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Does Nothing Yet, but Soon!
                checkAnswer(true);
            }
        });

        mtrueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveProcess(1);
            }
        });

        mPrvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveProcess(-1);
            }
        });

        mChBtn = findViewById(R.id.chBtn);
        mChBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivity(intent);
            }
        });


    }

    public void moveProcess(int delta){
        mCurrentIndex = (mCurrentIndex + delta + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        idc.selectDot(mCurrentIndex);
        mTv.setText(question);
        mPageDisp.setText((mCurrentIndex+1)+" / "+mQuestionBank.length);
    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_INDEX,mCurrentIndex);
        super.onSaveInstanceState(outState);
    }
}
