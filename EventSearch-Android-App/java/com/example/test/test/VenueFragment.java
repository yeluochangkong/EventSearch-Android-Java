package com.example.test.test;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.test.dao.EventDetail;
import com.example.test.dao.Venue;
import com.example.test.test.databinding.FragmentVenueBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VenueFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VenueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VenueFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Venue venue = new Venue();
    private TextView venueName;
    private TextView venueAddress;
    private TextView venueCity;
    private TextView venuePhoneNumber;
    private TextView venueOpenHour;
    private TextView venueGeneralRule;
    private TextView venueChildRule;
    private ProgressBar progressBar;
    private double lat;
    private double lng;
    private GoogleMap myMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VenueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VenueFragment newInstance(String param1, String param2) {
        VenueFragment fragment = new VenueFragment();
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
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng sydney = new LatLng(lat,lng);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void updateMap(double lat, double lng) {
        System.out.println("!!!!!!!!!!!!!!!!!! "+lat+" lng= "+lng);
        LatLng sydney = new LatLng(lat,lng);
        myMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVenueBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_venue, container, false);
        View view = binding.getRoot();
        binding.setVenueData(venue);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EventDetail eventDetail = ((DetailActivity)getActivity()).getEventDetail();
       // getVenueInfo(eventDetail.getVenue());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        System.out.println(" on create view mapFragemnt ----- "+mapFragment);
        mapFragment.getMapAsync(this);
    }

    public void getVenueInfo(String venueName) {
        final LinearLayout progressLinear  = getView().findViewById(R.id.venue_linearProgressBar);

        final Context context = getContext();

        final LinearLayout venueDisplay = getView().findViewById(R.id.venue_linear);
        final LinearLayout venueNoResults = getView().findViewById(R.id.venue_noResults);

         progressLinear.setVisibility(LinearLayout.VISIBLE);
        venueDisplay.setVisibility(LinearLayout.INVISIBLE);
        venueNoResults.setVisibility(LinearLayout.INVISIBLE);
        String url = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/venue/"+venueName;
        System.out.println("call getVenueInfo");
        ApiCall.getInputLocationEvents(this.getContext(), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    try {
                        System.out.println("-------venue "+response);
                        if (response.equals("null")) {
                            System.out.println("-------venue  null");
                            //progressBar.setVisibility(View.INVISIBLE);
                            progressLinear.setVisibility(LinearLayout.INVISIBLE);
                            venueNoResults.setVisibility(LinearLayout.VISIBLE);
                        }
                        else {
                            System.out.println("-------venue not null");
                            JSONObject jsonVenue = new JSONObject(response);
                            if (venue == null) venue = new Venue();
                            venue.venueName.set(Validation.checkString(jsonVenue.getString("venueName")));
                            venue.address.set(Validation.checkString(jsonVenue.getString("address")));
                            venue.city.set(Validation.checkString(jsonVenue.getString("city")));
                            venue.openHours.set(Validation.checkString(jsonVenue.getString("openHours")));
                            venue.phoneNumber.set(Validation.checkString(jsonVenue.getString("phoneNumber")));
                            venue.generalRule.set(Validation.checkString(jsonVenue.getString("generalRule")));
                            venue.childRule.set(Validation.checkString(jsonVenue.getString("childRule")));
                            venue.lat.set(jsonVenue.getString("lat"));
                            venue.lng.set(jsonVenue.getString("lng"));
                            venueDisplay.setVisibility(LinearLayout.VISIBLE);
                            lat = Double.parseDouble(jsonVenue.getString("lat"));
                            lng= Double.parseDouble(jsonVenue.getString("lng"));
                            updateMap(lat, lng);
                            progressLinear.setVisibility(LinearLayout.INVISIBLE);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                venueNoResults.setVisibility(TextView.VISIBLE);
                progressLinear.setVisibility(LinearLayout.INVISIBLE);
                Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String venueName) {
        if (mListener != null) {
            mListener.onVenueFragmentInteraction(venueName);
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
        void onVenueFragmentInteraction(String venueName);
    }
}
