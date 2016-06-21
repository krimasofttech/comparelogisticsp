package logistic.compare.comparelogistic.Won;

import android.content.pm.PackageManager;
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

public class WonActivity extends AppCompatActivity implements WonFragmentDetail.OnFragmentInteractionListener {
    EnquiryColumns ec;
    ViewPager viewpager;
    static WonViewPagerAdapter mAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.won);
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
            mAdpater = new WonViewPagerAdapter(getSupportFragmentManager());
            startLiveEnquiryPager(pos, WonAdapter.getEnquirylist()); // start if called from quoted enquiry
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("ActivityPermission", "true");
        mAdpater.getItem(viewpager.getCurrentItem()).onRequestPermissionsResult(requestCode, permissions, grantResults);
       /* switch (requestCode) {
            case 1: {
                if (grantResults.length > 0   //permission is granted
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionGranted", "true");
                } else {
                    //permission is denied
                }
                break;

            }
        }*/

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
