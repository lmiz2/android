package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by lmiz2 on 2017-08-29.
 */

public class MyFAB extends FloatingActionMenu {

    public MyFAB(Context context) {
        super(context, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }



        return super.onTouchEvent(event);
    }
}
