package com.example.test.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.test.dao.UpComingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpComingEventAdapter extends ArrayAdapter {
    public UpComingEventAdapter(Context context,  ArrayList<UpComingEvent> list) {
        super(context, 0, list);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        UpComingEvent upComingEvent = (UpComingEvent)getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.uncoming_events, parent, false);
        }
        TextView displayName = convertView.findViewById(R.id.upcoming_displayName);
        TextView artist = convertView.findViewById(R.id.upcoming_artist);
        TextView time = convertView.findViewById(R.id.upcoming_time);
        TextView type = convertView.findViewById(R.id.upcoming_type);



        displayName.setText(Validation.checkString(upComingEvent.getDisplayName()));
        artist.setText(Validation.checkString(upComingEvent.getArtist()));
       // time.setText(Validation.checkString(upComingEvent.getTime()));
       if (upComingEvent.getType().equals("")) {
           time.setText("N/A");
       }
        else  {



           try{
               if (upComingEvent.getTime() != "" && upComingEvent.getTime().length() > 12) {
                   SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   Date date = formatter.parse(upComingEvent.getTime());  // may not have time, only have date
                   SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                   String formateDate = format2.format(date);
                   time.setText(formateDate);
               }
               else {
                   SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd ");
                   Date date = formatter2.parse(upComingEvent.getTime());  // may not have time, only have date
                   SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy ");
                   String formateDate = format3.format(date);
                   time.setText(formateDate);
               }


           }catch(Exception e) {
               e.printStackTrace();
           }
       }
        type.setText("Type:"+Validation.checkString(upComingEvent.getType()));
        return convertView;
    }
}
