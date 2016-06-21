package logistic.compare.comparelogistic.Load;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.CityAdapter;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.MainActivity;
import logistic.compare.comparelogistic.R;

public class LoadActivity extends AppCompatActivity implements CommonInterface, CommonUtility {
    RecyclerView cityView;
    public ArrayList<String> cityList;
    LoadAdapter myAdapter;
    LinearLayoutManager layoutmanager;
    HandlerThread mHandlerThread;
    Handler mThreadHandler;
    EditText searchCityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cityView = (RecyclerView) findViewById(R.id.citylist);
        layoutmanager = new LinearLayoutManager(this);
        searchCityText = (EditText) findViewById(R.id.searchCityText);
        cityView.setLayoutManager(layoutmanager);
        layoutmanager.setSmoothScrollbarEnabled(true);
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        cityList = new ArrayList<>();
        String[] city = getResources().getStringArray(R.array.loadcategory);
        for (String s : city) {
            cityList.add(s);
        }
        myAdapter = new LoadAdapter(this, cityList);
        myAdapter.setCommonInterface(this);
        cityView.setAdapter(myAdapter);
    }


    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        String load = enquiry.toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("load", load);
        setResult(1, intent);
        finish();
    }
}
