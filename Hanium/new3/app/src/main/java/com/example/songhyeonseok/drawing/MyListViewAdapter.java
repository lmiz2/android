package com.example.songhyeonseok.drawing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SongHyeonSeok on 2017-08-17.
 */

public class MyListViewAdapter extends BaseAdapter {
    private ArrayList<ListItemFriend> friendArrayList = new ArrayList<ListItemFriend>();
    private Context con;


    public MyListViewAdapter(Context context) {
        con = context;
    }

    @Override
    public int getCount(){
        return friendArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_friend,parent,false);

        }

        TextView name = (TextView) convertView.findViewById(R.id.user_name);
        TextView notice = (TextView)convertView.findViewById(R.id.state_notice);

        ListItemFriend listItemFriend = friendArrayList.get(position);

        name.setText(listItemFriend.getUserName());
        notice.setText(listItemFriend.getNotice());

        return convertView;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public Object getItem(int position) {
        return friendArrayList.get(position);
    }

    public void additem(String name, String notice){
        ListItemFriend lif = new ListItemFriend();

        lif.setUserName(name);
        lif.setNotice(notice);

        friendArrayList.add(lif);
        notifyDataSetChanged();
    }
}
