package logistic.compare.comparelogistic;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import logistic.compare.comparelogistic.Adapter.*;
import logistic.compare.comparelogistic.Adapter.ViewPagerAdapter;
import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Declined.DeclinedFragment;
import logistic.compare.comparelogistic.Live.LiveAdapter;
import logistic.compare.comparelogistic.Live.LiveFragment;
import logistic.compare.comparelogistic.Lost.LostFragment;
import logistic.compare.comparelogistic.Quoted.QuotedFragment;
import logistic.compare.comparelogistic.Won.WonFragment;
import logistic.compare.comparelogistic.gcm.dialog;

public class EnquiryQuoteActivity extends AppCompatActivity implements EnquiryQuote.CommunicateToEnquiryQuote
        , EnquiryDetailFragment.OnFragmentInteractionListener, CommonUtility {
    EnquiryColumns ec;
    ViewPager viewpager;
    static logistic.compare.comparelogistic.Adapter.ViewPagerAdapter mAdpater;
    Toolbar toolbar;
    ImageView backDrop, send_quote, cancle_enquiry;
    public DisplayImageOptions options;
    public BottomSheetBehavior bottomSheetBehavior;
    View bottomsheet;
    dialog d;
    DB db;
    ReasonWidget reason_widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_quote);
        backDrop = (ImageView) findViewById(R.id.backimage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });
        db = new DB(this);
        send_quote = (ImageView) findViewById(R.id.send_quote);
        cancle_enquiry = (ImageView) findViewById(R.id.cancel_enquiry);
        clickEvent();
        toolbar.setLogo(android.R.drawable.btn_star_big_on);
        try {
            viewpager = (ViewPager) findViewById(R.id.viewPager);
            String pos = getIntent().getStringExtra("pos"); //get position of clicked enquiry in Live Adapter
            mAdpater = new ViewPagerAdapter(getSupportFragmentManager());
            startLiveEnquiryPager(pos, LiveAdapter.getEnquirylist()); // start if called from live enquiry
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickEvent() {
        send_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuote();
            }
        });
        cancle_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelQuote();
            }
        });
    }

    public void sendQuote() {
        if (d == null) {
            d = new dialog(this, this, mAdpater.getEnquirylist().get(viewpager.getCurrentItem()));
        }
        Dialog dialog = d.getDialog();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void cancelQuote() {
        reason_widget = new ReasonWidget(this, EnquiryColumns.getEnquiryColumns(), this);
        reason_widget.open();
    }

    public void startLiveEnquiryPager(String pos, ArrayList<EnquiryColumns> list) {
        Log.d("PositionAct", pos);
        mAdpater.setEnquirylist(list);
        viewpager.setAdapter(mAdpater);
        viewpager.setCurrentItem(Integer.parseInt(pos), true);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewpager.getCurrentItem();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void sendAck(String msg, String status) {
        //Log.d("msg", msg);
        try {
            //  Log.d("MessageReceived", "Rec" + msg);
            if (msg.contains("quote")) { //while send_quote
                if (d != null) {
                    d.showGreeting();
                    d.updateEnquiry(status);
                } else
                    d.hideButton();
            } else if (msg.equals(cancelenquiry)) {//while cancel_enquiry button hit by sp
                reason_widget.loader.setVisibility(View.INVISIBLE);
                reason_widget.d.dismiss();
                updateEnquiry(viewpager.getCurrentItem() + 1);//update to local database 4==declined  enquiry fragment
            } else if (msg.equals("close")) { // while close button hit.
                d.getDialog().dismiss();
                updateEnquiry(viewpager.getCurrentItem() + 1);//update to local database // quoted enquiry fragment
            } else {
                if (d != null) {
                    d.showButton();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateEnquiry(int item) {
        Log.d("UpdateEnquiry", "true");
        if (db != null) {
            db.open();
            db.updateEnquiry(EnquiryColumns.getEnquiryColumns());
            db.close();
            refreshAdapters(item);
        }
    }

    public void refreshAdapters(int item) {
        Log.d("RefreshAdapter", "true");
        //pass null reference to update adapter itself by fetching from database.
        if (null != LiveFragment.mAdapter)
            LiveFragment.mAdapter.updateAdapter(null);
        if (null != LostFragment.mAdapter)
            LostFragment.mAdapter.updateAdapter(null);
        if (null != QuotedFragment.mAdapter)
            QuotedFragment.mAdapter.updateAdapter(null);
        if (null != DeclinedFragment.mAdapter)
            DeclinedFragment.mAdapter.updateAdapter(null);
        if (WonFragment.mAdapter != null)
            WonFragment.mAdapter.updateAdapter(null);
        if (HomeFragment.pager != null) {
            //    HomeFragment.pager.setCurrentItem(item);
        }
        if (mAdpater.updateAdapter(LiveFragment.mAdapter.getEnquiryList())) {
            viewpager.setCurrentItem(item);
        }
    }

}
