package com.example.songhyeonseok.drawing;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;
    private TabFragment1 tabFragment1;
    private TabFragment2 tabFragment2;
    private TabFragment3 tabFragment3;
    String st="";

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                tabFragment1 = new TabFragment1();
                return tabFragment1;
            case 1:
                tabFragment2 = new TabFragment2();
                return tabFragment2;
            case 2:
                tabFragment3 = new TabFragment3();
                return tabFragment3;
            default:
                return null;
        }
    }
/*
    public void sendDatalist(int position,ListItemFriend listItemFriend){
        if(tabFragment1==null){
            Log.d("리턴됨",": 종료");
            return;
        }
        if(position==0) {
            Log.d("리턴됨",": additem");
            tabFragment1.additem(listItemFriend.getPicture(),listItemFriend.getUserName(),listItemFriend.getNotice());
        }
    }
*/
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}


