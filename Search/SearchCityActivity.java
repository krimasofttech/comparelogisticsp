package logistic.compare.comparelogistic.Search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.R;

public class SearchCityActivity extends AppCompatActivity implements CommonInterface {

    RecyclerView recyclerView;
    EditText searchView;
    CityAdapter cityAdapter;
    HandlerThread mHandlerThread;
    Handler mThreadHandler;
    LinearLayoutManager layoutManager;
    ProgressBar loader;
    Spinner spinner;
    TruckAdapter truckAdapter;
    String searchFlag = "city";
    boolean b = false;
    int requestedCode;

    String key = "false";
    String citykey = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initComponent();
        handleIntentCall();//handle call  from SearchActivity
    }

    public void handleIntentCall() {
        if (getIntent() != null) {
            Intent intent = getIntent();

            if (intent.hasExtra(SearchTruckActivity.fromcityText)) {
                citykey = "fromcity";
                key = citykey;
                b = true;
                spinner.setSelection(0);
                requestedCode = SearchTruckActivity.fromcitycode;
            } else if (intent.hasExtra(SearchTruckActivity.tocityText)) {
                citykey = "tocity";
                key = citykey;
                b = true;
                spinner.setSelection(0);
                requestedCode = SearchTruckActivity.tocitycode;
            } else if (intent.hasExtra(SearchTruckActivity.truckText)) {
                key = "truck";
                b = true;
                spinner.setSelection(1);
                requestedCode = SearchTruckActivity.trucktextcode;
            }
        }
    }

    public void initComponent() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.spinner);
        setFilterAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        searchView = (EditText) findViewById(R.id.searchview);
        cityAdapter = new CityAdapter();
        loader = (ProgressBar) findViewById(R.id.loader);
        cityAdapter.setCommonInterface(this);
        layoutManager = new LinearLayoutManager(this);
        initHandler();
        recyclerView.setAdapter(cityAdapter);
        recyclerView.setLayoutManager(layoutManager);
        clickEvent();
        initData();
    }

    public void initHandler() {
        if (mThreadHandler == null) {
            // Initialize and start the HandlerThread
            // which is basically a Thread with a Looper
            // attached (hence a MessageQueue)
            mHandlerThread = new HandlerThread("DataProcessed", android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();

            // Initialize the Handler
            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        changeAdapter();
                    } else if (msg.what == 2) {
                        initData();
                    }
                }
            };
        }
    }

    //init Data according to searchFlag
    public void initData() {
        if (searchFlag.contains("city")) {
            initCityData();//initCityData
        } else if (searchFlag.contains("truck")) {
            initTruckData();//initTruckData
        }
    }

    public void setFilterAdapter() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filters, R.layout.mytextview);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.mytextview);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeSearch(adapter.getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void changeSearch(String data) {
        if (data.contains("City")) {
            searchView.setHint("Search City");
            changeToCityAdapter();
        } else if (data.contains("Truck")) {
            searchView.setHint("Search Truck");
            changeToTruckAdapter();
        }
    }

    public void changeToCityAdapter() {
        searchFlag = "city";
        recyclerView.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
    }

    public void changeToTruckAdapter() {
        searchFlag = "truck";
        truckAdapter = new TruckAdapter();
        ArrayList list = new ArrayList();
        list = getTrucks(list);
        truckAdapter.setCityList(list);
        truckAdapter.setCommonInterface(this);
        recyclerView.setAdapter(truckAdapter);
        truckAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getTrucks(ArrayList<String> list) {
        String[] trucks = getResources().getStringArray(R.array.truckarray);
        for (String s : trucks) {
            list.add(s);
        }
        return list;
    }

    public void changeAdapter() {
        if (searchFlag.contains("city")) {
            refreshCityAdapter(); // notifyCityAdapter.
            key = citykey;
            requestedCode = SearchTruckActivity.fromcitycode;
        } else if (searchFlag.contains("truck")) {
            refreshTruckAdapter();// notifyTruckAdapter.
            key = "truck";
            requestedCode = SearchTruckActivity.trucktextcode;
        }

    }

    public void clickEvent() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String value = s.toString();
                loader.setVisibility(View.VISIBLE);
                try {
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);

                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Background thread
                            startSearch(mThreadHandler, value);

                        }
                    }, 10);
                } catch (Exception e) {
                    e.printStackTrace();
                    loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //call to start search according to filters
    public void startSearch(Handler mThreadHandler, String value) {
        if (new ConnectionUtility(SearchCityActivity.this).isConnectingToInternet()) {
            if (!TextUtils.isEmpty(value)) {
                if (searchFlag.contains("city")) {
                    searchCity(value);
                } else if (searchFlag.contains("truck")) {
                    searchTruck(value);
                }
            } else {
                mThreadHandler.sendEmptyMessage(2);
            }
        }
    }


    //seearch city in cityAdapter
    public void searchCity(String value) {
        cityAdapter.cityList = cityAdapter.placeAPI.autocomplete(value);
        if (cityAdapter.cityList.size() > 0)
            mThreadHandler.sendEmptyMessage(1);
    }

    //search truck in truckAdapter
    public void searchTruck(String value) {
        truckAdapter.cityList = truckAdapter.search(value);
        if (truckAdapter.cityList.size() > 0) {
            mThreadHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    public void initCityData() {
        ArrayList<String> cityList = new ArrayList<>();
        String[] city = getResources().getStringArray(R.array.city);
        for (String s : city) {
            cityList.add(s);
        }
        cityAdapter.setCityList(cityList);
        refreshCityAdapter();
    }

    public void initTruckData() {
        ArrayList<String> truckList = new ArrayList<>();
        truckList = getTrucks(truckList);
        truckAdapter.setCityList(truckList);
        refreshTruckAdapter();
    }

    public void refreshCityAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cityAdapter.notifyDataSetChanged();
                loader.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void refreshTruckAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                truckAdapter.notifyDataSetChanged();
                loader.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        if (searchFlag.equals("city")) {
            String city = cityAdapter.getCityList().get(Integer.parseInt(enquiry.toString()));
            if (b == false) {
                startSearchActivity("city", city);
            } else { // activity called from SearchActivity
                handleCityCall(city);
            }
        } else if (searchFlag.equals("truck")) {
            String truck = truckAdapter.getCityList().get(Integer.parseInt(enquiry.toString()));
            if (b == false) {
                startSearchActivity("truck", truck);
            } else { // activity called from SearchActivity
                handleTruckCall(truck);
            }
        }
    }

    public void handleCityCall(String city) {
        Intent intent = new Intent(this, SearchTruckActivity.class);
        intent.putExtra(key, city);
        setResult(requestedCode, intent);
        Log.d("City", key);
        Log.d("RequestCode", "" + requestedCode);
        this.finish();
    }

    public void handleTruckCall(String truck) {
        Intent intent = new Intent(this, SearchTruckActivity.class);
        intent.putExtra(key, truck);
        setResult(requestedCode, intent);
        this.finish();
    }

    public void startSearchActivity(String key, String value) {
        Intent intent = new Intent(this, SearchTruckActivity.class);
        intent.putExtra(key, value);
        startActivity(intent);
    }
}
