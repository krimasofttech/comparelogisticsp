package logistic.compare.comparelogistic.Profile.load;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.Profile.ProfileIntentService;
import logistic.compare.comparelogistic.Profile.ProfileTruckAdapter;
import logistic.compare.comparelogistic.Profile.truck.MyProfileIntentService;
import logistic.compare.comparelogistic.Profile.truck.MyViewTruckActivity;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import okhttp3.FormBody;

public class MyLoadPost extends Fragment implements CommonInterface {

    private MyLoadPostListener mListener;
    public View rootView;
    ViewPager myPager;
    RecyclerView myListView;
    TextView emptyText;
    ProgressBar loader;
    ImageView toogle;
    LinearLayoutManager mLinearLayout;
    MyListAdapter myListAdapter;
    MyProfileIntentService myInteyntService;
    MyViewPagerAdapter myViewPagerAdapter;
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

    public MyLoadPost() {
        // Required empty public constructor
    }

    public void getPostData() {
        if (loader != null) {
            loader.setVisibility(View.VISIBLE);
        }
        FormBody.Builder mBuilder = myInteyntService.getBuilder("load", User.getInstance().getID());
        if (mBuilder != null) {
            myInteyntService.setType("load");
            myInteyntService.setCommonInterface(this);
            myInteyntService.getPost(this.url, mBuilder);
        }
    }

    public void BindService() {
        Intent intent = new Intent(getActivity(), MyProfileIntentService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_load_post, container, false);
        }
        initComponent(rootView);
        ToggleEvent();
        BindService();
        return rootView;
    }

    public void initComponent(View mView) {
        if (mView != null) {
            toogle = (ImageView) mView.findViewById(R.id.toogle);
            myListView = (RecyclerView) mView.findViewById(R.id.listview);
            myPager = (ViewPager) mView.findViewById(R.id.viewpager);
            emptyText = (TextView) mView.findViewById(R.id.emptyText);
            loader = (ProgressBar) mView.findViewById(R.id.loader);
            mLinearLayout = new LinearLayoutManager(getContext());
            mLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
            myListView.setLayoutManager(mLinearLayout);
        }
    }

    public void ToggleEvent() {
        toogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(v.getTag().toString());
            }
        });
    }

    public void changeView(String status) {
        if (status.trim().equals("list")) {
            toogle.setImageResource(R.drawable.grid_view_icon);
            myListView.setVisibility(View.VISIBLE);
            myPager.setVisibility(View.INVISIBLE);
            toogle.setTag("grid");
        } else if (status.trim().equals("grid")) {
            myListView.setVisibility(View.INVISIBLE);
            myPager.setVisibility(View.VISIBLE);
            toogle.setImageResource(R.drawable.list_menu_icon);
            toogle.setTag("list");
        }
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (MyLoadPostListener) activity;
            mListener.onMyLoadPostInteraction("My Load Post");
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

    public void HideShowEmptyText(int visibility) {
        if (emptyText != null)
            emptyText.setVisibility(visibility);
    }

    public void updateAdapter(ArrayList<PostLoadBean> truckList) {
        myListAdapter = new MyListAdapter(getActivity(), truckList, this);
        myListAdapter.setCommonInterface(this);
        myListAdapter.setMyList(truckList);
        myListAdapter.notifyDataSetChanged();
        myListView.setAdapter(myListAdapter);
        myViewPagerAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        myViewPagerAdapter.setLoadList(truckList);
        myViewPagerAdapter.notifyDataSetChanged();
        myPager.setAdapter(myViewPagerAdapter);
        myPager.setVisibility(View.INVISIBLE);
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        loader.setVisibility(View.INVISIBLE);
        HideShowEmptyText(View.INVISIBLE);
        Log.d("mytruckdetail", enquiry.toString());
        try {
            if (enquiry instanceof ArrayList) {
                updateAdapter((ArrayList<PostLoadBean>) enquiry);//update Adapter
            } else if (enquiry.toString().equals("false")) {
                emptyText.setText("Oops you didn't posted truck yet.");
                HideShowEmptyText(View.VISIBLE);
            } else if (enquiry.toString().contains("pos")) {
               /* Intent intent = new Intent(getActivity(), MyViewTruckActivity.class);
                Log.d("Pos", enquiry.toString().split(":")[1]);
                intent.putExtra("current", enquiry.toString().split(":")[1]);
                intent.putExtra("trucklist", ProfileTruckAdapter.getmTruckListAdapter().trucklist);
                startActivity(intent);*/
                myListView.setVisibility(View.INVISIBLE);
                myPager.setVisibility(View.VISIBLE);
                toogle.setTag("list");
                toogle.setImageResource(R.drawable.list_menu_icon);
                myPager.setCurrentItem(Integer.parseInt(enquiry.toString().split(":")[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MyLoadPostListener {
        // TODO: Update argument type and name
        public void onMyLoadPostInteraction(String uri);
    }

}
