package com.line360.loginprojectah;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.line360.loginprojectah.helper.Hall;
import com.line360.loginprojectah.helper.ImageItem;
import com.line360.loginprojectah.helper.MhallsSize;
import com.line360.loginprojectah.helper.RecyclerView_Adapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HallActivity extends Activity {
    private static String TAG;
    private static RecyclerView recyclerView;
    public static final String[] IMAGES = {"http://i.imgur.com/beABPPk.jpg","http://i.imgur.com/ShDTxcW.jpg","http://i.imgur.com/mK0x1jn.jpg","http://i.imgur.com/ITT17YQ.jpg","http://i.imgur.com/PqeyuxT.jpg","http://i.imgur.com/Gk754wS.jpg"};
    ImageView smallImage;
    public static TextView nameHall,priceHall,addressHall, hallSee;
    public static ExpandableTextView preparingTx;
    AsyncHallTask asyncHallTask;
    final String idKey = "idKey";
    LinearLayout loadLayout;
    BottomNavigationView bottomNavigationView;
    ProgressBar mprogressBar;
    String url1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        loadLayout = (LinearLayout) findViewById(R.id.load_layout_hall);
        loadLayout.setVisibility(View.VISIBLE);
        mprogressBar = (ProgressBar) findViewById(R.id.progBar);
        if (mprogressBar != null) {
            mprogressBar.setIndeterminate(true);
            mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        smallImage = (ImageView) findViewById(R.id.small_image);
        nameHall = (TextView) findViewById(R.id.tx_name_hall);
        hallSee = (TextView) findViewById(R.id.tx_see);
        priceHall= (TextView) findViewById(R.id.tx_price_hall);
        addressHall = (TextView) findViewById(R.id.tx_address_hall);


        initViews();
        populatRecyclerView();
        // sample code snippet to set the text content on the ExpandableTextView


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
                                Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phone));

                                boolean result = isPhoneCallPermissionGranted();

                                try {
                                    startActivity(callintent);
                                    Toast toast = Toast.makeText(getApplication(),
                                            "Calling",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast toast = Toast.makeText(getApplication(),
                                            "Call Failed",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;

                            case R.id.action_facebook:
                                final String facebook_key = "fkey";
                                Intent fintent = getIntent();
                                String username = fintent.getStringExtra(facebook_key);
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + username)));
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

        recyclerView = (RecyclerView)
                findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView
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
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter

    }


    // 1 async -----------------------------------------------------------------------------------------------------

    public class AsyncHallTask extends AsyncTask<String, String, List<Hall>> {
        Intent imgintent = getIntent();
        final String idIntent = imgintent.getStringExtra(idKey);

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
                    urlConnection.addRequestProperty("Authorization", "bdbdfdd9ebee6849eabb835f825eeaab");

                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    List<NameValuePair> parames = new ArrayList<NameValuePair>();
                    parames.add(new BasicNameValuePair("id", idIntent));


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
                    String name = hall.getString("name");
                    nameHall.setText(name.toString());
                    String price = hall.getString("price");
                    priceHall.setText(price.toString());
                    String phone = hall.getString("phone");
                    String facebook = hall.getString("facebook");
                    String instagram = hall.getString("instagram");
                    String twitter = hall.getString("twitter");
                    String preparing = hall.getString("preparing");
                    preparingTx.setText(preparing.toString());
                    String image1 = hall.getString("image1");
                    Picasso.with(getApplicationContext())
                            .load(image1.toString())
                            .into(smallImage);
                    String image2 = hall.getString("image2");
                    String image3 = hall.getString("image3");
                    String address = hall.getString("address");
                    addressHall.setText(address.toString());
                    String see = hall.getString("see");
                    hallSee.setText(see.toString());
                }

                loadLayout.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);


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
}
