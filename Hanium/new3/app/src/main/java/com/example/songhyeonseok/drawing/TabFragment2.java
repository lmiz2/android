package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TabFragment2 extends Fragment {

    private tab2Listener mCallback;
    private FirebaseDatabase db;
    DatabaseReference refDB;
    int count = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tolist, container, false);
        TextView title = (TextView)v.findViewById(R.id.todolist_title);
        title.setText("Doing");
        db = FirebaseDatabase.getInstance();
        refDB = db.getReference("message");

        return v;
    }

    public interface tab2Listener{
        String communication2(String a);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (TabFragment2.tab2Listener)getActivity();
        }catch(ClassCastException e){
            System.out.println("에러 : "+ e.toString());
            Log.d("에러 : ",getActivity().toString()+" 는 반드시 tab1Listener를 implements 해야 합니다.");
        }
    }

}