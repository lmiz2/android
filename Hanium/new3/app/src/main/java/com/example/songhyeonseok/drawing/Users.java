package com.example.songhyeonseok.drawing;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lmiz2 on 2017-09-18.
 */

public class Users {

    public List<User> users;


    public Users() {
        this.users = new ArrayList<User>();
    }

    public boolean findUsers(User u) {
        return users.contains(u);
    }

    public boolean findUserForUID(String s){
        for(User user : users) {

            String element = user.getUser_id();
            Log.d("배열 확인중 ",element+" & "+s);
            if(element.equals(s)){
                Log.d("배열 확인중 ",s + "는 "+element.equals(s));
                return true;
            }
        }
        return false;
    }

    public void addUser(String name, String uid, String email) {
        User user = new User();
        user.setUser_name(name);
        user.setUser_id(uid);
        user.setUser_email(email);
        this.users.add(user);
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public void del_user(User u){
        users.remove(u);
    }
}
