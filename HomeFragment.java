package logistic.compare.comparelogistic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import logistic.compare.comparelogistic.Declined.DeclinedFragment;
import logistic.compare.comparelogistic.Live.LiveFragment;
import logistic.compare.comparelogistic.Lost.LostFragment;
import logistic.compare.comparelogistic.Quoted.QuotedFragment;
import logistic.compare.comparelogistic.Service.RefreshEnquiryIntentService;
import logistic.compare.comparelogistic.Won.WonFragment;

public class HomeFragment extends android.support.v4.app.Fragment implements CommonInterface, CommonUtility {

    private OnFragmentInteractionListener mListener;

    PagerSlidingTabStrip tabs;
    static ViewPager pager;
    MyPageAdapter adapter;
    Context context;
    OnpageChangeListener onpageChangeListener;
    RefreshEnquiryIntentService refreshenquiryservice;
    ProgressBar loader;
    LinearLayout coordinatorLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        serviceBinder();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        onpageChangeListener = new OnpageChangeListener();
        adapter = Model.getInstance().getAdapter();
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        coordinatorLayout = (LinearLayout) view.findViewById(R.id.coordinatorLayout);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        //pager.setCurrentItem(1);
        tabs.setOnPageChangeListener(onpageChangeListener);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);

        tabs.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                //  Toast.makeText(getActivity(), "Tab reselected: " + position, Toast.LENGTH_SHORT).show();
                String enquiry = MyPageAdapter.TITLES[position];
            }
        });
        return view;
    }

    public void serviceBinder() {
        Intent intent = new Intent(getActivity(), RefreshEnquiryIntentService.class);
        boolean b = getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void updateEnquiry() {
        Log.d("UpdateEnquiry", "true");
        showView(loader);
        refreshenquiryservice.setCommonInterface(this);
        refreshenquiryservice.RefreshEnquiry(livestatus, 0);
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnectedHome", "true");
            RefreshEnquiryIntentService.MyBinder binder = (RefreshEnquiryIntentService.MyBinder) service;
            refreshenquiryservice = binder.getService();
            updateEnquiry();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceDisconnected", "true");
        }
    };

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    public void showError(String messsage, int duration) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, messsage, duration);
        snackbar.show();
        hideView(loader);
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("ReceivedMessage", enquiry.toString());
        hideView(loader);
        if (enquiry.toString().split(":")[0].equals(ENQUIRYCOUNT)) {
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
            if (Integer.parseInt(enquiry.toString().split(":")[1]) > 0) {
                showError("Enquiry Refresh", Snackbar.LENGTH_SHORT);
            } else {
                showError("No New Enquiry", Snackbar.LENGTH_SHORT);
            }
        } else {
            showError(enquiry.toString().split(":")[1], Snackbar.LENGTH_LONG);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
