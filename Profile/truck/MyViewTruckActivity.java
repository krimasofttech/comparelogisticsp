package logistic.compare.comparelogistic.Profile.truck;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Profile.truck.MyTruckAdapter;
import logistic.compare.comparelogistic.Profile.truck.MyViewTruckFragment;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

public class MyViewTruckActivity extends AppCompatActivity implements MyViewTruckFragment.MyViewTruckFragmentListener {

    ViewPager myPager;
    MyTruckAdapter mAdapter;
    String current = "0";
    ArrayList<PostTruckBean> trucklist;

    public BottomSheetBehavior bottomSheetBehavior;
    View bottomsheet;
    TextView customer, contact, nocredits, customertext, contacttext, email, emailtext;
    android.support.v7.widget.Toolbar toolbar;
    ImageView close, call;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view_truck);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Find Truck");
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });
        trucklist = (ArrayList<PostTruckBean>) getIntent().getSerializableExtra("trucklist");
        current = getIntent().getStringExtra("current");
        myPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new MyTruckAdapter(getSupportFragmentManager());
        mAdapter.setTrucklist(trucklist);
        myPager.setAdapter(mAdapter);
        myPager.setCurrentItem(Integer.parseInt(current));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
