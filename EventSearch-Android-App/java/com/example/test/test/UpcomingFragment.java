package com.example.test.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.test.dao.UpComingEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpcomingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<UpComingEvent> upComingList;
    private OnFragmentInteractionListener mListener;
    private ListView upcomingListView;
    private UpComingEventAdapter upComingAdapter;
    private Spinner typeSpinner;
    private Spinner orderSpinner;
    private ArrayList<UpComingEvent> defaultUpcomingList;
    private String venueName;
    private ProgressBar progressBar;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
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
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }


    public void getUpcomingEvent(String venueName) {
        progressBar = getView().findViewById(R.id.progressBar_upcoming);
        progressBar.setVisibility(View.VISIBLE);
        final LinearLayout spinnerLinear = getView().findViewById(R.id.upcoming_linearSpinner);
        final LinearLayout upcomingNoResults = getView().findViewById(R.id.upcoming_noResults);
        final Context context = getContext();
        upComingList = new ArrayList<>();
        defaultUpcomingList = new ArrayList<>();
        upcomingListView = getView().findViewById(R.id.listView_upcoming);
        upComingAdapter = new UpComingEventAdapter(getContext(),upComingList);

        typeSpinner = getView().findViewById(R.id.spinner_type);
        orderSpinner = getView().findViewById(R.id.spinner_order);
        orderSpinner.setEnabled(false);
        upcomingListView.setAdapter(upComingAdapter);
        upComingList.clear();
        upComingAdapter.notifyDataSetChanged();
        defaultUpcomingList.clear();

        String url = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/up/"+venueName;
        ApiCall.getInputLocationEvents(getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && response != "") {
                    try {
                        JSONArray jsonUpcoming = new JSONArray(response);
                        if (jsonUpcoming.length() == 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            upcomingNoResults.setVisibility(LinearLayout.VISIBLE);
                        }
                        else {

                            for (int i = 0; i < jsonUpcoming.length(); i++ ) {
                                JSONObject row = jsonUpcoming.getJSONObject(i);
                                UpComingEvent upComingEvent = new UpComingEvent(row.getString("displayName"), row.getString("uri"),
                                        row.getString("artist"), row.getString("time"), row.getString("type"));
                                upComingList.add(upComingEvent);
                                defaultUpcomingList.add(upComingEvent);
                                upComingAdapter.notifyDataSetChanged();
                            }

                            progressBar.setVisibility(View.INVISIBLE);
                            spinnerLinear.setVisibility(LinearLayout.VISIBLE);
                            upcomingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    UpComingEvent event = (UpComingEvent) parent.getItemAtPosition(position);
                                    Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getUri()));
                                    startActivity(browerIntent);
                                }
                            });

                            typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    String type = typeSpinner.getSelectedItem().toString();

                                    String order = orderSpinner.getSelectedItem().toString();
                                    if ( type.contains("Event")  && order.equals("Ascending") ) {

                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.eventNameComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if ( type.contains("Event")  && order.equals("Descending")) {

                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.eventNameComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Time") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.timeComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Time") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.timeComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Artist") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.artistComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Artist") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.artistComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Type") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.typeComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Type") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.typeComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else {

                                        orderSpinner.setEnabled(false);
                                        upComingList.clear();;
                                        upComingList.addAll(defaultUpcomingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String type = typeSpinner.getSelectedItem().toString();
                                    String order = orderSpinner.getSelectedItem().toString();
                                    if ( type.contains("Event")  && order.equals("Ascending") ) {

                                        Collections.sort(upComingList, UpComingEvent.eventNameComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.contains("Event")  && order.equals("Descending")) {

                                        Collections.sort(upComingList, UpComingEvent.eventNameComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Time") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.timeComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Time") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.timeComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Artist") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.artistComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Artist") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.artistComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Type") && order.equals("Ascending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.typeComparator);
                                        upComingAdapter.notifyDataSetChanged();
                                    }
                                    else if (type.equals("Type") && order.equals("Descending")) {
                                        orderSpinner.setEnabled(true);
                                        Collections.sort(upComingList, UpComingEvent.typeComparator);
                                        Collections.reverse(upComingList);
                                        upComingAdapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }






                    } catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);
                upcomingNoResults.setVisibility(LinearLayout.VISIBLE);
                Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUpcomingFragmentInteraction(uri);
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
        void onUpcomingFragmentInteraction(Uri uri);
    }
}
