package com.example.test.test;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public class PageAdapterDetail extends FragmentPagerAdapter {
    private int numOfTabs;
    private String firstTag;
    private String secondTag;
    private String thirdTag;
    private String forthTag;

    public PageAdapterDetail(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                 return new EventFragment();
            case 1:
                return new ArtistFragment();
            case 2:
                return new VenueFragment();
            case 3:
                return new UpcomingFragment();
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
            case 2:
                 thirdTag = createdFragment.getTag();
                break;
            case 3:
                 forthTag = createdFragment.getTag();
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

    public String getThirdTag() {
        return thirdTag;
    }

    public String getForthTag() {
        return forthTag;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
