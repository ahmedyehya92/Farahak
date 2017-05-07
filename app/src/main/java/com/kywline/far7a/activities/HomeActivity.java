package com.kywline.far7a.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.kywline.far7a.GifView;
import com.kywline.far7a.R;
import com.kywline.far7a.app.AppConfig;
import com.kywline.far7a.app.AppController;
import com.kywline.far7a.helper.CityList;
import com.kywline.far7a.helper.Comment_Model;
import com.kywline.far7a.helper.DateSharedPref;
import com.kywline.far7a.helper.SQLiteHandler;
import com.kywline.far7a.helper.SeenRecyclerViewAdabter;
import com.kywline.far7a.helper.SessionManager;
import com.kywline.far7a.helper.TopSeenModel;
import com.trncic.library.DottedProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends MainActivity {
    DateSharedPref dateSharedpref;
    private static RecyclerView seenRecyclerView;
    public static ArrayList<TopSeenModel> mSeenArrayList;
    SeenRecyclerViewAdabter seenAdapter;
    private TextView txtTimerDay, txtTimerHour, txtTimerMinute, txtTimerSecond;
    private Handler handler;
    private Runnable runnable;
    private TextView tvDisplayDate;
    private TextView tvDisplayName;
    private Button btnChangeDate;
    private DrawerLayout mDrawerLayout;
    private int year;
    private int month;
    private int day;
    private SQLiteHandler db;
    private static String apiKey;
    private static DottedProgressBar progressBar;
    private SessionManager session;
    static final int DATE_DIALOG_ID = 999;
    LinearLayout loadTopSeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadTopSeen = (LinearLayout) findViewById(R.id.load_topseen);
        loadTopSeen.setVisibility(View.VISIBLE);
        progressBar = (DottedProgressBar) findViewById(R.id.progress);
        progressBar.startProgress();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite

        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        apiKey = user.get("uid");






        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        loadBackdrop();

        initTopSeenViews();
        mSeenArrayList = new ArrayList<TopSeenModel>();
        seenAdapter = new SeenRecyclerViewAdabter(HomeActivity.this, mSeenArrayList);
        seenRecyclerView.setAdapter(seenAdapter);
        seenAdapter.notifyDataSetChanged();



        getTopSeenHalls();
        seenRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        dateSharedpref = new DateSharedPref(this);
        txtTimerDay = (TextView) findViewById(R.id.tx_days);
        txtTimerHour = (TextView) findViewById(R.id.tx_hours);
        txtTimerMinute = (TextView) findViewById(R.id.tx_minutes);
        txtTimerSecond = (TextView) findViewById(R.id.tx_seconds);
        tvDisplayName = (TextView) findViewById(R.id.tx_desplay_name);

        tvDisplayName.setText(name);

        addListenerOnButton();
        setCurrentDateOnView();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_item_logout:
                        mDrawerLayout.closeDrawers();
                        dateSharedpref.SaveData(null);
                        logoutUser();
                        break;

                    case R.id.nav_item_share_app:
                        mDrawerLayout.closeDrawers();
                        final String appPackageName = getPackageName();
                        String shareBody = "قم بتجربة تطبيق فرحة ومشاركته مع أصدقائك \n https://play.google.com/store/apps/details?id="+ appPackageName;
                        Log.d("share", shareBody.toString());
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "مشاركة تطبيق فرحة");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_with)));
                        break;

                    case R.id.navigation_item_attachment:
                        mDrawerLayout.closeDrawers();
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_home:
                        mDrawerLayout.closeDrawers();

                        break;

                    case R.id.nav_item_Exit:
                        mDrawerLayout.closeDrawers();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        break;

                }

                return true;

            }
        });



    }

    private void initTopSeenViews() {
        seenRecyclerView = (RecyclerView) findViewById(R.id.seen_recycler_view);
        seenRecyclerView.setHasFixedSize(true);
        seenRecyclerView
                .setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;




        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    //countdown
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.tx_date);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview






        String date = dateSharedpref.LoadData();


        if (date != "nodate") {
            countDownStart(date.toString());
            btnChangeDate.setText("تغيير الموعد");
            tvDisplayDate.setText(date);
        }

        else {
            tvDisplayDate.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }




    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.btn_set_date);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(day)
                    .append("-").append(month+1).append("-").append(year)
                    .append(" "));



            dateSharedpref.SaveData(new StringBuilder().append(year).append("-").append(month+1).append("-")
                    .append(day).toString());

            if (handler!= null) {

                handler.removeCallbacks(runnable);
            }
            btnChangeDate.setText("تغيير الموعد");


            countDownStart(new StringBuilder().append(year).append("-").append(month+1).append("-")
                    .append(day).toString());



        }
    };

    public void countDownStart(final String time) {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
// Please here set your event date//YYYY-MM-DD

                    Date futureDate = dateFormat.parse(time);
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        txtTimerDay.setText("" + String.format("%02d", days));
                        txtTimerHour.setText("" + String.format("%02d", hours));
                        txtTimerMinute.setText(""
                                + String.format("%02d", minutes));
                        txtTimerSecond.setText(""
                                + String.format("%02d", seconds));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);


    }

    // end of coundown code




    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load("http://i.imgur.com/Gd7S8GT.jpg").centerCrop().into(imageView);
    }




    private void getTopSeenHalls() {
        // Tag used to cancel the request



        String tag_string_req = "req_get_top_seen";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_TOP_SEEN_HALLS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray jsonArray = jObj.getJSONArray("halls");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject hall = jsonArray.getJSONObject(i);
                            mSeenArrayList.add(i,new TopSeenModel(hall.getString("id"),hall.getString("name"),hall.getString("price"),hall.getString("image"),hall.getString("address"),hall.getString("see")));

                        }
                        progressBar.stopProgress();
                        loadTopSeen.setVisibility(View.GONE);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Do something on UiThread

                            }
                        });

                        HomeActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                seenAdapter.notifyDataSetChanged();
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



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("seen", "a");


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", apiKey);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
