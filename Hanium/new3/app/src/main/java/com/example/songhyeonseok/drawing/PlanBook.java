package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;

public class PlanBook extends AppCompatActivity{
    Context context;
    int BookID = 0;
    String planbookName;
    Button planbookicon;
    PlanBook(Context context, int id, String name,float para){
        this.context = context;
        this.BookID = id;
        this.planbookName = name;
        planbookicon = new Button(context);
        planbookicon.setPadding(50,50,50,50);
        planbookicon.setText(planbookName);
        planbookicon.setTextSize(20);
        planbookicon.setGravity(Gravity.CENTER);
        planbookicon.setBackgroundResource(R.drawable.checklist);
        planbookicon.setWidth(toDip(140,para));
        planbookicon.setHeight(toDip(200,para));
        planbookicon.setId(BookID+1000);
    }

    private int toDip(int d,float mScale){
        final int calHeight = (int)(d*mScale);
        return calHeight;
    }
}
