package com.bignerdranch.android.photogallery;

import android.content.Intent;
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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private static int page = 1;

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    String query;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_photo_gallery, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                QueryPreferences.setStoredQuery(getActivity(), s);
                updateItems();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange: " + s);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItems() {
        query = QueryPreferences.getStoredQuery(getActivity());
        new FetchItemsTask(query).execute(page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
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

        updateItems();


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
                    new FetchItemsTask(query).execute(++page);
                }else if(newState == 0 && !recyclerView.canScrollVertically(-1) && page <= 10){
                    Log.d(TAG, "onScrollStateChanged: ");
                    mAdapter.mGalleryItems = new ArrayList<GalleryItem>();
                    page =  1;
                    new FetchItemsTask(query).execute(page);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return v;
    }

    private class FetchItemsTask extends AsyncTask<Integer,Void,List<GalleryItem>> {
        private String mQuery;
        public FetchItemsTask(String query) {
            mQuery = query;
        }


        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {

            if (mQuery == null) {
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                return new FlickrFetchr().searchPhotos(mQuery);
            }

        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            mAdapter.addItems(mItems);
            mAdapter.notifyItemInserted(-1);
        }

    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImageView;
        private TextView mTextView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
            mTextView = itemView.findViewById(R.id.item_text_view);
            itemView.setOnClickListener(this);

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
            mTextView.setVisibility(View.GONE);
            mItemImageView.setVisibility(View.VISIBLE);
        }

        public void setVisibleInit(){
            mTextView.setVisibility(View.VISIBLE);
            mItemImageView.setVisibility(View.GONE);
        }
        public void bindGalleryItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW, mGalleryItem.getPhotoPageUri());
            startActivity(i);
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
            photoHolder.bindGalleryItem(galleryItem);
            photoHolder.setVisibleInit();
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
