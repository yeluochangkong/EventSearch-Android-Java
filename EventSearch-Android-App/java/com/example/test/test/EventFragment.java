package com.example.test.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.test.dao.EventDetail;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView artistTextView = view.findViewById(R.id.textView_artistValue);
        final TextView venueTextView  = view.findViewById(R.id.textView_venueValue);
        final TextView timeTextView  = view.findViewById(R.id.textView_timeValue);
        final TextView categoryTextView  = view.findViewById(R.id.textView_categoryValue);
        final TextView priceRangeTextView = view.findViewById(R.id.textView_priceRangeValue);
        final TextView ticketStatusTextView = view.findViewById(R.id.textView_ticketStatusValue);
        final TextView buyTicketAtTextView = view.findViewById(R.id.textView_buyTicketAtValue);
        final TextView seatMapTextView = view.findViewById(R.id.textView_seatMapValue);
        final ProgressBar eventProgressbar = view.findViewById(R.id.progressBar_event);
        final Context context = getContext();
        final TableLayout tableDsipaly = view.findViewById(R.id.event_LinearDisplay);
        final TextView noResults = view.findViewById(R.id.textView_eventNoResults);
        noResults.setVisibility(TextView.INVISIBLE);

        eventProgressbar.setVisibility(View.VISIBLE);
        tableDsipaly.setVisibility(TableLayout.INVISIBLE);

        String url = ((DetailActivity) getActivity()).getUrl();

        System.out.println("url =  "+url);
        ApiCall.getInputLocationEvents(getActivity(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject detailJson = new JSONObject(response);
                    System.out.println("Event Name Display name"+response);
                    final EventDetail eventDetail = new EventDetail(detailJson.getString("artist"), detailJson.getString("venue"), detailJson.getString("time")
                            , detailJson.getString("category"), detailJson.getString("priceLow"), detailJson.getString("priceHigh"), detailJson.getString("ticketStatus"),
                            detailJson.getString("buyTicketAt"), detailJson.getString("seatMap"), detailJson.getString("eventId"));


                    eventProgressbar.setVisibility(View.INVISIBLE);
                    tableDsipaly.setVisibility(TableLayout.VISIBLE);
                    if (eventDetail.getArtist().equals("")) {
                        artistTextView.setText("N/A");
                    }
                    else {
                        artistTextView.setText(eventDetail.getArtist());
                    }
                    if (eventDetail.getVenue().equals("")) {
                        venueTextView.setText("N/A");
                    }
                    else {
                        venueTextView.setText(eventDetail.getVenue());
                    }
                   if (eventDetail.getTime().equals("")) {
                       timeTextView.setText("N/A");
                   }
                    else {

                       if (eventDetail.getTime() != "" && eventDetail.getTime().length() > 12) {
                           SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                           Date date = formatter.parse(eventDetail.getTime());  // may not have time, only have date
                           SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                           String formateDate = format2.format(date);
                           timeTextView.setText(formateDate);
                       }
                       else {
                           SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd ");
                           Date date = formatter2.parse(eventDetail.getTime());  // may not have time, only have date
                           SimpleDateFormat format3 = new SimpleDateFormat("MMM dd, yyyy ");
                           String formateDate = format3.format(date);
                           timeTextView.setText(formateDate);
                       }

                   }
                    categoryTextView.setText(eventDetail.getCategory());

                    ticketStatusTextView.setText(eventDetail.getTicketStatus());
                    DecimalFormat  formate = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);

                    if (eventDetail.getLowPrice().equals("") && eventDetail.getHighPrice().equals("")) {
                        priceRangeTextView.setText("N/A");
                    }
                    else {
                        String low = formate.format(Float.parseFloat(eventDetail.getLowPrice()));
                        String high = formate.format(Float.parseFloat(eventDetail.getHighPrice()));
                        priceRangeTextView.setText(low+"~"+high);
                    }

                    String text = "Ticketmaster";
                    SpannableString content = new SpannableString(text);
                    content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                    if (eventDetail.getBuyTicketAt().equals("")) {
                        buyTicketAtTextView.setText("N/A");
                    }
                    else {

                        buyTicketAtTextView.setText(content);
                    }

                    String text2 = "View Here";
                    SpannableString content2 = new SpannableString(text2);
                    content2.setSpan(new UnderlineSpan(), 0, text2.length(), 0);
                    if (eventDetail.getSeatMap().equals("")) {
                        seatMapTextView.setText("N/A");
                    }
                    else {
                        seatMapTextView.setText(content2);
                    }

                    if (!eventDetail.getBuyTicketAt().equals("")) {
                        buyTicketAtTextView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(eventDetail.getBuyTicketAt()));
                                startActivity(browserIntent);
                            }
                        });
                    }

                    if (!eventDetail.getSeatMap().equals("")) {
                        seatMapTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("-------"+eventDetail.getSeatMap());
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(eventDetail.getSeatMap()));
                                startActivity(browserIntent);
                            }
                        });
                    }

                    mListener.onEventFragmentInteraction(eventDetail);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                eventProgressbar.setVisibility(View.INVISIBLE);
                noResults.setVisibility(TextView.VISIBLE);
                Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();

            }
        });

    }

     //TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(EventDetail eventDetail) {
        if (mListener != null) {
            mListener.onEventFragmentInteraction(eventDetail);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onEventFragmentInteraction(EventDetail eventDetail);
    }
}
