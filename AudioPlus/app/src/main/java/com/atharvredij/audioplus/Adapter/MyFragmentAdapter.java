package com.atharvredij.audioplus.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.atharvredij.audioplus.R;
import com.atharvredij.audioplus.UI.RecordFragment;
import com.atharvredij.audioplus.UI.RecordingsFragment;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MyFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RecordFragment();
        } else {
            return new RecordingsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.record_label);
        } else {
            return mContext.getString(R.string.recordings_label);
        }
    }
}
