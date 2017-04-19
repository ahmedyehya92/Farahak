package com.line360.loginprojectah.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.line360.loginprojectah.R;
import com.line360.loginprojectah.app.AppConfig;
import com.line360.loginprojectah.app.AppController;
import com.line360.loginprojectah.helper.SQLiteHandler;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = ReviewActivity.class.getSimpleName();
    private RatingBar ratingBar;
    private Button btnSubmit;
    private static EditText etReview;
    private static String ratingValue;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private static String rating = "empty";
    final String idKeyCome = "idHallkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        etReview = (EditText) findViewById(R.id.et_review);
        btnSubmit = (Button) findViewById(R.id.submit_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // rating bar listener
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

               ratingValue = String.valueOf(rating);

            }
        });


        // Fetching user id from sqlite
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        final String user_id = user.get("user_id");
        final String api_key = user.get("uid");


        //get id for hall by intent
        Intent idIntent = getIntent();
        final String hall_id = idIntent.getStringExtra(idKeyCome);



        // button listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = etReview.getText().toString();

                if (ratingValue != null){
                rating = ratingValue.toString();
                }

                if (etReview.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "الرجاء كتابة تعليق في المكان المخصص", Toast.LENGTH_LONG)
                            .show();
                }
                else if (rating == "empty"|| rating =="0") {
                    Toast.makeText(getApplicationContext(),
                            "الرجاء التقييم من خلال الضغط والسحب على شريط التقييم بالأعلى", Toast.LENGTH_LONG)
                            .show();
                }

                else {
                         submitReview(api_key, user_id, hall_id, rating, review );
                }
            }
        });
    }

    private void submitReview(final String api_key, final String user_id, final String hall_id, final String rating , final String review) {

        // Tag used to cancel the request
        String tag_string_req = "review_submit";

        pDialog.setMessage("جاري رفع تقييمك ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REVIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),
                        "تم رفع التقييم", Toast.LENGTH_LONG).show();
                hideDialog();

                try {
                    final String id_Key = "idKey";
                    Intent myIntent = new Intent(ReviewActivity.this, HallActivity.class);
                    myIntent.putExtra(id_Key,hall_id);
                    startActivity(myIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // onBackPressed();
            }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "خطأ في الإتصال حاول مرة أخرى", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", user_id);
                params.put("hallid", hall_id);
                params.put("rating", rating);
                params.put("review", review);



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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
