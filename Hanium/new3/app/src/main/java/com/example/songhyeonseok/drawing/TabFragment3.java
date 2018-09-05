package com.example.songhyeonseok.drawing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tolist, container, false);
        TextView title = (TextView)v.findViewById(R.id.todolist_title);
        title.setText("Done");
        return v;
    }
}