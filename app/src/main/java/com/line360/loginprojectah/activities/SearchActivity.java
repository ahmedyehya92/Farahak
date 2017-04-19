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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.line360.loginprojectah.R;
import com.line360.loginprojectah.app.AppConfig;
import com.line360.loginprojectah.app.AppController;
import com.line360.loginprojectah.helper.Hall;
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
    public static ArrayList<String> citys;
    private TextView citytx;
    private SQLiteHandler db;
    private static String api_key;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        citys = new ArrayList<String>();
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        api_key = user.get("uid");

      /*  citys.add(0, "اسيوط");
        citys.add(1, "سوهاج");
        citys.add(2, "اسيوط");
        citys.add(3, "سوهاج");
        citys.add(4, "اسيوط");
        citys.add(5, "سوهاج");
        citys.add(6, "اسيوط");
        citys.add(7, "سوهاج");
        citys.add(8, "اسيوط");
        citys.add(9, "سوهاج");
        citys.add(10, "اسيوط");
        citys.add(11, "سوهاج"); */
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setVisibility(View.GONE);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,citys);
        mListView.setAdapter(adapter);
        citys.clear();


        setupSearchView();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("البحث بإسم المحافظة");
    }

    public boolean onQueryTextChange(String newText) {
        RequestQueue queue = AppController.getInstance().getRequestQueue();
        if(queue!=null){

            queue.cancelAll("req_search");
            Log.i(TAG, "all search request were cancelled");
        }
        citys.clear();
        searchQuery(newText);
        Log.d(TAG, "Text Response: " + newText);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                citytx = (TextView) findViewById(android.R.id.text1);
                String city = citytx.getText().toString();
                final String city_key = "citykey";
                String cityIntent = city;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.putExtra(city_key, cityIntent);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
        if (TextUtils.isEmpty(newText)) {
            RequestQueue queueE = AppController.getInstance().getRequestQueue();
            if(queueE!=null){

                queue.cancelAll("req_search");
                Log.i(TAG, "all search request were cancelled");
            }

            citys.clear();

            mListView.setVisibility(View.GONE);
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
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
                            citys.add(i,city.getString("city"));

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



}
