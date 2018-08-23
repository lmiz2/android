package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private static int page = 1;

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder,
                                                      Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );

        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");

        new FetchItemsTask().execute(page);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        setupAdapter();
        mAdapter = new PhotoAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mAdapter);
        mPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == 0 && !recyclerView.canScrollVertically(1) && page <= 10){
                    new FetchItemsTask().execute(++page);
                }else if(newState == 0 && !recyclerView.canScrollVertically(-1) && page <= 10){
                    Log.d(TAG, "onScrollStateChanged: ");
                    mAdapter.mGalleryItems = new ArrayList<GalleryItem>();
                    page =  1;
                    new FetchItemsTask().execute(page);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return v;
    }

    private class FetchItemsTask extends AsyncTask<Integer,Void,List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
                return new FlickrFetchr().fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            mAdapter.addItems(mItems);
            mAdapter.notifyItemInserted(-1);
        }

    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;
        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
        }
//        public void bindGalleryItem(GalleryItem item) {
//            if(item.isNewItem()) {
//                mTitleTextView.setTextColor(Color.RED);
//            }else{
//                mTitleTextView.setTextColor(Color.BLACK);
//            }
//            mTitleTextView.setText(item.toString());
//        }
        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

        public void bindBitmap(Bitmap bitmap){
            mItemImageView.setImageBitmap(bitmap);
        }

    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;
        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }
        public void addItems(List<GalleryItem> list){
            modifyOldItems();
            mGalleryItems.addAll(list);
        }

        public void modifyOldItems(){
            for(GalleryItem galleryItem : mGalleryItems){
                galleryItem.setNewItem(false);
            }
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);

        }
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
//            Drawable placeholder = getResources().getDrawable(R.drawable.stk);
//            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl());
        }
        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }


}
