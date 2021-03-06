package com.kywline.far7a.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kywline.far7a.R;
import com.kywline.far7a.app.AppConfig;
import com.kywline.far7a.app.AppController;
import com.kywline.far7a.helper.Comment_Model;
import com.kywline.far7a.helper.Hall;
import com.kywline.far7a.helper.ImageItem;
import com.kywline.far7a.helper.RecyclerView_Adapter;
import com.kywline.far7a.helper.Comment_RecyclerView_Adapter;

import com.kywline.far7a.helper.SQLiteHandler;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

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
import java.util.Map;

public class HallActivity extends AppCompatActivity {
    private static String TAG;
    private LinearLayout btn_rating;
    private static String phone_url ;
    private static String facebook_url ;
    private static String instagram_url ;
    private static String youtube_url ;
    private static RecyclerView imRecyclerView;
    private static RecyclerView commentRecyclerView;
    public static ArrayList<Comment_Model> mComments;
    public static ArrayList<ImageItem> imageItems;
    ImageView smallImage;
    public static TextView nameHall,priceHall,addressHall, hallSee;
    public static ExpandableTextView preparingTx;
    AsyncHallTask asyncHallTask;
    AsyncCommentTask asyncCommentTask;
    public static String id;
    final String idKey = "idKey";
    final String idKeyGo = "idHallkey";
    LinearLayout loadLayout;
    BottomNavigationView bottomNavigationView;
    ProgressBar mprogressBar;
    public  static String nameOhall;
    String url1;
    private static String apiKey;
    private SQLiteHandler db;
    Comment_RecyclerView_Adapter  adapter;
    RecyclerView_Adapter imAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Intent idIntent = getIntent();
        id = idIntent.getStringExtra(idKey);
        loadLayout = (LinearLayout) findViewById(R.id.load_layout_hall);
        loadLayout.setVisibility(View.VISIBLE);
        mprogressBar = (ProgressBar) findViewById(R.id.progBar);
        if (mprogressBar != null) {
            mprogressBar.setIndeterminate(true);
            mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        }




        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hall);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();


        apiKey = user.get("uid");

        initCollapsingToolbar();

        btn_rating = (LinearLayout) findViewById(R.id.btn_rating);
        smallImage = (ImageView) findViewById(R.id.small_image);
        nameHall = (TextView) findViewById(R.id.tx_name_hall);
        hallSee = (TextView) findViewById(R.id.tx_see);
        priceHall= (TextView) findViewById(R.id.tx_price_hall);
        addressHall = (TextView) findViewById(R.id.tx_address_hall);


        initViews();
       // populatRecyclerView();
        initCommentViews();
        mComments = new ArrayList<Comment_Model>();
        adapter = new Comment_RecyclerView_Adapter(HallActivity.this, mComments);
        commentRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter




        imageItems = new ArrayList<ImageItem>();
        imAdapter = new RecyclerView_Adapter(HallActivity.this,imageItems);
        imRecyclerView.setAdapter(imAdapter);
        imAdapter.notifyDataSetChanged();

        //config layout as button

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent idIntent = getIntent();
                final String idHall = idIntent.getStringExtra(idKey);

                Intent myIntent = new Intent(getApplicationContext(), ReviewActivity.class);
                myIntent.putExtra(idKeyGo,idHall);
                startActivity(myIntent);
                finish();

            }
        });


        final String smallImage_key = "image1key";
        Intent imgintent = getIntent();
        String smallImageInt = imgintent.getStringExtra(smallImage_key);
       /*  Picasso.with(getApplicationContext())
                .load(smallImageInt.toString())
                .into(smallImage);  */

        final String name_key = "namekey";
        Intent nameintent = getIntent();
        String name = nameintent.getStringExtra(name_key);

        final String price_key = "pricekey";
        Intent priceintent = getIntent();
        String price = priceintent.getStringExtra(price_key);

        final String address_key = "addresskey";
        Intent addressintent = getIntent();
        String address = addressintent.getStringExtra(address_key);

     //   nameHall.setText(name.toString());
     //   priceHall.setText(price.toString());
     //   addressHall.setText(address.toString());

        preparingTx = (ExpandableTextView) findViewById(R.id.expand_text_view);
        final String preparing_key = "preparingkey";
        Intent preintent = getIntent();
        String preparing = preintent.getStringExtra(preparing_key);

