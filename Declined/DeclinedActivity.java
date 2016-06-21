package logistic.compare.comparelogistic.Declined;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Lost.LostAdapter;
import logistic.compare.comparelogistic.Lost.LostViewPagerAdapter;
import logistic.compare.comparelogistic.R;

public class DeclinedActivity extends AppCompatActivity implements DeclinedFragmentDetail.OnFragmentInteractionListener {
    EnquiryColumns ec;
    ViewPager viewpager;
    static DeclinedViewPagerAdapter mAdpater;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("BackPressed", "true");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "homebuttonpressed", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declined);
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
        try {
            viewpager = (ViewPager) findViewById(R.id.viewPager);
            String pos = getIntent().getStringExtra("pos"); //get position of clicked enquiry in Live Adapter
            Log.d("Position", pos);
            mAdpater = new DeclinedViewPagerAdapter(getSupportFragmentManager());
            startLiveEnquiryPager(pos, DeclinedAdapter.getEnquirylist()); // start if called from quoted enquiry
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
