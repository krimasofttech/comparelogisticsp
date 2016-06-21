package logistic.compare.comparelogistic.Truck;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.ConnectionUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.R;

public class ViewTruckFragment extends Fragment implements CommonInterface {

    private OnViewTruckListener mListener;
    PostTruckBean ptb;
    View rootView;
    LinearLayout pickuplayout, deliverylayout, customerpanel, ftllinear;
    TextView pickupcitytext, deliverycitytext;
    CardView onewaycard, roundtrip;
    ImageView oneway, FTL;
    TextView ftltext, datetext, selecttruck, waytext;
    FrameLayout movingdateframe, truckframe;
    TextView capacity, freight, remark;
    Button submit;
    String trucway = "";
    String CUSTOMERNAME, CONTACTNO, EMAIL;
    RelativeLayout root;
    DB db;
    String flag;
    PostTruckAsync pta;
    String FTLLTL = "0";
    ProgressBar loader;
    String url = "http://comparelogistic.in/appSender/spInfoView";
    String isviewedurl = "http://comparelogistic.in/appSender/IsViewed";
    TextView customertextvalue, contactnotextvalue, emailtextvalue;
    boolean b = true;

    public ViewTruckFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ptb = (PostTruckBean) getArguments().getSerializable("truck");
            PostTruckBean.setPtb(ptb);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_view_truck, container, false);
        }
        Log.d("oncreatecalled", "true");
        initComponent(rootView);
        setData();
        flag = "oncreate";
        startThread(flag);
        return rootView;
    }

    public void initComponent(View rootView) {
        selecttruck = (TextView) rootView.findViewById(R.id.selecttruck);
        root = (RelativeLayout) rootView.findViewById(R.id.root);
        truckframe = (FrameLayout) rootView.findViewById(R.id.selecttruckframe);
        pickuplayout = (LinearLayout) rootView.findViewById(R.id.pickuplayout);
        deliverylayout = (LinearLayout) rootView.findViewById(R.id.deliverylayout);
        pickupcitytext = (TextView) rootView.findViewById(R.id.pickupcitytext);
        onewaycard = (CardView) rootView.findViewById(R.id.onewaycard);
        //   customerpanel = (LinearLayout) rootView.findViewById(R.id.customerpanel);
        submit = (Button) rootView.findViewById(R.id.submitbutton);
        oneway = (ImageView) rootView.findViewById(R.id.oneway);
        //  customerpanel.setVisibility(View.VISIBLE);
        waytext = (TextView) rootView.findViewById(R.id.waytext);
        roundtrip = (CardView) rootView.findViewById(R.id.roundwaycard);
        deliverycitytext = (TextView) rootView.findViewById(R.id.deliverycity);
        oneway = (ImageView) rootView.findViewById(R.id.oneway);
        FTL = (ImageView) rootView.findViewById(R.id.FTL);
        ftllinear = (LinearLayout) rootView.findViewById(R.id.ftllinear);
        ftltext = (TextView) rootView.findViewById(R.id.ftltext);
        movingdateframe = (FrameLayout) rootView.findViewById(R.id.movingdateframe);
        datetext = (TextView) rootView.findViewById(R.id.datetext);
        capacity = (TextView) rootView.findViewById(R.id.capacityvalue);
        remark = (TextView) rootView.findViewById(R.id.remark);
        freight = (TextView) rootView.findViewById(R.id.freightvalue);
        pta = new PostTruckAsync();
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "submit";
                v.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
                startThread(flag);
            }
        });
        setWay();
    }

    public void startThread(String status) {
        if (new ConnectionUtility(getActivity()).isConnectingToInternet()) { // connectivity available
            if (status.equals("submit"))
                getCustomerInfo(); // called while view customer button hit.
            else if (status.equals("oncreate")) {
                isAlreadyViewed();//check with server already viewed or not.
            }
        } else {
            //no connectivity available
            Toast.makeText(getActivity(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
            loader.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
    }

    public void isAlreadyViewed() {
        String url = isviewedurl;
        pta = new PostTruckAsync();
        pta.setCommonInterface(this);
        pta.setFormEncodingBuilder(getBuilder());
        pta.setUrl(url);
        pta.execute();
    }

    public void getCustomerInfo() {
        pta = new PostTruckAsync();
        pta.setCommonInterface(this);
        pta.setFormEncodingBuilder(getBuilder());
        pta.setUrl(url);
        pta.execute();
    }

    //set data of one way and round trip
    public void setWay() {
        if (ptb.getWAY().contains("one")) {
            oneway.setImageResource(R.drawable.arrowright);
            waytext.setText("oneway");
        } else if (ptb.getWAY().contains("two")) {
            oneway.setImageResource(R.drawable.arrowleft);
            waytext.setText("Round trip");
        }
    }

    public FormBody.Builder getBuilder() {
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        formEncodingBuilder.add("spid", this.ptb.getSPID());
        formEncodingBuilder.add("SP", User.getInstance().getID());
        formEncodingBuilder.add("enqid", this.ptb.getID());
        formEncodingBuilder.add("type", "truck");
        return formEncodingBuilder;
    }


    public void setData() {
        if (ptb != null) {
            selecttruck.setText(ptb.getTYPEOFVEHICLE());
            pickupcitytext.setText(ptb.getFROMCITY());
            deliverycitytext.setText(ptb.getTOCITY());
            datetext.setText(ptb.getPICKUPDATE());
            capacity.setText(ptb.getCAPACITY());
            freight.setText(ptb.getFREIGHT());
            remark.setText(ptb.getREMARK());
            setFTLTL(ptb.getFTLLTL());
        }
    }

    public void setFTLTL(String ftlltl) {
        ftltext.setTextColor(getActivity().getResources().getColor(R.color.green));
        if (ftlltl.equals("0")) {
            ftltext.setText("FTL");
            FTL.setImageResource(R.drawable.fullgreen);
        } else {
            ftltext.setText("LTL");
            FTL.setImageResource(R.drawable.ltlgreen);
        }
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnViewTruckListener) activity;
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
        Log.d("CustomerReceived", enquiry.toString());
        b = true;
        if (loader != null)
            loader.setVisibility(View.INVISIBLE);
        String data = enquiry.toString();
        if (submit != null)
            submit.setVisibility(View.VISIBLE);
        try {
            if (enquiry.toString().contains("COMPANYNAME")) { //customer data received{company name,mobile no,email,DONOTCALL}
                JSONObject obj = (JSONObject) new JSONArray(enquiry.toString()).get(0);
                data = obj.getString("COMPANYNAME") + ":" + obj.getString("MOBILE") + ":" + obj.getString("HOEMAIL");
                Log.d("DATACONVERTEDTO", data);
            } else if (enquiry.toString().contains("nocredit")) {
                data = "nocredit";
            } else if (data.contains("no transaction")) {
                data = "notransaction";
            } else if (data.contains("notviewed")) {
                submit.setVisibility(View.VISIBLE);
            } else if (data.contains("notparams")) {
                Toast.makeText(getActivity(), "Something went wrong with system", Toast.LENGTH_SHORT).show();
            }
            if (mListener != null)
                mListener.onViewInteraction(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setData(String data) {
        String[] cust = data.split(":");
        Log.d("oncreate", data.toString());
        customerpanel.setVisibility(View.VISIBLE);
        CUSTOMERNAME = cust[0];
        CONTACTNO = cust[1];
        EMAIL = cust[2];
        customertextvalue.setText(CUSTOMERNAME);
        contactnotextvalue.setText(CONTACTNO);
        emailtextvalue.setText(EMAIL);
        submit.setVisibility(View.GONE);
    }

    public interface OnViewTruckListener {
        // TODO: Update argument type and name
        public void onViewInteraction(String uri);
    }

}
