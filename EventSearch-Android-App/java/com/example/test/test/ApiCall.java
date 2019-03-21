package com.example.test.test;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class ApiCall {
    private RequestQueue requestQueue;
    private static  Context context;
    private static ApiCall instance;

    public ApiCall (Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized ApiCall getInstance(Context context) {
        if (instance == null) {
            instance = new ApiCall(context);
        }
        return instance;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context context, String query, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (query != null && query.trim() != "") {
            String url = "http://yeluochangkong.us-east-2.elasticbeanstalk.com/auto/"+query;
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url, listener, errorListener);
            ApiCall.getInstance(context).addToRequestQueue(stringRequest);
        }
    }
    public static void getInputLocationEvents(Context context, String url, Response.Listener<String> listener, Response.ErrorListener errorListener ) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, listener, errorListener);
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy( 5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApiCall.getInstance(context).addToRequestQueue(stringRequest);
    }


}
