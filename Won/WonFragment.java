package logistic.compare.comparelogistic.Won;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import logistic.compare.comparelogistic.AsyncTask.CustomerIntentService;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Quoted.QuotedAdapter;
import logistic.compare.comparelogistic.Quoted.QuotedIntentService;
import logistic.compare.comparelogistic.R;


public class WonFragment extends Fragment implements CommonInterface, CommonUtility {
    View rootView;
    ProgressBar loader, enquiryloader;
    RecyclerView listView;
    public static WonAdapter mAdapter;
    ArrayList<EnquiryColumns> enquiryList;
    WonIntentService quotedintentservice;
    DB db;
    String pos;
    CustomerIntentService customerIntentService;
    ImageView backimage;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutmanager;
    TextView emptyText;
    boolean isLoading = false;
    private OnWonFragmentListener mListener;

    public WonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public void bindCustomerIntentService() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_won, container, false);
        }
        initComponent(rootView);
        swipeListener();
        hideView(loader);
        hideView(emptyText);
        setAdapter();
        listView.setOnScrollListener(scrollListener);
        return rootView;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            boolean enabled = manager.findFirstCompletelyVisibleItemPosition() == 0;
            swipeRefreshLayout.setEnabled(enabled);
        }
    };

    public void setAdapter() {
        Log.d("Adapter", "true");
        enquiryList = getEnquiryList();
        mAdapter = new WonAdapter(getActivity(), enquiryList, this);
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
            Intent intent = new Intent(getActivity(), WonIntentService.class);
            boolean b = getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d("Connected", "" + b);
            if (b == true) {
                //getLiveEnquiryFromServer();
            }
        } else {
            Log.d("serviceConnection", "null");
        }
    }


    public ArrayList<EnquiryColumns> getEnquiryList() {
        ArrayList<EnquiryColumns> list = new ArrayList<>();
        db.open();
        list = db.getEnquiry(wonstatus);
        db.close();
        return list;
    }

    //initialization of view
    public void initComponent(View v) {
        db = new DB(getActivity());
        layoutmanager = new LinearLayoutManager(getActivity());
        backimage = (ImageView) v.findViewById(R.id.backimage);
        loader = (ProgressBar) v.findViewById(R.id.loader);
        listView = (RecyclerView) v.findViewById(R.id.recycleview);
        listView.setLayoutManager(layoutmanager);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        emptyText = (TextView) v.findViewById(R.id.emptyText);
        layoutmanager.setSmoothScrollbarEnabled(true);
    }

    public void swipeListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (quotedintentservice != null) {
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
        enquiryList = db.getEnquiry(wonstatus);
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


    //here we get max id from database call IntentService to execute for fetch Live Enquiry Data
    public void getLiveEnquiryFromServer() {
        db.open();
        int maxid = db.getEnquiryMaxId(wonstatus);
        db.close();
        Log.d("Maxid", "" + maxid);
        quotedintentservice.setCommonInterface(this);//here we set commonInterface as this  class in IntentService
        quotedintentservice.RefreshEnquiry(wonstatus, maxid);//
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnected", "true");
            WonIntentService.MyBinder binder = (WonIntentService.MyBinder) service;
            quotedintentservice = binder.getService();//after successfull connection with Intent Service
            getLiveEnquiryFromServer();
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
            mListener = (OnWonFragmentListener) activity;
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
        Log.d("Enquiry", enquiry.toString());
        try {
            swipeRefreshLayout.setRefreshing(false);
            Log.d("EnquiryReceivedLive", enquiry.toString());
            if (enquiry.toString().contains(message)) { //here we check enquiry => message then its error message
                Toast.makeText(getActivity(), enquiry.toString().split(":")[1], Toast.LENGTH_SHORT).show();
                //show error on screen
            } else if (enquiry.toString().contains(ENQUIRYCOUNT)) { //response from wonIntentService
                refreshAdapter();
            } else if (enquiry instanceof ProgressBar) { //response from wonAdapter
                Log.d("Loader", "true");
                showLoader(enquiry);
            } else if (enquiry.toString().contains(isExist)) { //response from intent service if customer exist
                disableLoader();
            } else if (enquiry.toString().contains("customer-")) {//response from wonIntentService
                disableLoader();
                Log.d("JsonObject", enquiry.toString().split("-")[1]);
                JSONArray customerArray = new JSONArray(enquiry.toString().split("-")[1]);
                JSONObject obj = (JSONObject) customerArray.get(0);
                openFullEnquiry(pos, enquiry.toString().split("-")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableLoader() {
        isLoading = false;
        hideView(enquiryloader);
    }

    //show loader on enquiry
    public void showLoader(Object enquiry) {
        if (isLoading == false) { //isLoading check that user clicked already or not
            if (new ConnectionUtility(getActivity()).isConnectingToInternet()) {
                enquiryloader = (ProgressBar) enquiry;
                pos = enquiryloader.getTag().toString();  // get the position of  enquiryColumns object in ArrayList
                isLoading = true;
                showView(enquiryloader);
                quotedintentservice.getCustomerDetail(WonAdapter.getEnquirylist().get(Integer.parseInt(pos)).getID());
            } else {
                Snackbar.make(swipeRefreshLayout, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(swipeRefreshLayout, "Request is Already in Process,Please wait......", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void openFullEnquiry(String pos, String customer) {
        EnquiryColumns ec = WonAdapter.getEnquirylist().get(Integer.parseInt(pos));
       // ec.setCUSTOMER(customer);
        EnquiryColumns.setEnquiryColumns(ec);
        Intent intent = new Intent(getActivity(), WonActivity.class);
        Log.d("Position", pos);
        intent.putExtra("pos", pos);
        getActivity().startActivity(intent);
    }

    public interface OnWonFragmentListener {
        // TODO: Update argument type and name
        public void onWonFragmentInteration(Uri uri);
    }

}
