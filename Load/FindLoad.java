package logistic.compare.comparelogistic.Load;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Search.SearchCityActivity;
import logistic.compare.comparelogistic.Search.SearchLoadActivity;
import logistic.compare.comparelogistic.Search.SearchLoadCityActivity;
import logistic.compare.comparelogistic.Truck.MyIntentService;
import logistic.compare.comparelogistic.Truck.PostTruckActivity;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import logistic.compare.comparelogistic.Truck.TruckListAdapter;

public class FindLoad extends Fragment implements CommonInterface {
    private FindLoadListener mListener;
    View rootView;
    RecyclerView mListView;
    DB db;
    FindLoadAdapter mAdapter;
    TextView searchBox;
    ArrayList<PostLoadBean> trucklist;
    RecyclerView.LayoutManager layoutManager;
    LoadIntentService myIntentService;
    ProgressBar loader;
    FloatingActionButton floattruckbutton;


    public FindLoad() {
        // Required empty public constructor
    }

    public void BindService() {
        Log.d("ServiceBind", "true");
        if (mServiceConnection != null) {
            Log.d("ServiceConnection", "notnull");
            Intent intent = new Intent(getActivity(), LoadIntentService.class);
            boolean b = getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d("Connected", "" + b);
        } else {
            Log.d("serviceConnection", "null");
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceConnected", "true");
            LoadIntentService.MyBinder binder = (LoadIntentService.MyBinder) service;
            myIntentService = binder.getService();//after successfull connection with Intent Service
            updateTruck();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceDisconnected", "true");
        }
    };


    public void getCurrentTruck() {
        if (db == null) {
            db = new DB(getActivity());
        }
        db.open();
        this.trucklist = db.getPostLoad();
        db.close();
    }

    public void initComponent(View mView) {
        mListView = (RecyclerView) mView.findViewById(R.id.listview);
        layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new FindLoadAdapter(getActivity(), trucklist, this);
        loader = (ProgressBar) mView.findViewById(R.id.loader);
        mListView.setHasFixedSize(true);
        floattruckbutton = (FloatingActionButton) mView.findViewById(R.id.fab);
        floattruckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostLoadActivity.class));
                getActivity().overridePendingTransition(0, R.anim.down_from_top);
            }
        });
        mListView.setAdapter(mAdapter);
        searchBox = (TextView) mView.findViewById(R.id.searchBox);
        mListView.setLayoutManager(layoutManager);
        FindLoadAdapter.setmTruckListAdapter(mAdapter);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivtiy();
            }
        });
    }

    public void startActivtiy() {
        startActivity(new Intent(getActivity(), SearchLoadCityActivity.class));
    }


    public void updateTruck() {
        loader.setVisibility(View.VISIBLE);
        if (myIntentService != null) {
            myIntentService.setCommonInterface(this);
            myIntentService.updateNewTruck();
        } else {
            loader.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_find_load, container, false);
        }
        getCurrentTruck();
        initComponent(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (FindLoadListener) activity;
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
        Log.d("ReceiveTruckFragment", enquiry.toString());
        //stop loader
        loader.setVisibility(View.INVISIBLE);
        if (enquiry.toString().equals("success")) {
            FindLoadAdapter.getmTruckListAdapter().updateTruckList();
        } else if (enquiry.toString().contains("pos")) {
            String pos = enquiry.toString().split(":")[1];
            Intent intent = new Intent(getActivity(), ViewLoadActivity.class);
            intent.putExtra("current", pos);
            intent.putExtra("loadlist", FindLoadAdapter.getmTruckListAdapter().trucklist);
            startActivity(intent);
        }
    }


    public interface FindLoadListener {
        // TODO: Update argument type and name
        public void OnFindInteraction(String uri);
    }

}
