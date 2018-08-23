package com.bignerdranch.android.photogallery;

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;
    private boolean isNewItem;
    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isNewItem() {
        return isNewItem;
    }

    public void setNewItem(boolean newItem) {
        isNewItem = newItem;
    }
}
