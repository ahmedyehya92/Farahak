package com.line360.loginprojectah.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.line360.loginprojectah.R;
import com.line360.loginprojectah.app.AppConfig;
import com.line360.loginprojectah.app.AppController;
import com.line360.loginprojectah.helper.CitySearch;
import com.line360.loginprojectah.helper.CitySearchAdapter;
import com.line360.loginprojectah.helper.Gov;
import com.line360.loginprojectah.helper.GovAdapter;
import com.line360.loginprojectah.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private SearchView mSearchView;
    private ListView mListView;
    private ListView govListView;
    public static ArrayList<CitySearch> cities;
    public static ArrayList<Gov> govs;
    private TextView citytx;
    private SQLiteHandler db;
    private static String api_key;
    CitySearchAdapter adapter;
    GovAdapter goveAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_search);
        getGovs("a");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        cities = new ArrayList<CitySearch>();
        govs = new ArrayList<Gov>();
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        api_key = user.get("uid");

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        govListView = (ListView) findViewById(R.id.list_govs);
        mListView.setVisibility(View.GONE);
        adapter = new CitySearchAdapter(getApplicationContext(),cities);
        goveAdapter = new GovAdapter(getApplicationContext(),govs);
        mListView.setAdapter(adapter);
        govListView.setAdapter(goveAdapter);
        govs.clear();
        cities.clear();

        mSearchView.animate();

        govListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gov gov = govs.get(position);
                String idGove = gov.getCityId();
                final String gove_key = "govekey";
                String govIntent = idGove;
                Intent myIntent = new Intent(view.getContext(), CitiesActivity.class);
                myIntent.putExtra(gove_key, govIntent);
                startActivity(myIntent);

            }
        });


        setupSearchView();


    }

    private void setupSearchView() {

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("اختر مدينتك");
    }

    public boolean onQueryTextChange(String newText) {

        RequestQueue queue = AppController.getInstance().getRequestQueue();
        if(queue!=null){

            queue.cancelAll("req_search");
            Log.i(TAG, "all search request were cancelled");
        }
        cities.clear();
        searchQuery(newText);
        Log.d(TAG, "Text Response: " + newText);








        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CitySearch citySearch = cities.get(position);
                String idCity = citySearch.getCityId();
                final String city_key = "citykey";
                String cityIntent = idCity;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.putExtra(city_key, cityIntent);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
        if (TextUtils.isEmpty(newText)) {
            if(queue!=null){

                queue.cancelAll("req_search");
                Log.i(TAG, "all search request were cancelled");
            }

            cities.clear();

            mListView.setVisibility(View.GONE);
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        RequestQueue queueE = AppController.getInstance().getRequestQueue();
        if(queueE!=null){

            queueE.cancelAll("req_search");
            Log.i(TAG, "all search request were cancelled");
        }
        mListView.setVisibility(View.GONE);
        return false;
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

    @Override
    protected void onStart() {


        super.onStart();
    }

    /**
     * Function to search in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void searchQuery(final String search) {
        // Tag used to cancel the request
        String tag_string_req = "req_search";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Search Response: " + response.toString());
                mListView.setVisibility(View.VISIBLE);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray jsonArray = jObj.getJSONArray("cities");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject city = jsonArray.getJSONObject(i);
                            cities.add(i,new CitySearch(city.getString("id"),city.getString("governerote"),city.getString("city")));

                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Do something on UiThread

                            }
                        });

                        SearchActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                adapter.notifyDataSetChanged();
                            }
                        });
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
                params.put("search", search);


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










    private void getGovs(final String goves) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_govs";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_GOVES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Goves Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray jsonArray = jObj.getJSONArray("governorates");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject gov = jsonArray.getJSONObject(i);
                            govs.add(i,new Gov(gov.getString("id"),gov.getString("governerote")));

                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Do something on UiThread

                            }
                        });

                        SearchActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                goveAdapter.notifyDataSetChanged();
                            }
                        });
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
                params.put("govs", goves);


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
