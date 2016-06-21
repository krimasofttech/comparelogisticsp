package logistic.compare.comparelogistic.Load;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.MyViewTruckAdapter;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

public class ViewLoadActivity extends AppCompatActivity implements ViewloadFragment.OnLoadFragmentListener {
    ViewPager myPager;
    ViewLoadPageAdapter mAdapter;
    String current = "0";
    ArrayList<PostLoadBean> trucklist;
    public BottomSheetBehavior bottomSheetBehavior;
    View bottomsheet;
    TextView customer, contact, nocredits, customertext, contacttext, email, emailtext;
    android.support.v7.widget.Toolbar toolbar;
    ImageView close, call;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_load2);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Find Load");
        setSupportActionBar(toolbar);
        trucklist = (ArrayList<PostLoadBean>) getIntent().getSerializableExtra("loadlist");
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });
        current = getIntent().getStringExtra("current");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        myPager = (ViewPager) findViewById(R.id.viewpager);
        myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (bottomsheet != null && bottomSheetBehavior != null)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter = new ViewLoadPageAdapter(getSupportFragmentManager());
        mAdapter.setTrucklist(trucklist);
        myPager.setAdapter(mAdapter);
        myPager.setCurrentItem(Integer.parseInt(current));
        bottomsheet = findViewById(R.id.customerbottomsheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //   bottomSheetBehavior.setPeekHeight(30);
        bottomSheetBehavior.setHideable(true);
        customer = (TextView) findViewById(R.id.customername);
        customertext = (TextView) findViewById(R.id.customertext);
        contact = (TextView) findViewById(R.id.contactno);
        contacttext = (TextView) findViewById(R.id.contacttext);
        email = (TextView) findViewById(R.id.email);
        emailtext = (TextView) findViewById(R.id.emailtext);
        close = (ImageView) findViewById(R.id.close);
        call = (ImageView) findViewById(R.id.call);
        nocredits = (TextView) findViewById(R.id.nocredit);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
        Event();
    }

    public void Event() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.onLayoutChild(coordinatorLayout,
                            bottomSheet,
                            ViewCompat.LAYOUT_DIRECTION_LTR);
                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
    }

    public void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getText().toString().trim()));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(intent);
    }


    @Override
    public void onLoadFragmentInterction(String uri) { // data from viewLoadFragment Received
        Log.d("ActivityReceived", uri);
        if (uri.contains(":") && !uri.contains("notviewed")) {
            String[] customerarray = uri.split(":"); // 1=customer,2=contact,3=email
            changeVisisbility(View.VISIBLE);
            customer.setText(customerarray[0]);
            contact.setText(customerarray[1]);
            email.setText(customerarray[2]);
            nocredits.setVisibility(View.INVISIBLE);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else if (uri.contains("nocredit")) {
            changeVisisbility(View.INVISIBLE);
            nocredits.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No Credits in Account,Please contact comparelogistic.in", Toast.LENGTH_SHORT).show();
        } else if (uri.contains("notransaction")) {
            changeVisisbility(View.INVISIBLE);
            nocredits.setVisibility(View.VISIBLE);
            nocredits.setText("No Transaction Performed,try after some time");
            Toast.makeText(this, "No Credits in Account,Please contact comparelogistic.in", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeVisisbility(int status) {
        customer.setVisibility(status);
        customertext.setVisibility(status);
        contact.setVisibility(status);
        contacttext.setVisibility(status);
        email.setVisibility(status);
        emailtext.setVisibility(status);
        call.setVisibility(status);
    }
}
