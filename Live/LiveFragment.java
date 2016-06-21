package logistic.compare.comparelogistic.Live;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.R;

public class LiveFragment extends Fragment implements CommonInterface, CommonUtility {

    private OnLiveFragmentListener mListener;
    View rootView;
    ProgressBar loader;
    RecyclerView listView;
    public static LiveAdapter mAdapter;
    ArrayList<EnquiryColumns> enquiryList;
    LiveIntentService liveIntentService;
    DB db;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutmanager;
    TextView emptyText;
    public CommonInterface commonInterface;

    public LiveFragment() {
        // Required empty public constructor
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnected", "true");
            LiveIntentService.MyBinder binder = (LiveIntentService.MyBinder) service;
            liveIntentService = binder.getService();//after successfull connection with Intent Service
            getLiveEnquiryFromServer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceDisconnected", "true");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live, container, false);
        }
        initComponent(rootView);
        swipeListener();
        hideView(loader);
        hideView(emptyText);
        setAdapter();
        return rootView;
    }

    public void setAdapter() {
        Log.d("Adapter", "true");
        enquiryList = getEnquiryList();
        mAdapter = new LiveAdapter(getActivity(), enquiryList);
        listView.setAdapter(mAdapter);
        BindService();
        Log.d("AdapterSet", "true");
        if (enquiryList.isEmpty()) {
            Log.d("listEmpty", "true");
            showView(emptyText);
        } else {
            hideView(emptyText);
        }
    }

    public void BindService() {
        Log.d("ServiceBind", "true");
        if (mServiceConnection != null) {
            Log.d("ServiceConnection", "notnull");
            Intent intent = new Intent(getActivity(), LiveIntentService.class);
            boolean b = getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d("Connected", "" + b);
        }
    }

    //here we get max id from database call IntentService to execute for fetch Live Enquiry Data
    public void getLiveEnquiryFromServer() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        db.open();
        int maxid = db.getEnquiryMaxId("1");
        db.close();
        Log.d("Maxid", "" + maxid);
        liveIntentService.setCommonInterface(this);//here we set commonInterface as this  class in IntentService
        liveIntentService.RefreshEnquiry("1", maxid);//
    }


    public ArrayList<EnquiryColumns> getEnquiryList() {
        ArrayList<EnquiryColumns> list = new ArrayList<>();
        db.open();
        list = db.getEnquiry(livestatus);
        db.close();
        return list;
    }

    //initialization of view
    public void initComponent(View v) {
        db = new DB(getActivity());
        layoutmanager = new LinearLayoutManager(getActivity());
        loader = (ProgressBar) v.findViewById(R.id.loader);
        listView = (RecyclerView) v.findViewById(R.id.recycleview);
        listView.setLayoutManager(layoutmanager);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        emptyText = (TextView) v.findViewById(R.id.emptyText);
        layoutmanager.setSmoothScrollbarEnabled(true);
        listView.setVerticalScrollBarEnabled(true);
        listView.setOnScrollListener(scrollListener);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            boolean enabled = manager.findFirstCompletelyVisibleItemPosition() == 0;
            swipeRefreshLayout.setEnabled(enabled);
        }
    };

    public void swipeListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.d("OnefreshCalled", "true");
                if (liveIntentService != null) {
                    getLiveEnquiryFromServer();
                }
            }

        });
    }

    //for hide a view
    public void hideView(View v) {
        if (v != null) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    //for visible a view
    public void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void refreshAdapter() {
        ArrayList<EnquiryColumns> enquiryList;
        db.open();
        enquiryList = db.getEnquiry(livestatus);
        db.close();
        showView(loader);//start loader
        if (enquiryList.size() > 0) {
            mAdapter.updateAdapter(enquiryList);//update enquiry list;
            hideView(loader);
            hideView(emptyText);
        } else {
            hideView(loader);
            showView(emptyText);
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLiveFragmentListener) activity;
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

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        swipeRefreshLayout.setRefreshing(false);
        Log.d("EnquiryReceivedQuoted", enquiry.toString());
        if (enquiry.toString().contains(message)) { //here we check enquiry => message then its error message
            Toast.makeText(getActivity(), enquiry.toString().split(":")[1], Toast.LENGTH_SHORT).show();//show error on screen
        } else if (enquiry.toString().contains(ENQUIRYCOUNT)) {
            Log.d("EnquiryCount", enquiry.toString().split(":")[1]);
            int count = Integer.parseInt(enquiry.toString().split(":")[1]);
            if (count != 0) {
                getLiveEnquiryFromServer();
            }
            refreshAdapter();
            //getLiveEnquiryFromServer();
        }
    }


    public interface OnLiveFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
