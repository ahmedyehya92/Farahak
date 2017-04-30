package com.kywline.far7a.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kywline.far7a.R;
import com.kywline.far7a.app.AppConfig;
import com.kywline.far7a.app.AppController;
import com.kywline.far7a.helper.CityList;
import com.kywline.far7a.helper.CityListAdapter;
import com.kywline.far7a.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CitiesActivity extends AppCompatActivity {
    RelativeLayout fullGov;
    final String gov_key = "govekey";
    LinearLayout loadLayout;
    ProgressBar mprogressBar;
    private static String gov ;

    private static final String TAG = CitiesActivity.class.getSimpleName();
    private ListView citListView;
    public static ArrayList<CityList> cities;
    private SQLiteHandler db;
    private static String api_key;
    CityListAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cities);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        cities = new ArrayList<CityList>();
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        api_key = user.get("uid");
        citListView = (ListView) findViewById(R.id.list_cities);

        loadLayout = (LinearLayout) findViewById(R.id.load_layout_cities);
        loadLayout.setVisibility(View.VISIBLE);
        mprogressBar = (ProgressBar) findViewById(R.id.progBar_cities);
        if (mprogressBar != null) {
            mprogressBar.setIndeterminate(true);
            mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        cityAdapter = new CityListAdapter(getApplicationContext(),cities);
        citListView.setAdapter(cityAdapter);
        cities.clear();

        Intent cityintent = getIntent();
        gov = cityintent.getStringExtra(gov_key);
        getCities(gov);
        fullGov = (RelativeLayout) findViewById(R.id.tx_full_govs);

        fullGov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String city_key = "citykey";
                String id_gov = gov;
                final String status_gov_key = "statusGovKey";
                final int status = 1;
                Intent govIntent = new Intent(CitiesActivity.this, MainActivity.class);
                govIntent.putExtra(city_key,gov);
                govIntent.putExtra(status_gov_key,status);
                startActivityForResult(govIntent, 0);
            }
        });


        citListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityList city = cities.get(position);
                String idCity = city.getCityId();
                final String city_key = "citykey";
                String cityIntent = idCity;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.putExtra(city_key, cityIntent);
                startActivityForResult(myIntent, 0);

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                onBackPressed();
                return true;




        }

        return super.onOptionsItemSelected(item);
    }


    private void getCities(final String gove) {
        // Tag used to cancel the request



        String tag_string_req = "req_get_cities";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CITIES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Cities Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray jsonArray = jObj.getJSONArray("cities");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject city = jsonArray.getJSONObject(i);
                            cities.add(i,new CityList(city.getString("id"),city.getString("city")));

                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Do something on UiThread

                            }
                        });

                        CitiesActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                cityAdapter.notifyDataSetChanged();
                            }
                        });
                        loadLayout.setVisibility(View.GONE);

                        // Launch login activity
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " Error: " + "error");


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("govid", gove);


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", api_key);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}
