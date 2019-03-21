package com.example.test.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnFragmentInteractionListener,FavFragment.OnFragmentInteractionListener{
    private PageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tablayout);

//        ListView test = findViewById(R.id.listView_fav);

        final ViewPager viewPager = findViewById(R.id.viewPager);

        if (pageAdapter == null) {
            pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        }
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    String secondTag = pageAdapter.getSecondTag();
                    FavFragment f1 = (FavFragment)getSupportFragmentManager().findFragmentByTag(secondTag);
                    if (f1 == null) {
                    }
                    else {
                        f1.getFav();
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public void onFragmentInteraction(Uri uri) {

    }

}
