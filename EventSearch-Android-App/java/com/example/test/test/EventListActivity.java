package com.example.test.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.test.dao.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {
    private ArrayList<Event> events;
    private ListView listView;
    private ListView eventListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        context = this;

        events = new ArrayList<>();
        final LinearLayout linearBar = findViewById(R.id.progressBar_linear_eventList);
        linearBar.setVisibility(LinearLayout.VISIBLE);

        eventListView = findViewById(R.id.ListView_event);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event)parent.getItemAtPosition(position);
                String eventId = event.getId();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("eventId",eventId);
                intent.putExtra("eventName",event.getEventName());
                startActivity(intent);

            }
        });


        listView = findViewById(R.id.ListView_event);

        final LinearLayout noResltsLinear = findViewById(R.id.noResults_linear);

        final Context context = this;
       String url = getIntent().getStringExtra("url");

        ApiCall.getInputLocationEvents(this, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    System.out.println("test --"+response);

                    if (array.length() == 0) {
                        linearBar.setVisibility(LinearLayout.INVISIBLE);
                        noResltsLinear.setVisibility(LinearLayout.VISIBLE);
                    }
                    else {
                       
                        linearBar.setVisibility(LinearLayout.INVISIBLE);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            Event event = new Event(row.getString("date"), row.getString("eventName"), row.getString("category"), row.getString("venueInfo"), row.getString("id"), row.getString("fav"));
                            events.add(event);
                            EventListAdapter eventListAdapter = new EventListAdapter(context, events);
                            listView.setAdapter(eventListAdapter);
                        }
                    }

                } catch(Exception e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
             
                linearBar.setVisibility(LinearLayout.INVISIBLE);
                noResltsLinear.setVisibility(LinearLayout.VISIBLE);
                Toast.makeText(context,"An error occurs",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getEventList () {
        final LinearLayout linearBar = findViewById(R.id.progressBar_linear_eventList);
      

        eventListView = findViewById(R.id.ListView_event);


        listView = findViewById(R.id.ListView_event);
        final LinearLayout noResltsLinear = findViewById(R.id.noResults_linear);
       
        final Context context = this;
        if (events == null || events.size() == 0) {
            linearBar.setVisibility(LinearLayout.INVISIBLE);
          
        }
        else
        {
            EventListAdapter eventListAdapter = new EventListAdapter(context, events);
            listView.setAdapter(eventListAdapter);
            linearBar.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getEventList();    // 
    }
}
