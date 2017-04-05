package com.line360.loginprojectah;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.line360.loginprojectah.helper.MhallsSize;

import com.line360.loginprojectah.helper.SQLiteHandler;
import com.line360.loginprojectah.helper.SessionManager;
import com.line360.loginprojectah.helper.Hall;
import com.line360.loginprojectah.helper.HallAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    private TextView txtName;
    private TextView txtEmail;
    private ImageView imageView;
    private static String TAG;
    private SQLiteHandler db;
    private SessionManager session;
    public static ArrayList<Hall> mHalls;
    ArrayList<Hall> mHallsTemp = new ArrayList<Hall>();
    int totalItemCountVisible=0; //totalItems visible
    Thread BackThreed; //only one threed
    HallAdapter Adapter;
    Activity context;
    AsyncTextTask asyncTextTask;
    RelativeLayout searchbox;
    LinearLayout snacBar1;
    LinearLayout loadLayout;
    String url1;
    ProgressBar mprogressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        /* new AsyncTextTask().execute(url2);
        new AsyncTextTask().execute(url3);
        new AsyncTextTask().execute(url4);
        new AsyncTextTask().execute(url5);
        new AsyncTextTask().execute(url6); */

        context = this;
        mHalls = new ArrayList<Hall>();
        ListView listview = (ListView) findViewById(R.id.list);
        loadLayout = (LinearLayout) findViewById(R.id.load_layout);
        loadLayout.setVisibility(View.VISIBLE);
        mprogressBar = (ProgressBar) findViewById(R.id.progBar);
        if (mprogressBar != null) {
            mprogressBar.setIndeterminate(true);
            mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        searchbox = (RelativeLayout) findViewById(R.id.search_box);
        snacBar1 = (LinearLayout) findViewById(R.id.snack_bar1);
        snacBar1.setVisibility(View.GONE);
        Adapter = new HallAdapter(getApplicationContext(), mHalls);       // انتبه انت عندك هنا constructor مختلف

        listview.setAdapter(Adapter);
        mHalls.clear();

        final String city_key = "citykey";



        if ((mHalls.size()==MhallsSize.getmArraylistSize())&& MhallsSize.getmArraylistSize()!=0)
        {}
        else {
            boolean result = isNetworkStatePermissionGranted();
            if(isNetworkAvailable(this))
            {
                Intent cityintent = getIntent();
                String city = cityintent.getStringExtra(city_key);

               if (city != null) {

                    switch (city) {
                        case "":
                            break;
                        case "اسيوط":
                            url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getassuthalls.php";

                            asyncTextTask = new AsyncTextTask();
                            asyncTextTask.execute(url1);
                            break;
                        case "سوهاج":
                            url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getsohaghalls.php";

                            asyncTextTask = new AsyncTextTask();
                            asyncTextTask.execute(url1);
                            break;

                    }


                }


            /*    url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getassuthalls.php";

                asyncTextTask = new AsyncTextTask();
                asyncTextTask.execute(url1); */
            }
            else {
               // Toast.makeText(MainActivity.this,"خطأ في الإتصال حاول مرة أخرى",Toast.LENGTH_SHORT).show();
                loadLayout.setVisibility(View.GONE);
                snacBar1.setVisibility(View.VISIBLE);
            }




        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hall hall = mHalls.get(position);
                final String facebook_key = "fkey";
                final String phone_key = "phonekey";
                final String image1_key = "image1key";
                final String image2_key = "image2key";
                final String image3_key = "image3key";
                final String preparing_key = "preparingkey";
                final String name_key = "namekey";
                final String price_key = "pricekey";
                final String address_key = "addresskey";
                String name = hall.getmName();
                String price = hall.getmPrice();
                String address = hall.getmAddress();
                String image1 = hall.getmUrlOfImage1();
                String image2 = hall.getmUrlOfImage2();
                String image3 = hall.getmUrlOfImage3();
                String facebook = hall.getmFacebook();
                String phone = hall.getmPhone();
                String preparing = hall.getmPreparing();
                Intent myIntent = new Intent(view.getContext(), HallActivity.class);
                myIntent.putExtra(image1_key,image1);
                myIntent.putExtra(image2_key,image2);
                myIntent.putExtra(image3_key,image3);
                myIntent.putExtra(facebook_key,facebook);
                myIntent.putExtra(phone_key,phone);
                myIntent.putExtra(preparing_key,preparing);
                myIntent.putExtra(name_key,name);
                myIntent.putExtra(price_key,price);
                myIntent.putExtra(address_key,address);
                startActivityForResult(myIntent, 0);

            }

        });


        searchbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        SearchActivity.class);
                startActivity(i);
                finish();

            }
        });



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
        String email = user.get("email");

        // Displaying the user details on the screen


        // Logout button click event


        //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header=navigationView.getHeaderView(0);
        txtName = (TextView)header.findViewById(R.id.nav_name);
        txtName.setText(name);
        txtEmail = (TextView)header.findViewById(R.id.nav_email);
        txtEmail.setText(email);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_item_logout:

                        logoutUser();
                        break;


                    case R.id.navigation_item_attachment:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;


                }

                return true;

            }
        });



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
    protected void onStart() {

   /*     if ((mHalls.size()==MhallsSize.getmArraylistSize())&& MhallsSize.getmArraylistSize()!=0)
        {}
        else {
               boolean result = isNetworkStatePermissionGranted();
            if(isNetworkAvailable(this))
            {
                final String city_key = "citykey";
                Intent cityintent = getIntent();
                String city = cityintent.getStringExtra(city_key).toString();
                switch (city)
                {
                    case "":
                        break;
                    case "اسيوط":
                        url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getassuthalls.php";

                        asyncTextTask = new AsyncTextTask();
                        asyncTextTask.execute(url1);
                        break;
                    case "سوهاج":
                        url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getsohaghalls.php";

                        asyncTextTask = new AsyncTextTask();
                        asyncTextTask.execute(url1);
                        break;

                }





              url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getassuthalls.php";

                asyncTextTask = new AsyncTextTask();
                asyncTextTask.execute(url1);
            }
            else {
                Toast.makeText(MainActivity.this,"خطأ في الإتصال حاول مرة أخرى",Toast.LENGTH_SHORT).show();
            } */






        super.onStart();
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }*/



    @Override
    protected void onStop() {
       /* mHalls.clear();
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // your stuff to update the UI
                Adapter.notifyDataSetChanged();
            }
        });*/


        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public class AsyncTextTask extends AsyncTask<String, String, List<Hall>> {


        @Override
        protected void onPreExecute() {
            //before works
        }

        @Override
        protected List<Hall> doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String NewsData;
                //define the url we have to connect with
                URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(40000);//set timeout to 5 seconds

                try {
                    urlConnection.equals(null);
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //convert the stream to string
                    NewsData = ConvertInputToStringNoChange(in);
                    //send to display data
                    publishProgress(NewsData);
                    urlConnection.disconnect();


                } catch (Exception ex) {
                    //end connection
                    urlConnection.disconnect();


                }

            } catch (Exception ex) {

            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {

            // التعديل بتاعك هيكون هنا عشان تقدر تاخد الداتا اللي انت عايزها

            try {
                //display response data
                JSONArray jsonArray = new JSONArray(progress[0]);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject hall = jsonArray.getJSONObject(i);
                    mHalls.add(i,new Hall(hall.getInt("id"),hall.getString("name"),hall.getInt("price"),hall.getString("phone"),hall.getString("facebook"),hall.getString("instagram"),hall.getString("twitter"),hall.getString("preparing"),hall.getString("image1"),hall.getString("image2"),hall.getString("image3"),hall.getString("address")));

                }


                MhallsSize.setmArraylistSize(mHalls.size());

                runOnUiThread(new Runnable() {
                    public void run() {
                        //Do something on UiThread

                    }
                });

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // your stuff to update the UI
                        Adapter.notifyDataSetChanged();
                    }
                });




               /* JSONObject jsonObject = new JSONObject(progress[0]);
                JSONObject query = jsonObject.getJSONObject("query");
                JSONObject result = query.getJSONObject("results");
                JSONObject channel = result.getJSONObject("channel");
                JSONObject location = channel.getJSONObject("location");
                String cityStr = location.getString("city");
                JSONObject item = channel.getJSONObject("item");
                JSONObject condition = item.getJSONObject("condition");
                String tempStr = condition.getString("temp");
                String conditionStr = condition.getString("text");
                String dateStr = condition.getString("date");
                String urlOfImage = "http://i.imgur.com/loHDpym.png";


                mHalls.add(0, new Hall(cityStr, tempStr, conditionStr, dateStr, urlOfImage)); */


                Log.d("temp", mHalls.toString());
                loadLayout.setVisibility(View.GONE);


            } catch (Exception ex) {


            }


        }

        protected void onPostExecute(String result2) {

        }
    }


    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }


    public boolean isNetworkAvailable(Context ctx)
    {
        ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public  boolean isNetworkStatePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
