package com.example.test.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.dao.Event;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EventListAdapter extends ArrayAdapter {
    private FavFragment favFragment;
   // private Context context;
    public EventListAdapter(Context context, ArrayList<Event> list) {
        super(context, 0,list);
        //context = context;
    }

    public EventListAdapter(Context context, ArrayList<Event> list, FavFragment favFragment) {
        super(context, 0,list);
        this.favFragment  =favFragment;
        //context = context;
    }
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        final Event event = (Event)getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list, parent, false);
        }

        ImageButton favBtn =  convertView.findViewById(R.id.imageButton);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView eventNameTextView = convertView.findViewById(R.id.eventName_textView);
        TextView venueNameTextView = convertView.findViewById(R.id.venueName_textView);
        TextView dateTextView = convertView.findViewById(R.id.date_textView);
       // TextView idTextView = convertView.findViewById(R.id.textView_id);
       final  ImageButton imageBtn = convertView.findViewById(R.id.imageButton);


        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getContext().getSharedPreferences("favList", Context.MODE_PRIVATE);
                String json = prefs.getString(event.getId(), "");
                SharedPreferences.Editor prefsEditor = prefs.edit();
                if (json == null || json == "") {
                    System.out.println("put into fav");
                    String tip = event.getEventName() + "was added to favorites";


                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    event.setAddTime(timeStamp);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(event);
                    prefsEditor.putString(event.getId(), myJson);
                    prefsEditor.commit();
                    imageBtn.setImageResource(R.drawable.ic_heart_fill_red);
                    Toast.makeText(getContext(),tip,Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("delete from fav");
                    String tip = event.getEventName() + "was removed from favorites";

                    prefsEditor.remove(event.getId());
                    prefsEditor.commit();
                    Toast.makeText(getContext(),tip,Toast.LENGTH_LONG).show();
                    imageBtn.setImageResource(R.drawable.ic_heart_outline_black);
                    if (favFragment != null) {
                        System.out.println(" !!!!!!! != null ");
                        favFragment.getFav();
                    }
                }
            }
        });

         System.out.println("did dapter called !!!!!!!!!!!!!  ");
         String category = event.getCategory().toLowerCase();
         if (category.contains("music")) imageView.setImageResource(R.drawable.music_icon);
         if (category.contains("art")) imageView.setImageResource(R.drawable.art_icon);
         if (category.contains("film")) imageView.setImageResource(R.drawable.film_icon);
         if (category.contains("sport")) imageView.setImageResource(R.drawable.sport_icon);
         if (category.contains("miscellaneous")) imageView.setImageResource(R.drawable.miscellaneous_icon);
        eventNameTextView.setText(event.getEventName());
        venueNameTextView.setText(event.getVenueInfo());
        dateTextView.setText(event.getDate());
       // idTextView.setText(event.getId());

        SharedPreferences prefs = getContext().getSharedPreferences("favList", Context.MODE_PRIVATE);
        if(prefs != null) {
            String json = prefs.getString(event.getId(), "");

            if (json != null && json != "") {
                imageBtn.setImageResource(R.drawable.ic_heart_fill_red);
            }
            else {
                imageBtn.setImageResource(R.drawable.ic_heart_outline_black);
            }
        }
        else {
            imageBtn.setImageResource(R.drawable.ic_heart_outline_black);
        }

        return convertView;
    }
}
