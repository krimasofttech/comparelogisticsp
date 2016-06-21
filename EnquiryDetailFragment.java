package logistic.compare.comparelogistic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import logistic.compare.comparelogistic.Adapter.EnquiryDetailAdapter;
import logistic.compare.comparelogistic.Adapter.EnquiryJsonCreator;
import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Declined.DeclinedFragment;
import logistic.compare.comparelogistic.Live.LiveEnquiryJsonParser;
import logistic.compare.comparelogistic.Live.LiveFragment;
import logistic.compare.comparelogistic.Lost.LostFragment;
import logistic.compare.comparelogistic.Quoted.QuotedFragment;
import logistic.compare.comparelogistic.Won.WonFragment;
import logistic.compare.comparelogistic.gcm.dialog;


public class EnquiryDetailFragment extends Fragment implements EnquiryQuote.CommunicateToEnquiryQuote, CommonInterface, CommonUtility {
    private OnFragmentInteractionListener mListener;
    View rootView;
    RecyclerView recyclerView;
    EnquiryDetailAdapter mAdapter;
    public EnquiryColumns ec;
    LinearLayoutManager layoutmanager;
    LinearLayout bottomHeader;
    TextView send_quote, cancel_enquiry;
    LiveEnquiryJsonParser parser;
    Animation bottom_top, top_bottom;
    dialog d;
    DB db;
    ProgressBar loader;
    ReasonWidget reason_widget;
    public DisplayImageOptions options;
    BottomSheetBehavior bottomSheetBehavior;

    public EnquiryDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pos = getArguments().getInt("pos");
        ec = (EnquiryColumns) getArguments().getSerializable("data");
        Log.d("EnquiryId", ec.getID());
        // EnquiryColumns.setEnquiryColumns(this.ec);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_enquiry_detail, container, false);
        }
        initComponent(rootView);
        setData();
        setScrolling();
        return rootView;
    }

    private void setScrolling() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUR_DEVELOPMENT) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        } else {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        // Log.d("ScrollUp", "true");
                        //toggleView(bottomHeader);
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //showView(bottomHeader);
                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        // hideView(bottomHeader);
                    }
                }
            });
        }
    }

    private void setData() {
        EnquiryJsonCreator ejc = new EnquiryJsonCreator(ec.getExtra());
        try {
            JSONObject data = ejc.RaiseEnquiryJsonObject((ec.getExtra()));
            parser = new LiveEnquiryJsonParser(data);
            data = parser.parse(); // here we parse and manage enquiry
            if (ec != null) {
                mAdapter = new EnquiryDetailAdapter(getActivity(), data);
                mAdapter.setCommonInterface(this);
                recyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        layoutmanager = new LinearLayoutManager(getActivity());
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
        recyclerView.setLayoutManager(layoutmanager);
        //bottomHeader = (LinearLayout) rootView.findViewById(R.id.bottombar);
      /*  bottomSheetBehavior = BottomSheetBehavior.from(bottomHeader);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(50);*/
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottom_top = AnimationUtils.loadAnimation(getActivity(), R.anim.up_from_bottom);
        top_bottom = AnimationUtils.loadAnimation(getActivity(), R.anim.down_from_top);
        //send_quote = (TextView) rootView.findViewById(R.id.send_quote);
        //cancel_enquiry = (TextView) rootView.findViewById(R.id.cancel_enquiry);
        layoutmanager.setSmoothScrollbarEnabled(true);
        db = new DB(getActivity());
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public void hideView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void showView(View view) {
        if (view != null && view.getVisibility() == View.INVISIBLE) {
            view.startAnimation(bottom_top);
            view.setVisibility(View.VISIBLE);
        }
    }

    public void toggleView(View view) {
        if (view != null && view.getVisibility() == View.INVISIBLE) {
            showView(view);
        } else {
            hideView(view);
        }
    }

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

    //open Reason Dialog
    public void openReasonWidget() {
        reason_widget = new ReasonWidget(getActivity(), this.ec, this);
        reason_widget.open();
    }

    public void refreshAdapters(int item) {
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
        this.getActivity().finish();
        if (HomeFragment.pager != null) {
            HomeFragment.pager.setCurrentItem(item);
        }
    }

    public void updateEnquiry(int item) {
        db.open();
        db.updateEnquiry(EnquiryColumns.getEnquiryColumns());
        db.close();
        refreshAdapters(item);
    }

    @Override
    public void sendAck(String msg, String status) {
        try {
            // Log.d("MessageReceived", "Rec" + msg);
            if (msg.contains("quote")) { //while send_quote
                if (d != null) {
                    d.showGreeting();
                    d.updateEnquiry(status);
                } else
                    d.hideButton();
            } else if (msg.equals("close")) { // while close button hit.
                d.getDialog().dismiss();
                updateEnquiry(1);//update to local database // quoted enquiry fragment
            } else if (msg.equals(cancelenquiry)) {//while cancel_enquiry button hit by sp
                reason_widget.loader.setVisibility(View.INVISIBLE);
                hideView(bottomHeader);
                updateEnquiry(4);//update to local database 4==declined  enquiry fragment
            } else {
                if (d != null) {
                    d.showButton();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://comparelogistic.in/public/uploads/" + enquiry.toString()));
        startActivity(i);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
