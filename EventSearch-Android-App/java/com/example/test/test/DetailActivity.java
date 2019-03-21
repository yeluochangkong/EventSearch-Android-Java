package com.example.test.test;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.dao.Event;
import com.example.test.dao.EventDetail;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener, ArtistFragment.OnFragmentInteractionListener, VenueFragment.OnFragmentInteractionListener,UpcomingFragment.OnFragmentInteractionListener {
    private String url;
    private EventDetail eventDetail;
    private PageAdapterDetail pageAdapterDetail;
    private ActionBar actionBar;
    private ImageButton favBtn;
    private ImageButton twitterBtn;
    private String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String eventId = getIntent().getStringExtra("eventId");
        eventName = getIntent().getStringExtra("eventName");
        url = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/eventDetail/"+eventId;

        eventName = getIntent().getStringExtra("eventName");

        final TabLayout detailTabLayOut = findViewById(R.id.tablayout_detail);
        final ViewPager viewPager = findViewById(R.id.viewPager_detail);
         pageAdapterDetail = new PageAdapterDetail(getSupportFragmentManager(),detailTabLayOut.getTabCount());


        viewPager.setAdapter(pageAdapterDetail);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(detailTabLayOut));
        detailTabLayOut.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    String secondTag = pageAdapterDetail.getSecondTag();
                    ArtistFragment f1 = (ArtistFragment)getSupportFragmentManager().findFragmentByTag(secondTag);
                    if (f1 == null) {
                    }
                    else {
                        f1.getAllArtists(eventDetail);
                    }
                }
                if (tab.getPosition() == 2) {
                    String thirdTag = pageAdapterDetail.getThirdTag();
                    VenueFragment  f2 = (VenueFragment)getSupportFragmentManager().findFragmentByTag(thirdTag);
                    if (f2 == null) {
                    }
                    else {
                        f2.getVenueInfo(eventDetail.getVenue());
                    }
                }
                if(tab.getPosition() == 3) {
                    String forthTag = pageAdapterDetail.getForthTag();
                    UpcomingFragment  f3 = (UpcomingFragment)getSupportFragmentManager().findFragmentByTag(forthTag);
                    if (f3 == null) {

                    }
                    else {
                        f3.getUpcomingEvent(eventDetail.getVenue());
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

    public EventDetail getEventDetail() {
        return this.eventDetail;
    }

    public String getUrl() {
        return url;
    }
    public String getEventName() {
        return eventName;
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onEventFragmentInteraction(final EventDetail eventDetail) {
        this.eventDetail = eventDetail;
        final Context context = this;
        actionBar = getSupportActionBar();
        actionBar.setTitle(eventDetail.getArtist());
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        TextView title = findViewById(R.id.textView_title);
        title.setText(Validation.subString(eventName));
        favBtn = findViewById(R.id.actionBar_fav);
        twitterBtn = findViewById(R.id.actionBar_twitter);

        String text = "";
        if (eventDetail != null) {
            String artist = this.eventDetail.getArtist();
            if (artist != null) {
                String[] ar = artist.split("\\|");
                artist = Arrays.toString(ar);
            }
            text = "Check out " + artist + " located at"+eventDetail.getVenue() + ". Website:"+this.eventDetail.getBuyTicketAt();
        }
        final String urlTwitter = "https://twitter.com/intent/tweet?text="+text;
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlTwitter));
                startActivity(browserIntent);
            }
        });
        final SharedPreferences prefs = getSharedPreferences("favList", MODE_PRIVATE);
         String json = prefs.getString(eventDetail.getEventId(),"");
        if (json == "") {
            favBtn.setImageResource(R.drawable.ic_heart_fill_white);
        }
        else {
            favBtn.setImageResource(R.drawable.ic_heart_fill_red);
        }
        favBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor prefsEditor = prefs.edit();
                String json = prefs.getString(eventDetail.getEventId(),"");
                if (json == "") {
                    System.out.println("put into fav");
                    Event event = new Event();
                    event.setEventName(eventName);
                    event.setCategory(eventDetail.getCategory());
                    event.setDate(eventDetail.getTime());
                    event.setId(eventDetail.getEventId());
                    event.setVenueInfo(eventDetail.getVenue());
                    String tip = eventName + "was added to favorites";
                    Toast.makeText(context,tip,Toast.LENGTH_LONG).show();

                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    event.setAddTime(timeStamp);


                    Gson gson = new Gson();
                    String myJson = gson.toJson(event);
                    prefsEditor.putString(event.getId(), myJson);
                    prefsEditor.commit();
                    favBtn.setImageResource(R.drawable.ic_heart_fill_red);
                }
                else {
                    System.out.println("delete from fav");
                    String tip = eventName + "was removed from favorites";
                    Toast.makeText(context,tip,Toast.LENGTH_LONG).show();
                    prefsEditor.remove(eventDetail.getEventId());
                    prefsEditor.commit();
                    favBtn.setImageResource(R.drawable.ic_heart_fill_white);
                }

            }
        });



    }


    @Override
    public void onVenueFragmentInteraction(String venueName) {

    }

    @Override
    public void onUpcomingFragmentInteraction(Uri uri) {

    }
}
