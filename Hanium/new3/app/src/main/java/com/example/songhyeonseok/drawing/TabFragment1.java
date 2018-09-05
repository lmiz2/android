package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.app.Activity.RESULT_OK;


public class TabFragment1 extends Fragment {

    private ListView mListView;
    private MyListViewAdapter mAdapter;
//    private MyListViewAdapter mAdapter;
    tab1Listener mCallback;
    private FirebaseDatabase db;
    DatabaseReference refDB;
    int count = 1;
    Button add;
    ImageButton bef_add;
    public int start_dates[];
    public int end_dates[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tolist, container, false);
        db = FirebaseDatabase.getInstance();
        refDB = db.getReference("message");
 //       item = new ListItemFriend();
       add = (Button)view.findViewById(R.id.addbtn);
        bef_add = (ImageButton)view.findViewById(R.id.bef_addbtn_todo);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),newTodo.class);
                startActivityForResult(intent,100);
                add.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                bef_add.setVisibility(View.VISIBLE);
            }
        });

        bef_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),newTodo.class);
                startActivityForResult(intent,100);
            }
        });


        mListView = (ListView) view.findViewById(R.id.list_view1);
        mAdapter = new MyListViewAdapter(getActivity());
        mListView.setAdapter(mAdapter);


        refDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               /* item = dataSnapshot.getValue(ListItemFriend.class);
                mAdapter.additem(null, item.getUserName(), item.getNotice());
                Log.d("아이템이 수신됨", " ");
                mCallback.communication("");*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        return view;


    }

    public void additem(String a, String b){
        mAdapter.additem(a,b);
        mAdapter.notifyDataSetChanged();
        mCallback.communication("");
        Log.d("ㅇㅋㅇㅋㅇㅋㅇ","additem");
    }

    public interface tab1Listener{
        void communication(String a);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (tab1Listener)getActivity();
        }catch(ClassCastException e){
            System.out.println("에러 : "+ e.toString());
            Log.d("에러 : ",getActivity().toString()+" 는 반드시 tab1Listener를 implements 해야 합니다.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ㅇㅋㅇㅋㅇㅋㅇ","온액리");
                start_dates = data.getIntArrayExtra("start");
                end_dates = data.getIntArrayExtra("end");
                additem(data.getStringExtra("defini")
                        ,start_dates[0]+"."+start_dates[1]+"."+start_dates[2]+" ~ "+
                        end_dates[0]+"."+end_dates[1]+"."+end_dates[2]);

                Log.d("ㅇㅋㅇㅋㅇㅋㅇ","온액리"+start_dates[2]);
                Log.d("ㅇㅋㅇㅋㅇㅋㅇ","온액리");
    }
}
