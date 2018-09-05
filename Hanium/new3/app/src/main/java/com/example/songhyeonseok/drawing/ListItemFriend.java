package com.example.songhyeonseok.drawing;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by SongHyeonSeok on 2017-08-17.
 */

public class ListItemFriend {
    private Drawable picture;
    private String userName;
    private String notice;

    public Drawable getPicture() {
        return picture;
    }


    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public void setPictureBitmap(Bitmap picture) {
        this.picture = new BitmapDrawable(picture);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