// description text
      //  preparingTx.setText(preparing.toString());




        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);



        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_call:
                                final String phone_key = "phonekey";
                                Intent intent = getIntent();
                                String phone = intent.getStringExtra(phone_key);
                                Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone_url));

                                boolean result = isPhoneCallPermissionGranted();

                                if (result==true) {
                                    try {
                                        startActivity(callintent);
                                        Toast toast = Toast.makeText(getApplication(),
                                                "جاري الإتصال",
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast toast = Toast.makeText(getApplication(),
                                                "الرجاء السماح بإجراء المكالمات",
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }

                                else
                                {
                                    boolean resultE = isPhoneCallPermissionGranted();
                                }
                                break;

                            case R.id.action_facebook:
                                final String facebook_key = "fkey";
                                Intent fintent = getIntent();
                                String username = fintent.getStringExtra(facebook_key);
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebook_url)));
                                } catch (Exception e) {
                                }
                                break;
                            case R.id.action_youtybe:
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youtube_url)));
                                } catch (Exception e) {
                                }
                                break;
                            case R.id.action_instagram:
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/" + instagram_url)));
                                } catch (Exception e) {
                                }
                                break;


                        }
                        return true;
                    }
                });

        url1 = "https://telegraphic-miscond.000webhostapp.com/halls_manager/v1/hall";

        asyncHallTask = new AsyncHallTask();
        asyncHallTask.execute(url1);
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

    public  boolean isPhoneCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    private void initViews() {

        imRecyclerView = (RecyclerView)
                findViewById(R.id.recycler_view);
        imRecyclerView.setHasFixedSize(true);
        imRecyclerView
                .setLayoutManager(new LinearLayoutManager(HallActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void initCommentViews() {
        commentRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView
                .setLayoutManager(new LinearLayoutManager(HallActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }


    private void populatRecyclerView() {
        final String image1_key = "image1key";
        final String image2_key = "image2key";
        final String image3_key = "image3key";
        Intent intent = getIntent();

        final String[] IMAGES1 = {intent.getStringExtra(image1_key),intent.getStringExtra(image2_key),intent.getStringExtra(image3_key)};
        ArrayList<ImageItem> arrayList = new ArrayList<>();
        for (int i = 0; i < IMAGES1.length; i++) {
            arrayList.add(new ImageItem(IMAGES1[i]));
        }
        RecyclerView_Adapter  adapter = new RecyclerView_Adapter(HallActivity.this, arrayList);
        imRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter

    }





    // 1 async -----------------------------------------------------------------------------------------------------

    public class AsyncHallTask extends AsyncTask<String, String, List<Hall>> {


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
                    parames.add(new BasicNameValuePair("id", id));


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

                if (error != true)
                {
                    JSONObject hall = jsonObject.getJSONObject("hall");
                    nameOhall = hall.getString("name");
                    nameHall.setText(nameOhall.toString());
                    String price = hall.getString("price");
                    priceHall.setText(price.toString());
                    phone_url = hall.getString("phone");
                    facebook_url = hall.getString("facebook");
                    instagram_url = hall.getString("instagram");
                    youtube_url = hall.getString("youtube");
                    String preparing = hall.getString("preparing");
                    preparingTx.setText(preparing.toString());
               /*     String image1 = hall.getString("image1");
                    Picasso.with(getApplicationContext())
                            .load(image1.toString())
                            .into(smallImage);
                    imageItems.add(0,new ImageItem(image1));
                    String image2 = hall.getString("image2");
                    imageItems.add(1,new ImageItem(image2));
                    String image3 = hall.getString("image3");
                    imageItems.add(2,new ImageItem(image3)); */
            /*        runOnUiThread(new Runnable() {
                        public void run() {
                            //Do something on UiThread

                        }
                    });

                    HallActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // your stuff to update the UI
                            imAdapter.notifyDataSetChanged();
                        }
                    }); */
                    String address = hall.getString("address");
                    addressHall.setText(address.toString());
                    String see = hall.getString("see");
                    hallSee.setText(see.toString());
                }



                String urlComment = "https://telegraphic-miscond.000webhostapp.com/halls_manager/v1/comments";

                getImages(id);
                asyncCommentTask = new AsyncCommentTask();
                asyncCommentTask.execute(urlComment);

         /*       JSONArray jsonArray = new JSONArray(progress[0]);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject hall = jsonArray.getJSONObject(i);
                    mHalls.add(i,new Hall(hall.getInt("id"),hall.getString("name"),hall.getInt("price"),hall.getString("phone"),hall.getString("facebook"),hall.getString("instagram"),hall.getString("twitter"),hall.getString("preparing"),hall.getString("image1"),hall.getString("image2"),hall.getString("image3"),hall.getString("address")));

                } */




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




            }
            catch (Exception ex) {


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



    public class AsyncCommentTask extends AsyncTask<String, String, String> {

        Intent idIntent = getIntent();
        final String id = idIntent.getStringExtra(idKey);
        @Override
        protected void onPreExecute() {
            //before works
        }

        @Override
        protected String doInBackground(String... params) {
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
                    parames.add(new BasicNameValuePair("hallid", id ));


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
                JSONArray jsonArray = jsonObject.getJSONArray("comments");
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject comment = jsonArray.getJSONObject(i);
                    mComments.add(i,new Comment_Model(comment.getString("name"),comment.getString("created_at"),comment.getString("review"),comment.getString("rate")));

                }



         /*       JSONArray jsonArray = new JSONArray(progress[0]);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject hall = jsonArray.getJSONObject(i);
                    mHalls.add(i,new Hall(hall.getInt("id"),hall.getString("name"),hall.getInt("price"),hall.getString("phone"),hall.getString("facebook"),hall.getString("instagram"),hall.getString("twitter"),hall.getString("preparing"),hall.getString("image1"),hall.getString("image2"),hall.getString("image3"),hall.getString("address")));

                } */




                runOnUiThread(new Runnable() {
                    public void run() {
                        //Do something on UiThread

                    }
                });

                HallActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // your stuff to update the UI
                        adapter.notifyDataSetChanged();
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





            }
            catch (Exception ex) {


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





// 2 async tool --------------------------------------------------------------------------------------------------------
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

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(nameOhall.toString()+""+"");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void getImages(final String hall_id) {

        // Tag used to cancel the request
        String tag_string_req = "get_images";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_IMAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");

                    if (!error)
                    {

                        JSONArray jsonArray = jsonObject.getJSONArray("images");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject image = jsonArray.getJSONObject(i);
                            imageItems.add(i,new ImageItem(image.getString("image_url")));
                        }
                        if (jsonArray.length() != 0) {
                            JSONObject mainImage = jsonArray.getJSONObject(0);
                            String smallImageV = mainImage.getString("image_url");
                            Picasso.with(getApplicationContext())
                                    .load(smallImageV.toString())
                                    .into(smallImage);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Do something on UiThread

                            }
                        });

                        HallActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                imAdapter.notifyDataSetChanged();
                            }
                        });
                        loadLayout.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // onBackPressed();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error in Get Images " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "لم يتم تحميل الصور حاول مرة أخرى", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("hallid", hall_id);




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
