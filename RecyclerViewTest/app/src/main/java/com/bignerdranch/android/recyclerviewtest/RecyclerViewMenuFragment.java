package com.bignerdranch.android.recyclerviewtest;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

public class RecyclerViewMenuFragment extends Fragment {
    private static final String TAG = "RecyclerViewMenuFragment";

    private RecyclerView mRecyclerView;

    public static RecyclerViewMenuFragment newInstance(){
        return new RecyclerViewMenuFragment();
    }
}
