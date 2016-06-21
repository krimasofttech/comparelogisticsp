package logistic.compare.comparelogistic.Lost;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Quoted.QuotedAdapter;
import logistic.compare.comparelogistic.Quoted.QuotedViewPagerAdapter;
import logistic.compare.comparelogistic.R;

public class LostActivity extends AppCompatActivity implements LostFragmentDetail.OnFragmentInteractionListener {
    EnquiryColumns ec;
    ViewPager viewpager;
    static LostViewPagerAdapter mAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            viewpager = (ViewPager) findViewById(R.id.viewPager);
            String pos = getIntent().getStringExtra("pos"); //get position of clicked enquiry in Live Adapter
            Log.d("Position", pos);
            mAdpater = new LostViewPagerAdapter(getSupportFragmentManager());
            startLiveEnquiryPager(pos, LostAdapter.getEnquirylist()); // start if called from quoted enquiry
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startLiveEnquiryPager(String pos, ArrayList<EnquiryColumns> list) {
        mAdpater.setEnquirylist(list);
        viewpager.setAdapter(mAdpater);
        viewpager.setCurrentItem(Integer.parseInt(pos), true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
