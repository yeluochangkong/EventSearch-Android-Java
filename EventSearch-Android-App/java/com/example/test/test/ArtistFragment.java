package com.example.test.test;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.test.dao.Artist;
import com.example.test.dao.EventDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    private  ArrayList<Artist> artists;
//    private  ArtistAdapter artistAdapter;
//    private  ProgressBar progressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArtistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
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
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getAllArtists(EventDetail eventDetail)  {
        final ProgressBar progressBar = getView().findViewById(R.id.progressBar_artist);
        final TextView noResults = getView().findViewById(R.id.artist_noResults);
        final Context context = getContext();
        noResults.setVisibility(TextView.INVISIBLE);

        progressBar.setVisibility(View.VISIBLE);

        if (eventDetail.getArtist().equals("")) {
            noResults.setVisibility(TextView.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }
        else {
            String[] artistName = eventDetail.getArtist().split("\\|");

            String category = eventDetail.getCategory();

            final ArrayList artists = new ArrayList<>();
            final ArtistAdapter artistAdapter  = new ArtistAdapter(this.getContext(), artists);
            final ListView artistListView = getView().findViewById(R.id.ListView_artist);
            artistListView.setAdapter(artistAdapter);

            for (final String name : artistName) {
                final Artist artist = new Artist();
                if (category.toLowerCase().contains("music")) {

                    String urlArtist = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/artist/"+name;

                    ApiCall.getInputLocationEvents(getActivity(), urlArtist, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            artist.setName(name);
                            try {
                                if (!response.equals("null")) {
                                
                                    JSONObject jsonArtist = new JSONObject(response);
                                    artist.setName(jsonArtist.getString("name"));
                                    artist.setFollowers(jsonArtist.getString("followers"));
                                    artist.setPopularity(jsonArtist.getString("popularity"));
                                    artist.setCheckAt(jsonArtist.getString("checkAt"));
                                }
                                String url2 = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/photo/"+name;
                                ApiCall.getInputLocationEvents(getActivity(), url2, new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String response) {
                                        List<String> photos = new ArrayList<>();
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                                photos.add(jsonRow.getString("link"));

                                            }
                                            artist.setPhotos(photos);
                                            if (artists.size() < 2) {
                                                artists.add(artist);
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                            artistAdapter.notifyDataSetChanged();

                                        } catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        noResults.setVisibility(TextView.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();
                                    }
                                });

                            } catch(Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            noResults.setVisibility(TextView.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    String url2 = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/photo/"+name;
                    ApiCall.getInputLocationEvents(getActivity(), url2, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            List<String> photos = new ArrayList<>();
                            if (response != null && response != "") {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        photos.add(jsonRow.getString("link"));
                                    }
                                    artist.setName(name);
                                    artist.setPhotos(photos);
                                    if (artists.size() < 2) {
                                        artists.add(artist);
                                    }
                                    artistAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.INVISIBLE);
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            noResults.setVisibility(TextView.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
