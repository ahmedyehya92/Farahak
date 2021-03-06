package com.kywline.far7a.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kywline.far7a.R;
import com.kywline.far7a.app.AppConfig;
import com.kywline.far7a.helper.MhallsSize;

import com.kywline.far7a.helper.SQLiteHandler;
import com.kywline.far7a.helper.SessionManager;
import com.kywline.far7a.helper.Hall;
import com.kywline.far7a.helper.HallAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private static boolean asyncTaskStatus;
    // Variables for scroll listener
    int mVisibleThreshold = 5;
    int mCurrentPage = 0;
    int mPreviousTotal = 0;
    boolean mLoading = true;
    boolean mLastPage = false;
    boolean userScrolled = false;
    Integer page = 1;
    private static RelativeLayout loadBar;
    private static JSONArray jsonArray;
    private DrawerLayout mDrawerLayout;
    ListView listview;
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
    private static int status;
    LinearLayout snacBar1;
    LinearLayout loadLayout;
    final String urlGetHalls = "https://telegraphic-miscond.000webhostapp.com/halls_manager/v1/halls";;
    ProgressBar mprogressBar;
    private static String apiKey;
    private static String cityEntry;
    private static JSONObject hall;
    private static boolean lastItem;
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
        listview = (ListView) findViewById(R.id.list);
        loadLayout = (LinearLayout) findViewById(R.id.load_layout);
        loadBar = (RelativeLayout) findViewById(R.id.loading_bar);
        loadBar.setVisibility(View.GONE);
    //    loadLayout.setVisibility(View.VISIBLE);
        mprogressBar = (ProgressBar) findViewById(R.id.progBar);
        if (mprogressBar != null) {
            mprogressBar.setIndeterminate(true);
            mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        snacBar1 = (LinearLayout) findViewById(R.id.snack_bar1);
        snacBar1.setVisibility(View.GONE);
        Adapter = new HallAdapter(getApplicationContext(), mHalls);       // انتبه انت عندك هنا constructor مختلف

        listview.setAdapter(Adapter);
        mHalls.clear();

        final String city_key = "citykey";
        final String status_gov_key = "statusGovKey";
        Intent statusIntent = getIntent();
        status = statusIntent.getIntExtra(status_gov_key,0);


        if ((mHalls.size()==MhallsSize.getmArraylistSize())&& MhallsSize.getmArraylistSize()!=0)
        {}
        else {
         boolean result = isNetworkStatePermissionGranted();
            if(isNetworkAvailable(this))
            {
                Intent cityintent = getIntent();
                String city = cityintent.getStringExtra(city_key);

               if (city != null) {

                   cityEntry = city;


                   asyncTextTask = new AsyncTextTask();
                   if (status == 1) {
                       asyncTextTask.execute(AppConfig.URL_GET_GOVS_HALL);
                   }

                   else {
                       asyncTextTask.execute(urlGetHalls);
                   }
                }


            /*    url1 = "https://telegraphic-miscond.000webhostapp.com/Farahak/getassuthalls.php";

                asyncTextTask = new AsyncTextTask();
                asyncTextTask.execute(url1); */
           }
          else {
               // Toast.makeText(MainActivity.this,"خطأ في الإتصال حاول مرة أخرى",Toast.LENGTH_SHORT).show();
             //   loadLayout.setVisibility(View.GONE);
                snacBar1.setVisibility(View.VISIBLE);
            }




        }
        implementScrollListener();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hall hall = mHalls.get(position);
                final String id_key = "idKey";
                final String facebook_key = "fkey";
                final String phone_key = "phonekey";
                final String image1_key = "image1key";
                final String preparing_key = "preparingkey";
                final String name_key = "namekey";
                final String price_key = "pricekey";
                final String address_key = "addresskey";
                String idHall = hall.getmId();
                String name = hall.getmName();
                String price = hall.getmPrice();
                String address = hall.getmAddress();
                String facebook = hall.getmFacebook();
                String phone = hall.getmPhone();
                String preparing = hall.getmPreparing();
                Intent myIntent = new Intent(view.getContext(), HallActivity.class);
                myIntent.putExtra(id_key,idHall);
                myIntent.putExtra(facebook_key,facebook);
                myIntent.putExtra(phone_key,phone);
                myIntent.putExtra(preparing_key,preparing);
                myIntent.putExtra(name_key,name);
                myIntent.putExtra(price_key,price);
                myIntent.putExtra(address_key,address);
                startActivityForResult(myIntent, 0);

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
        apiKey = user.get("uid");


        // Displaying the user details on the screen


        // Logout button click event


        //
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header=navigationView.getHeaderView(0);
     /*   txtName = (TextView)header.findViewById(R.id.nav_name);
        txtName.setText(name);
        txtEmail = (TextView)header.findViewById(R.id.nav_email);
        txtEmail.setText(email); */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_item_logout:

                        logoutUser();
                        break;


                    case R.id.navigation_item_find_venues:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
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

                    case R.id.nav_home:
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
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
    lastItem = false;
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
            asyncTaskStatus = false;
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






                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(40000);








                //waiting for 7000ms for response
           //     urlConnection.setConnectTimeout(40000);//set timeout to 5 seconds

                try {
              //      urlConnection.equals(null);


                    urlConnection.setRequestMethod("POST");
                  urlConnection.addRequestProperty("Authorization", apiKey);

                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    List<NameValuePair> parames = new ArrayList<NameValuePair>();

                    if (status == 1)
                    {
                        parames.add(new BasicNameValuePair("govid", cityEntry));
                        parames.add(new BasicNameValuePair("page", page.toString()));
                    }

                    else {
                        parames.add(new BasicNameValuePair("city", cityEntry));
                    }

                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(parames));
                    writer.flush();
                    writer.close();
                    os.close();



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
                JSONObject jsonObject = new JSONObject(progress[0]);
             //   JSONObject error = jsonObject.getJSONObject("error");
                Boolean error = jsonObject.getBoolean("error");
                jsonArray = jsonObject.getJSONArray("halls");
                if (jsonArray.length()==0)
                {
                    lastItem = true;
                }
                int index;
                int jsonarray;
            /*  if (page > 1)
                {
                    index = mHalls.size()+1;
                    jsonarray = jsonArray.length()+1;
                }
                else
                {
                    index=0;
                    jsonarray= jsonArray.length();
                }*/
         //       if (index==0) {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        hall = jsonArray.getJSONObject(i);
                        mHalls.add(new Hall(hall.getInt("id"), hall.getString("name"), hall.getInt("price"), hall.getString("phone"), hall.getString("facebook"), hall.getString("instagram"), hall.getString("youtube"), hall.getString("preparing"), hall.getString("image1"), hall.getString("address"), hall.getInt("see")));

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
                    }
                //    loadLayout.setVisibility(View.GONE);
             //   }
          /*      else if (index>0)
                {
                    hall=null;
                    for (int i = index; i < index+10 ; i++) {

                        for (int l = 0; l < jsonArray.length(); l++) {
                            hall = jsonArray.getJSONObject(l);
                        }
                        mHalls.add(i, new Hall(hall.getInt("id"), hall.getString("name"), hall.getInt("price"), hall.getString("phone"), hall.getString("facebook"), hall.getString("instagram"), hall.getString("twitter"), hall.getString("preparing"), hall.getString("image1"), hall.getString("image2"), hall.getString("image3"), hall.getString("address"), hall.getInt("see")));

                    }

                }*/

         /*       JSONArray jsonArray = new JSONArray(progress[0]);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject hall = jsonArray.getJSONObject(i);
                    mHalls.add(i,new Hall(hall.getInt("id"),hall.getString("name"),hall.getInt("price"),hall.getString("phone"),hall.getString("facebook"),hall.getString("instagram"),hall.getString("twitter"),hall.getString("preparing"),hall.getString("image1"),hall.getString("image2"),hall.getString("image3"),hall.getString("address")));

                } */


                MhallsSize.setmArraylistSize(mHalls.size());





                    asyncTaskStatus = true;

                   if (page>1) {
                       loadBar.setVisibility(View.GONE);
                       scrollMyListViewToBottom(jsonArray.length());
                   }

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



            }
            catch (Exception ex) {

            System.out.println(ex.getMessage());
            }


        }

        protected void onPostExecute(String result2) {

        }



        private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
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

    private void implementScrollListener() {
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // If scroll state is touch scroll then set userScrolled
                // true
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // Now check if userScrolled is true and also check if
                // the item is end then update list view and set
                // userScrolled to false
                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    if (asyncTaskStatus != false) {
                        asyncTextTask.cancel(true);
                        page++;
                        updateListView();
                    }

                }
            }
        });
    }

    private void updateListView() {
        // Show Progress Layout


        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve
        asyncTextTask = new AsyncTextTask();
        if (status == 1&&lastItem!= true) {
            loadBar.setVisibility(View.VISIBLE);
            asyncTextTask.execute(AppConfig.URL_GET_GOVS_HALL);

        }

        else {

        }

        Adapter.notifyDataSetChanged();// notify adapter

        // Toast for task completion


        // After adding new data hide the view.

    /*    new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Loop for 3 items

                loadBar.setVisibility(View.GONE);

                // asynctask



            }
        }, 5000); */
    }

    private void scrollMyListViewToBottom(final int length) {
        listview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listview.setSelection(Adapter.getCount() - length);
            }
        });
    }
}
