package com.example.songhyeonseok.drawing;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class newTodo extends AppCompatActivity implements DatePickerFragment.datePickerPort{

    public static Byte tt = 127;
    public int start_dates[];
    public int end_dates[];
    EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dateframe);

        edt = (EditText)findViewById(R.id.editText);
        TextView txv_start = (TextView)findViewById(R.id.start_date);
        TextView txv_end = (TextView)findViewById(R.id.end_date);
        start_dates = new int[3];
        end_dates  = new int[3];

        Button calendar_startDay = (Button)findViewById(R.id.open_cal);
        Button calendar_endDay = (Button)findViewById(R.id.open_cal_end);
        Button cal_ok = (Button)findViewById(R.id.cal_ok);

        calendar_startDay.setOnClickListener(listener);
        calendar_endDay.setOnClickListener(listener);

        cal_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("start",start_dates);
                intent.putExtra("end",end_dates);
                intent.putExtra("defini",edt.getText().toString());
                setResult(100,intent);
                finish();
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.open_cal){
                showDatePickerDialog(v);
                tt = 1;
            }else if (v.getId() == R.id.open_cal_end){
                showDatePickerDialog(v);
                tt = 2;
            }
        }
    };


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void sendDateData(){
        Toast.makeText(getApplicationContext(),start_dates[0]+" "+start_dates[1]+" "+start_dates[2],Toast.LENGTH_SHORT).show();
    }

    @Override
    public void datePickerPort(int y, int m, int d, int state) {
        if(state == 1) {
            start_dates[0] = y;
            start_dates[1] = m;
            start_dates[2] = d;
        }else if(state == 2){
            end_dates[0] = y;
            end_dates[1] = m;
            end_dates[2] = d;
        }

    }
}
