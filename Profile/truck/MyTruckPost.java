package logistic.compare.comparelogistic.Profile.truck;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.Profile.ProfileTruckAdapter;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import okhttp3.FormBody;

public class MyTruckPost extends Fragment implements CommonInterface {

    private MyTruckPostListener mListener;
    View rootView;
    RecyclerView mListView;
    DB db;
    MyProfileIntentService myProfileIntentService;
    ProfileTruckAdapter mAdapter;
    TextView searchBox, emptyText;
    ArrayList<PostTruckBean> trucklist = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    MyProfileIntentService myInteyntService;
    ProgressBar loader;
    String url = "http://comparelogistic.in/appSender/getTruckOrPost";//input  spid , type = truck or load.

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MyProfileIntentService.MyBinder binder = (MyProfileIntentService.MyBinder) service;
            myInteyntService = binder.getService();//after successfull connection with Intent Service
            getPostData();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void getPostData() {
        if (loader != null) {
            loader.setVisibility(View.VISIBLE);
        }
        FormBody.Builder mBuilder = myInteyntService.getBuilder("truck", User.getInstance().getID());
        if (mBuilder != null) {
            myInteyntService.setCommonInterface(this);
            myInteyntService.setType("truck");
            myInteyntService.getPost(this.url, mBuilder);
        }
    }

    public MyTruckPost() {
        // Required empty public constructor
    }

    public void BindService() {
        Intent intent = new Intent(getActivity(), MyProfileIntentService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_truck_post, container, false);
        }
        initComponent(rootView);
        BindService();
        return rootView;
    }


    public void initComponent(View mView) {
        db = new DB(getActivity());
        mListView = (RecyclerView) mView.findViewById(R.id.listview);
        layoutManager = new LinearLayoutManager(getActivity());
        //getMyPost();
        emptyText = (TextView) mView.findViewById(R.id.emptyText);
        mAdapter = new ProfileTruckAdapter(getActivity(), trucklist, this);
        loader = (ProgressBar) mView.findViewById(R.id.loader);
        mListView.setHasFixedSize(true);
        mListView.setAdapter(mAdapter);
        searchBox = (TextView) mView.findViewById(R.id.searchBox);
        mListView.setLayoutManager(layoutManager);
        ProfileTruckAdapter.setmTruckListAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (MyTruckPostListener) activity;
            mListener.onFragmentInteraction("My Truck Post");
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
        loader.setVisibility(View.INVISIBLE);
        HideShowEmptyText(View.INVISIBLE);
        Log.d("mytruckdetail", enquiry.toString());
        try {
            if (enquiry instanceof ArrayList) {
                updateAdapter((ArrayList<PostTruckBean>) enquiry);//update Adapter
            } else if (enquiry.toString().equals("false")) {
                emptyText.setText("Oops you didn't posted truck yet.");
                HideShowEmptyText(View.VISIBLE);
            } else if (enquiry.toString().contains("pos")) {
                Intent intent = new Intent(getActivity(), MyViewTruckActivity.class);
                Log.d("Pos", enquiry.toString().split(":")[1]);
                intent.putExtra("current", enquiry.toString().split(":")[1]);
                intent.putExtra("trucklist", ProfileTruckAdapter.getmTruckListAdapter().trucklist);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAdapter(ArrayList<PostTruckBean> truckList) {
        mAdapter.setCommonInterface(this);
        mAdapter.setTrucklist(truckList);
        mAdapter.notifyDataSetChanged();
    }

    public void HideShowEmptyText(int visibility) {
        if (emptyText != null)
            emptyText.setVisibility(visibility);
    }

    public interface MyTruckPostListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String uri);
    }

}
