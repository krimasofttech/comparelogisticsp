package logistic.compare.comparelogistic.Truck;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

public class TruckFragment extends Fragment implements CommonInterface {
    private OnTruckFragmentListener mListener;
    View rootView;
    RecyclerView mListView;
    TruckListAdapter mAdapter;
    DB db;
    ArrayList<PostTruckBean> trucklist;
    RecyclerView.LayoutManager layoutManager;
    MyIntentService myIntentService;
    ProgressBar loader;
    TextView searchBox;
    FloatingActionButton floattruckbutton;

    public TruckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindService();
    }

    public void BindService() {
        Log.d("ServiceBind", "true");
        if (mServiceConnection != null) {
            Log.d("ServiceConnection", "notnull");
            Intent intent = new Intent(getActivity(), MyIntentService.class);
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
            MyIntentService.MyBinder binder = (MyIntentService.MyBinder) service;
            myIntentService = binder.getService();//after successfull connection with Intent Service
            updateTruck();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceDisconnected", "true");
        }
    };

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_truck_list, container, false);
        }
        getCurrentTruck();
        initComponent(rootView);
        return rootView;
    }

    public void getCurrentTruck() {
        if (db == null) {
            db = new DB(getActivity());
        }
        db.open();
        this.trucklist = db.getPostTruck();
        db.close();
    }

    public void initComponent(View mView) {
        mListView = (RecyclerView) mView.findViewById(R.id.listview);
        layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new TruckListAdapter(getActivity(), this.trucklist, this);
        loader = (ProgressBar) mView.findViewById(R.id.loader);
        mListView.setHasFixedSize(true);
        floattruckbutton = (FloatingActionButton) mView.findViewById(R.id.fab);
        floattruckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostTruckActivity.class));
                getActivity().overridePendingTransition(0, R.anim.down_from_top);
            }
        });
        mListView.setAdapter(mAdapter);
        searchBox = (TextView) mView.findViewById(R.id.searchBox);
        mListView.setLayoutManager(layoutManager);
        TruckListAdapter.setmTruckListAdapter(mAdapter);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCityActivity();
            }
        });
    }

    public void startCityActivity() {
        startActivity(new Intent(getActivity(), SearchCityActivity.class));
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTruckFragmentListener) activity;
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
            TruckListAdapter.getmTruckListAdapter().updateTruckList();
        } else if (enquiry.toString().contains("pos")) {
            Intent intent = new Intent(getActivity(), ViewTruckActivity.class);
            Log.d("Pos", enquiry.toString().split(":")[1]);
            intent.putExtra("current", enquiry.toString().split(":")[1]);
            intent.putExtra("trucklist", TruckListAdapter.getmTruckListAdapter().trucklist);
            startActivity(intent);
        }
    }

    public interface OnTruckFragmentListener {
        // TODO: Update argument type and name
        public void onTruckInteraction(Object uri);
    }

}
