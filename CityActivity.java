package logistic.compare.comparelogistic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.CityAdapter;

public class CityActivity extends AppCompatActivity implements CommonUtility, CommonInterface {
    RecyclerView cityView;
    public ArrayList<String> cityList;
    CityAdapter myAdapter;
    LinearLayoutManager layoutmanager;
    HandlerThread mHandlerThread;
    Handler mThreadHandler;
    EditText searchCityText;
    public ArrayList<String> statelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cityView = (RecyclerView) findViewById(R.id.citylist);
        layoutmanager = new LinearLayoutManager(this);
        initHandler();
        searchCityText = (EditText) findViewById(R.id.searchCityText);
        clickEvent();
        cityView.setLayoutManager(layoutmanager);
        layoutmanager.setSmoothScrollbarEnabled(true);
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        cityList = new ArrayList<>();
        statelist = new ArrayList<>();
        String[] city = getResources().getStringArray(R.array.city);
        String[] state = getResources().getStringArray(R.array.state);
        for (String s : city) {
            cityList.add(s);
        }
        for (String s : state) {
            statelist.add(s);
        }
        myAdapter = new CityAdapter(cityList, this);
        myAdapter.setCommonInterface(this);
        myAdapter.setState(statelist);
        cityView.setAdapter(myAdapter);
    }

    public void clickEvent() {
        searchCityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String value = s.toString();
                try {
                    // Remove all callbacks and messages
                    mThreadHandler.removeCallbacksAndMessages(null);

                    // Now add a new one
                    mThreadHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Background thread
                            if (new ConnectionUtility(CityActivity.this).isConnectingToInternet()) {
                                myAdapter.state.clear();
                                myAdapter.city = myAdapter.mPlaceAPI.autocomplete(value);
                                if (myAdapter.city.size() > 0)
                                    mThreadHandler.sendEmptyMessage(1);
                            }
                        }
                    }, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                        ArrayList<String> results = myAdapter.city;
                        if (results != null && results.size() > 0) {
                            changeAdapter("datsetchanged");
                            // mAdapter.notifyDataSetChanged();
                        } else {
                            changeAdapter("datasetinvalidated");
                            //   mAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            };
        }
    }

    public void changeAdapter(String status) {
        if (status.equals("datsetchanged")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAdapter.notifyDataSetChanged();
                }
            });
        } else if (status.equals("datasetinvalidated")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        try {
            String state = "false";
            String city = "false";
            // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            String cityText = myAdapter.city.get(Integer.parseInt(enquiry.toString()));
            city = cityText.split(",")[0];
            try {
                if (!TextUtils.isEmpty(cityText.split(",")[1])) {
                    state = cityText.split(",")[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = "false";
            }
            if (state.equals("false")) { // if by default city has been selected then fetch its state from state list
                state = myAdapter.state.get(Integer.parseInt(enquiry.toString()));
            } else if (!getState(city).equals("false")) {//if city is centralized then put city state as it is.
                state = getState(city);
            }

            Intent i = new Intent(this, getCallingActivity().getClass());
            i.putExtra("city", city);
            i.putExtra("state", state);
            setResult(2, i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //here we check that state we received is in centrilized state or not.true== add as it is false=add from city string
    public String getState(String city) {
        String[] stateList = getResources().getStringArray(R.array.centralindia);
        String state = "false";
        for (String s : stateList) {
            if (s.trim().equals(city.trim())) {
                state = s;
                break;
            }
        }
        return state;
    }
}
