package logistic.compare.comparelogistic.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Load.FindLoadAdapter;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.Load.ViewLoadActivity;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import logistic.compare.comparelogistic.Truck.TruckListAdapter;
import logistic.compare.comparelogistic.Truck.ViewTruckActivity;

public class SearchLoadActivity extends AppCompatActivity implements CommonInterface, View.OnClickListener, FilterLoadFragment.FilterLoadFragmentInterface {

    Toolbar toolbar;
    TextView fromCity, toCity, trucktext;
    RecyclerView recycleView;
    DB db;
    TextView searchBox;
    ArrayList<PostLoadBean> truckList = new ArrayList<>();
    String fromcity, tocity, vehicle;
    FindLoadAdapter mAdapter;
    LinearLayoutManager layoutManager;
    ProgressBar loader;
    public static String fromcityText = "fromcity";
    public static String tocityText = "tocity";
    public static String truckText = "truck";
    ImageView cancel;
    TextView emptyView, swapSearch, filter;
    public static int fromcitycode = 1, tocitycode = 1, trucktextcode = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_load);
        initComponent();
    }

    //initialize the component
    public void initComponent() {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            recycleView = (RecyclerView) findViewById(R.id.recycleView);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            searchBox = (TextView) findViewById(R.id.searchBox);
            cancel = (ImageView) findViewById(R.id.cancel);
            emptyView = (TextView) findViewById(R.id.emptyView);
            fromCity = (TextView) findViewById(R.id.fromCity);
            swapSearch = (TextView) findViewById(R.id.swap);
            toCity = (TextView) findViewById(R.id.toCity);
            trucktext = (TextView) findViewById(R.id.trucktext);
            trucktext.setText("Search Load");
            loader = (ProgressBar) findViewById(R.id.loader);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mAdapter = new FindLoadAdapter(this, truckList, this);
            recycleView.setAdapter(mAdapter);
            filter = (TextView) findViewById(R.id.filter);
            fromCity.setOnClickListener(this);
            toCity.setOnClickListener(this);
            trucktext.setOnClickListener(this);
            swapSearch.setOnClickListener(this);
            recycleView.setLayoutManager(layoutManager);
            cancel.setOnClickListener(this);
            filter.setOnClickListener(this);
            layoutManager.setSmoothScrollbarEnabled(true);
            db = new DB(this);
            if (getIntent().hasExtra("city")) {
                loader.setVisibility(View.VISIBLE);
                fromcity = getIntent().getStringExtra("city");
                setFromCity(fromcity);
            }
            if (getIntent().hasExtra("truck")) {
                loader.setVisibility(View.VISIBLE);
                vehicle = getIntent().getStringExtra("truck");
                Log.d("truckReceived", vehicle);
                setTruck(vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loader.setVisibility(View.INVISIBLE);
        }
    }

    public void setFromCity(String city) {
        city = city.split(",")[0].toString();
        fromcity = city;
        fromCity.setText(fromcity);
        getPostTruckList(fromcity, vehicle, tocity);
    }

    public void setTruck(String vehicle) {
        this.vehicle = vehicle;
        trucktext.setText(vehicle);
        getPostTruckList(fromcity, this.vehicle, tocity);
    }

    public void setToCity(String city) {
        city = city.split(",")[0].toString();
        tocity = city;
        toCity.setText(tocity);
        getPostTruckList(fromcity, vehicle, tocity);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fromCity:
                startSearchActivity(fromcityText, "true", fromcitycode);
                break;
            case R.id.toCity:
                startSearchActivity(tocityText, "true", fromcitycode);
                break;
            case R.id.trucktext:
                startSearchActivity(truckText, "true", trucktextcode);
                break;
            case R.id.cancel:
                cancelTruckSearch();
                break;
            case R.id.swap:
                swapSearch();
                break;
            case R.id.filter:
                openFilterFragment();
                break;
        }
    }

    public void openFilterFragment() {
        Bundle b = new Bundle();
        if (fromcity != null) {
            b.putString("pick", fromcity);
        }
        if (tocity != null) {
            b.putString("dest", tocity);
        }
        if (vehicle != null) {
            b.putString("truck", vehicle);
        }
        FilterLoadFragment filterFragment = new FilterLoadFragment();
        filterFragment.setArguments(b);
        filterFragment.show(getSupportFragmentManager().beginTransaction(), "filter");
    }

    public void swapSearch() {
        String current_from = fromcity;
        String current_to = tocity;
        if (!TextUtils.isEmpty(current_from) && !TextUtils.isEmpty(current_to)) {
            fromcity = current_to;//assign tocity -> fromcity
            tocity = current_from;//asssign fromcity -> tocity
            //   if (animateView()) {
            getPostTruckList(fromcity, vehicle, tocity);
            fromCity.setText(fromcity);
            toCity.setText(tocity);
            // }
        }
    }
/*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean animateView() {
        float x1 = fromCity.getX(); // fromCitycurrent X Pos
        float x2 = toCity.getX(); //tocityCurrent X Pos
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            if (x2 > x1) {
                //swap x2->x1 and x1->x2
                fromCity.animate().setDuration(10).x(x2).start();
                //  toCity.animate().setDuration(10).x(x1).start();
            }
        }
        return true;
    }*/

    public void cancelTruckSearch() {
        trucktext.setText("Search Load");
        vehicle = null;
        getPostTruckList(fromcity, vehicle, tocity);
    }

    public void startSearchActivity(String key, String value, int requestCode) {
        Intent intent = new Intent(this, SearchLoadCityActivity.class);
        intent.putExtra(key, value);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) { // if user clicked on something
            if (requestCode == SearchTruckActivity.fromcitycode) {
                if (data.hasExtra(SearchTruckActivity.fromcityText)) {
                    setFromCity(data.getStringExtra(SearchTruckActivity.fromcityText));
                } else if (data.hasExtra(SearchTruckActivity.tocityText)) {
                    setToCity(data.getStringExtra(SearchTruckActivity.tocityText));
                }
            } else if (requestCode == SearchTruckActivity.trucktextcode) {
                if (data.hasExtra(SearchTruckActivity.truckText)) {
                    setTruck(data.getStringExtra(SearchTruckActivity.truckText));
                }
            }
        }
    }


    public void getPostTruckList(String pickCity, String vehicle, String dest) {
        loader.setVisibility(View.INVISIBLE);
        try {
            db.open();
            truckList = db.getLoadListByVehicle(vehicle, pickCity, dest);
            db.close();
            mAdapter.updateLoadList(truckList);
            if (truckList.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("No Load Available");
            } else {
                emptyView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loader.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        String pos = enquiry.toString().split(":")[1];
        Intent intent = new Intent(this, ViewLoadActivity.class);
        intent.putExtra("current", pos);
        intent.putExtra("loadlist", mAdapter.getTrucklist());
        startActivity(intent);
    }

    @Override
    public void ReceiveFilterData(Object data) {
        getPostTruckList(fromcity, vehicle, tocity);
    }
}
