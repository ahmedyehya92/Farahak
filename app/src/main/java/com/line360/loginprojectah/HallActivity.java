package com.line360.loginprojectah;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.line360.loginprojectah.helper.ImageItem;
import com.line360.loginprojectah.helper.RecyclerView_Adapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class HallActivity extends Activity {
    private static String TAG;
    private static RecyclerView recyclerView;
    public static final String[] IMAGES = {"http://i.imgur.com/beABPPk.jpg","http://i.imgur.com/ShDTxcW.jpg","http://i.imgur.com/mK0x1jn.jpg","http://i.imgur.com/ITT17YQ.jpg","http://i.imgur.com/PqeyuxT.jpg","http://i.imgur.com/Gk754wS.jpg"};
    ImageView smallImage;
    TextView nameHall,priceHall,addressHall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        smallImage = (ImageView) findViewById(R.id.small_image);
        nameHall = (TextView) findViewById(R.id.tx_name_hall);
        priceHall= (TextView) findViewById(R.id.tx_price_hall);
        addressHall = (TextView) findViewById(R.id.tx_address_hall);


        initViews();
        populatRecyclerView();
        // sample code snippet to set the text content on the ExpandableTextView


        final String smallImage_key = "image1key";
        Intent imgintent = getIntent();
        String smallImageInt = imgintent.getStringExtra(smallImage_key);
        Picasso.with(getApplicationContext())
                .load(smallImageInt.toString())
                .into(smallImage);

        final String name_key = "namekey";
        Intent nameintent = getIntent();
        String name = nameintent.getStringExtra(name_key);

        final String price_key = "pricekey";
        Intent priceintent = getIntent();
        String price = priceintent.getStringExtra(price_key);

        final String address_key = "addresskey";
        Intent addressintent = getIntent();
        String address = addressintent.getStringExtra(address_key);

        nameHall.setText(name.toString());
        priceHall.setText(price.toString());
        addressHall.setText(address.toString());

        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        final String preparing_key = "preparingkey";
        Intent preintent = getIntent();
        String preparing = preintent.getStringExtra(preparing_key);

// description text
        expTv1.setText(preparing.toString());




        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);



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
}
