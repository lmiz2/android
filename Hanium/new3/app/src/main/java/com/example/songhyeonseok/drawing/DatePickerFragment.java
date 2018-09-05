package com.example.songhyeonseok.drawing;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.songhyeonseok.drawing.newTodo.tt;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    datePickerPort port1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        TextView tv1 = (TextView) getActivity().findViewById(R.id.start_date);
        TextView tv2 = (TextView) getActivity().findViewById(R.id.end_date);
        if(tt == 1) {
            tv1.setText(view.getYear()+"년 "+view.getMonth()+"월 "+view.getDayOfMonth()+"일");
            port1.datePickerPort(view.getYear(),view.getMonth(),view.getDayOfMonth(),1);
        } else if(tt == 2) {
            tv2.setText(view.getYear()+"년 "+view.getMonth()+"월 "+view.getDayOfMonth()+"일");
            port1.datePickerPort(view.getYear(),view.getMonth(),view.getDayOfMonth(),2);
        }

    }

    public interface datePickerPort{
        void datePickerPort(int y, int m, int d,int state);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            port1 = (datePickerPort)getActivity();
        }catch(ClassCastException e){
            System.out.println("에러 : "+ e.toString());
            Log.d("에러 : ",getActivity().toString()+" 는 반드시 datePickerPort를 implements 해야 합니다.");
        }
    }
}