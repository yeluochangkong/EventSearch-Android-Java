package com.example.test.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.test.dao.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends ArrayAdapter {
    private ArrayList<Artist> list;

    public ArtistAdapter(Context context,  ArrayList<Artist> list) {
        super(context, 0, list);
        this.list = list;
    }




    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {


        final Artist artist = (Artist)getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist, parent, false);
        }
        TextView titleTextView = convertView.findViewById(R.id.textView_artistTitleValue);
        titleTextView.setText(Validation.checkString(artist.getName()));
       // titleTextView.setText(artist.getName());
        if (artist.getFollowers() == null && artist.getPopularity() == null && artist.getCheckAt() == null) {
           System.out.println("remove all views");
            TableLayout artistLayout =  convertView.findViewById(R.id.table_artist);
            artistLayout.removeAllViewsInLayout();
        }
        else {
            System.out.println("not remove");
            TextView nameTextView = convertView.findViewById(R.id.textView_artistNameValue);
            TextView followersTextView = convertView.findViewById(R.id.textView_followerValue);
            TextView popularityTextView = convertView.findViewById(R.id.textView_popularityValue);
            TextView checkAtTextView = convertView.findViewById(R.id.textView_checkAtValue);

            nameTextView.setText(Validation.checkString(artist.getName()));
            String follower = Validation.checkString(artist.getFollowers());
            String re = Validation.followerFormat(follower);

            followersTextView.setText(re);
            popularityTextView.setText(Validation.checkString(artist.getPopularity()));


            if (artist.getCheckAt().equals("")) {
                checkAtTextView.setText("N/A");
            }
            else {
                String text = "Spotify";
                SpannableString content = new SpannableString(text);
                content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                checkAtTextView.setText(content);
                checkAtTextView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artist.getCheckAt()));
                        getContext().startActivity(browerIntent);
                    }
                });
            }
        }
        List<String> photos = artist.getPhotos();
       LinearLayout photoLayout = convertView.findViewById(R.id.linearLayout_photo);
        photoLayout.removeAllViewsInLayout();  // -------------------
        int i = 0;
        System.out.println("adapter length ="+photos.size());
        for (String photo: photos) {

            if (i < 9) {

                ImageView imageView = new ImageView(getContext());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);

                photoLayout.addView(imageView, params);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                Picasso.with(getContext()).load(photo).resize(width,0).into(imageView);
            }
           i++;
        }

        return convertView;

    }
}
