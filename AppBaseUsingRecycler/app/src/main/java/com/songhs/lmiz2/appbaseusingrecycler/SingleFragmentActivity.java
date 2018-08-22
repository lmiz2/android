package com.songhs.lmiz2.appbaseusingrecycler;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    private int LAYOUT_RES_ID = R.layout.main_fragment;
    private int FRAGMENT_RES_ID = R.id.fragment_container;

    @LayoutRes
    protected int getLayoutResId() {
        return LAYOUT_RES_ID;
    }

    protected void setTargetLayoutId(int id){
        this.LAYOUT_RES_ID = id;
    }

    protected void setTargetFragmentId(int id){
        this.FRAGMENT_RES_ID = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(FRAGMENT_RES_ID);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(FRAGMENT_RES_ID, fragment)
                    .commit();
        }
    }
}
