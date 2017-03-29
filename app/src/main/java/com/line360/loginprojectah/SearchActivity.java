package com.line360.loginprojectah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView mSearchView;
    private ListView mListView;
    private final ArrayList<String> citys = new ArrayList<String>();


    private final String[] mStrings = {"أسيوط","سوهاج","المنيا","أسيوط","سوهاج","أسيوط","سوهاج","أسيوط","سوهاج","أسيوط","سوهاج","أسيوط","سوهاج","أسيوط","سوهاج",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_search);
        citys.add(0,"اسيوط");
        citys.add(1,"المنيا");
        citys.add(2,"سوهاج");
        citys.add(3,"اسيوط");
        citys.add(4,"المنيا");
        citys.add(5,"سوهاج");
        citys.add(6,"اسيوط");
        citys.add(7,"المنيا");
        citys.add(8,"سوهاج");
        citys.add(9,"اسيوط");
        citys.add(10,"المنيا");
        citys.add(11,"سوهاج");
        citys.add(12,"اسيوط");
        citys.add(13,"المنيا");
        citys.add(14,"سوهاج");
        citys.add(15,"اسيوط");
        citys.add(16,"المنيا");
        citys.add(17,"سوهاج");
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setVisibility(View.GONE);

        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                citys));
        mListView.setTextFilterEnabled(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String city = citys.get(position);
                final String city_key = "citykey";
                String cityIntent = city;
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                myIntent.putExtra(city_key,cityIntent);
                startActivityForResult(myIntent, 0);
                finish();

            }
        });
        setupSearchView();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("البحث بإسم المحافظة");
    }

    public boolean onQueryTextChange(String newText) {
        mListView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}
