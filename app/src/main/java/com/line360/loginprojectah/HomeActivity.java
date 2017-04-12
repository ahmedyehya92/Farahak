package com.line360.loginprojectah;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.line360.loginprojectah.helper.DateSharedPref;
import com.line360.loginprojectah.helper.SQLiteHandler;
import com.line360.loginprojectah.helper.SessionManager;
import com.line360.loginprojectah.helper.SQLiteHandler;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class HomeActivity extends MainActivity {
    DateSharedPref dateSharedpref;
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
    private SessionManager session;
    static final int DATE_DIALOG_ID = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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






        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");
        loadBackdrop();

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
                        dateSharedpref.SaveData(null);
                        logoutUser();
                        break;


                    case R.id.navigation_item_attachment:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent);

                        break;


                }

                return true;

            }
        });



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

}
