package com.example.test.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements EasyPermissions.PermissionCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppCompatAutoCompleteTextView autoCompleteTextView;
    private AutoCompleteAdapter autoCompleteAdapter;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private EditText locationEditText;
    private TextView keywordError;
    private TextView inputLocationError;
    private Button searchBtn;
    private Button clearBtn;
    private Button currentLocationBtn;
    private Button inputLocationBtn;
    private RadioGroup radioGroup;
    private Spinner categorySpinner;
    private EditText distanceEditText;
    private Spinner unitSpinner;
    private EditText inputLocationEditText;
    private double lat = 0;
    private double lng = 0;
    private final int LOCATION = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        requestPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    @AfterPermissionGranted(LOCATION)
    private void requestPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), permission)) {
            getLocation();

            System.out.println("already get permission");

            System.out.println("lat = "+lat);
            System.out.println("lng = "+lng);

        }
        else {
            EasyPermissions.requestPermissions(this, getString(R.string.rational),
                    LOCATION, permission);
            System.out.println("request permission");

//            EasyPermissions.requestPermissions(
//                    new PermissionRequest.Builder(this, LOCATION, permission)
//                            .setRationale(R.string.rational)
//                            .setPositiveButtonText(R.string.rationale_ask_ok)
//                            .setNegativeButtonText(R.string.rationale_ask_cancel)
//                            .setTheme(R.style.permission_style)
//                            .build());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            System.out.println("requestion Allowed");
            getLocation();

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("PERMISSION", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        System.out.println("Permission request denyed");

//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
    }



    private void getLocation() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        CustomLocationListener customLocationListener = new CustomLocationListener();
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,customLocationListener);
        }catch(SecurityException e) {
            e.printStackTrace();
        }
        lat = customLocationListener.getLat();
        lng = customLocationListener.getLng();
        if (lat == 0 && lng == 0) {
            try {
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat = currentLocation.getLatitude();
                lng = currentLocation.getLongitude();
                System.out.println("get Last Known Location ");
            }catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private class CustomLocationListener implements LocationListener {
        private double lat;
        private double lng;
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                 lat = location.getLatitude();
                 lng = location.getLongitude();
            }

        }

        public double getLat() {
            return lat;
        }
        public  double getLng() {
            return  lng;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        keywordError =  view.findViewById(R.id.keyword_error);
        inputLocationError = view.findViewById(R.id.location_error);
        locationEditText = view.findViewById(R.id.edit_inputLocation);
        searchBtn = view.findViewById(R.id.btn_submit);
        categorySpinner = view.findViewById(R.id.spinner_category);
        distanceEditText = view.findViewById(R.id.distance_edit);
        unitSpinner = view.findViewById(R.id.unit_spinner);
        inputLocationEditText = view.findViewById(R.id.edit_inputLocation);



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(v);
            }
        });

        clearBtn = view.findViewById(R.id.btn_clear);
        currentLocationBtn = view.findViewById(R.id.radio_current);
        inputLocationBtn = view.findViewById(R.id.radio_input);
        final RadioButton currentBtn = view.findViewById(R.id.radio_current);
        final RadioButton inputBtn = view.findViewById(R.id.radio_input);

        radioGroup  = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == currentLocationBtn.getId()) {
                    locationEditText.setEnabled(false);
                }
                else {
                    locationEditText.setEnabled(true);
                }
            }
        });


        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

        clearBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentBtn.setChecked(true);
                inputLocationEditText.setText("");
                inputBtn.setChecked(false);
                autoCompleteTextView.setText("");
                locationEditText.setEnabled(false);
                distanceEditText.setText("");
                unitSpinner.setSelection(0);
                categorySpinner.setSelection(0);
            }
        });
        autoCompleteAdapter = new AutoCompleteAdapter(SearchFragment.this.getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //selectedOption.setText(autoCompleteAdapter.getObject(position));
                    }
                }
        );

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
        );

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        System.out.println("make api call");
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    private void submit (View v) {
        if (Validation.isEmpty(autoCompleteTextView)) {
            System.out.println("auto is null     ");
            keywordError.setVisibility(View.VISIBLE);
            System.out.println("location =============    "+locationEditText.getText().toString());
            if (radioGroup.getCheckedRadioButtonId() == currentLocationBtn.getId()) {
                if (Validation.isEmpty(locationEditText)) {
                    System.out.println("location is null     ");
                    inputLocationError.setVisibility(View.VISIBLE);
                }
            }
            Toast.makeText(SearchFragment.this.getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();
        }
        else {
            if (radioGroup.getCheckedRadioButtonId() == inputLocationBtn.getId()) {
                if (Validation.isEmpty(locationEditText)) {
                    inputLocationError.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchFragment.this.getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();
                }
                else {
                    keywordError.setVisibility(View.INVISIBLE);
                    inputLocationError.setVisibility(View.INVISIBLE);
                    send();
                }
            }
            else {
                keywordError.setVisibility(View.INVISIBLE);
                inputLocationError.setVisibility(View.INVISIBLE);
                send();
            }
        }
    }

    private void send() {
        String keyword = autoCompleteTextView.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String radius = distanceEditText.getText().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        String inputLocation = inputLocationEditText.getText().toString();
        String url;
        if (radioGroup.getCheckedRadioButtonId() == currentLocationBtn.getId()) {
            getLocation();
            if (lat == 0 && lng == 0) {
                Toast.makeText(SearchFragment.this.getActivity(),"Please open permission for location at setting",Toast.LENGTH_LONG).show();
            }
            else {
                System.out.println("-----------send request lat ="+lat+" lng = "+lng);
                url =  "http://yeluochangkong.us-east-2.elasticbeanstalk.com/event/currentLocationEvent/?keyword="+keyword+"&category="+category+"&radius="+
                        radius+"&unit="+unit+"&lat="+lat+"&lng="+lng;
                Intent intent = new Intent(SearchFragment.this.getActivity(),EventListActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }

        }
        else {
             url =  "http://yeluochangkong.us-east-2.elasticbeanstalk.com/event/inputLocationEvent/?keyword="+keyword+"&category="+category+"&radius="+
                    radius+"&unit="+unit+"&inputLocation="+inputLocation;
             System.out.println("input location url"+url);
            Intent intent = new Intent(SearchFragment.this.getActivity(),EventListActivity.class);
            intent.putExtra("url",url);
            startActivity(intent);
        }



    }

    private void makeApiCall(String text) {
        ApiCall.make(SearchFragment.this.getActivity(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> list = new ArrayList<>();
                try {
                    // JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        list.add(row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                autoCompleteAdapter.setOptions(list);
                autoCompleteAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
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
