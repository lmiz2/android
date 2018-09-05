package com.example.songhyeonseok.drawing;

/**
 * Created by SongHyeonSeok on 2017-07-26.
 */

public class PaintData {
    float x,y;
    int color;
    float border;
    int key;
    boolean eraseMode;
    String state;
    String authorUid;
    String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getAuthorUid() {
        return authorUid;
    }

    public void setAuthorUid(String authorUid) {
        this.authorUid = authorUid;
    }

    public boolean isEraseMode() {
        return eraseMode;
    }

    public void setEraseMode(boolean eraseMode) {
        this.eraseMode = eraseMode;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getBorder() {
        return border;
    }

    public void setBorder(float border) {
        this.border = border;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}