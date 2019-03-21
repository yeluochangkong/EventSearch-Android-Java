package com.example.test.test;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private String firstTag;
    private  String secondTag;
    private FragmentManager fragmentManager;

    public PageAdapter (FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchFragment f1 = (SearchFragment) fragmentManager.findFragmentByTag(firstTag);
                if (f1 == null) {
                    return new SearchFragment();
                }
                else {
                    return f1;
                }
            case 1:
                return new FavFragment();
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                firstTag = createdFragment.getTag();
                break;
            case 1:
                secondTag = createdFragment.getTag();
                break;
        }
        return createdFragment;
    }
    public String getFirstTag() {
        return firstTag;
    }

    public String getSecondTag() {
        return secondTag;
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
