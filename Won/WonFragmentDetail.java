package logistic.compare.comparelogistic.Won;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import logistic.compare.comparelogistic.Adapter.EnquiryDetailAdapter;
import logistic.compare.comparelogistic.Adapter.EnquiryJsonCreator;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.Live.LiveEnquiryJsonParser;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.gcm.dialog;

public class WonFragmentDetail extends Fragment implements CommonUtility, CommonInterface {
    View rootView;
    RecyclerView recyclerView;
    EnquiryDetailAdapter mAdapter;
    EnquiryColumns ec;
    LinearLayoutManager layoutmanager;
    LinearLayout bottomHeader;
    TextView send_quote, cancel_enquiry;
    LiveEnquiryJsonParser parser;
    Animation bottom_top, top_bottom;
    dialog d;
    DB db;
    ProgressBar loader;
    private OnFragmentInteractionListener mListener;


    public WonFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ec = (EnquiryColumns) getArguments().getSerializable("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_won_fragment_detail, container, false);
        }
        initComponent(rootView);
        setData();
        setScrolling();
        return rootView;
    }

    private void setData() {
        EnquiryJsonCreator ejc = new EnquiryJsonCreator(ec.getExtra());
        JSONObject data = ejc.RaiseEnquiryJsonObject(ec.getExtra());
        parser = new LiveEnquiryJsonParser(data);
        data = parser.quotedParse(); // here we parse and manage enquiry
        // data = getQuoteDetail(data); // here we add getQuoteDetail to json
        data = getCustomerDetail(data);//here we add customer details to json
        Log.d("parsedata", "dataParsed" + data.toString());
        if (ec != null) {
            mAdapter = new EnquiryDetailAdapter(getActivity(), data);
            mAdapter.setCommonInterface(this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public JSONObject getCustomerDetail(JSONObject data) {
        try {
         //   Log.d("Customer", this.ec.getCUSTOMER());
            JSONArray array = new JSONArray();
            JSONObject object = (JSONObject) array.get(0);
            data.put("Customer", object.getString(FULLNAME));
            data.put("Mobile", object.getString(MOBILE));
            data.put("Email", object.getString(EMAIL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public JSONObject getQuoteDetail(JSONObject data) {
        try {
            data.put(PRICE, this.ec.getPRICE());
            data.put(DAYS, this.ec.getDAYS());
            data.put(VALIDITY, this.ec.getVALIDITY());
            data.put(REMARK, this.ec.getREMARK());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
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
                        Log.d("ScrollUp", "true");
                        toggleView(bottomHeader);
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        //showView(bottomHeader);
                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        // hideView(bottomHeader);
                    }
                }
            });
        }

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


    private void initComponent(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        layoutmanager = new LinearLayoutManager(getActivity());
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
        recyclerView.setLayoutManager(layoutmanager);
        bottomHeader = (LinearLayout) rootView.findViewById(R.id.bottombar);
        bottom_top = AnimationUtils.loadAnimation(getActivity(), R.anim.up_from_bottom);
        top_bottom = AnimationUtils.loadAnimation(getActivity(), R.anim.down_from_top);
        send_quote = (TextView) rootView.findViewById(R.id.send_quote);
        cancel_enquiry = (TextView) rootView.findViewById(R.id.cancel_enquiry);
        layoutmanager.setSmoothScrollbarEnabled(true);
        db = new DB(getActivity());
        layoutmanager.setMeasuredDimension(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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

    @Override
    public void RefreshEnquiry(String status, int maxid) {
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("EnquiryReceived", enquiry.toString());
        if (enquiry.toString().contains("call")) {
            callMobile(enquiry.toString().split(":")[1]);
        } else if (enquiry.toString().contains("email")) {
            sendEmail(enquiryurl.toString().split(":")[1]);
        }
    }

    public void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quotation from " + User.getInstance().getCompanyname());
        intent.putExtra(Intent.EXTRA_TEXT, "We are happy to delivery service for you");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    public void callMobile(String mobile) {
        /*
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            getPermission();
        }*/
        String uri = "tel:" + mobile.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void getPermission() {
        Log.d("GetPermission", "true");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CALL_PHONE)) {
            Log.d("if", "true");
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            Log.d("else", "true");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("RequestFragment", "true");
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0   //permission is granted
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionGranted", "true");
                } else {
                    //permission is denied
                }
                break;

            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
