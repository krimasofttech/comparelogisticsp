package logistic.compare.comparelogistic.Load;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import logistic.compare.comparelogistic.Truck.PostTruckBean;

public class ViewloadFragment extends Fragment implements CommonInterface {

    private OnLoadFragmentListener mListener;
    PostLoadBean ptb;
    View rootView;
    LinearLayout pickuplayout, deliverylayout, ftllinear;
    TextView pickupcitytext, deliverycitytext;
    CardView onewaycard, roundtrip;
    ImageView oneway, FTL;
    TextView ftltext, datetext, selecttruck;
    FrameLayout movingdateframe, truckframe;
    TextView capacity, loaddescription, remark;
    Button submit;
    RelativeLayout root;
    DB db;
    PostTruckAsync pta;
    String url = "http://comparelogistic.in/appSender/spInfoView";
    String isviewedurl = "http://comparelogistic.in/appSender/IsViewed";
    ProgressBar loader;

    public ViewloadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ptb = (PostLoadBean) getArguments().getSerializable("truck");
            PostLoadBean.setPlb(ptb);
        }
    }

    public void initComponent(View rootView) {
        selecttruck = (TextView) rootView.findViewById(R.id.selectloadcategory);
        root = (RelativeLayout) rootView.findViewById(R.id.root);
        truckframe = (FrameLayout) rootView.findViewById(R.id.selecttruckframe);
        pickuplayout = (LinearLayout) rootView.findViewById(R.id.pickuplayout);
        deliverylayout = (LinearLayout) rootView.findViewById(R.id.deliverylayout);
        pickupcitytext = (TextView) rootView.findViewById(R.id.pickupcitytext);
        onewaycard = (CardView) rootView.findViewById(R.id.onewaycard);
        submit = (Button) rootView.findViewById(R.id.submitbutton);
        roundtrip = (CardView) rootView.findViewById(R.id.roundwaycard);
        deliverycitytext = (TextView) rootView.findViewById(R.id.deliverycity);
        oneway = (ImageView) rootView.findViewById(R.id.oneway);
        FTL = (ImageView) rootView.findViewById(R.id.FTL);
        ftllinear = (LinearLayout) rootView.findViewById(R.id.ftllinear);
        ftltext = (TextView) rootView.findViewById(R.id.ftltext);
        movingdateframe = (FrameLayout) rootView.findViewById(R.id.movingdateframe);
        datetext = (TextView) rootView.findViewById(R.id.datetext);
        capacity = (TextView) rootView.findViewById(R.id.weightvalue);
        remark = (TextView) rootView.findViewById(R.id.remark);
        loaddescription = (TextView) rootView.findViewById(R.id.loaddescriptionvalue);
        pta = new PostTruckAsync();
        db = new DB(getActivity());
        loader = (ProgressBar) rootView.findViewById(R.id.loader);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
                startThread("submit");
            }
        });
    }

   /* public void updateLoad(PostLoadBean plb) {
        db.open();
        db.updateCustomerDetail();
        db.close();
    }*/

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
        if (new ConnectionUtility(getActivity()).isConnectingToInternet()) {
            pta = new PostTruckAsync();
            pta.setCommonInterface(this);
            pta.setFormEncodingBuilder(getBuilder());
            pta.setUrl(url);
            pta.execute();
        } else {
            Toast.makeText(getActivity(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
            loader.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
    }

    public FormBody.Builder getBuilder() {
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        formEncodingBuilder.add("spid", ptb.getSPID());
        formEncodingBuilder.add("SP", User.getInstance().getID());
        formEncodingBuilder.add("enqid", ptb.getID());
        formEncodingBuilder.add("type", "load");
        return formEncodingBuilder;
    }

    public void setData() {
        if (ptb != null) {
            selecttruck.setText(ptb.getLOADCATEGORY());
            pickupcitytext.setText(ptb.getFROMCITY());
            deliverycitytext.setText(ptb.getTOCITY());
            datetext.setText(ptb.getPICKUPDATE());
            capacity.setText(ptb.getWEIGHT());
            loaddescription.setText(ptb.getLOADDISCRIPTION());
            remark.setText(ptb.getREMARK());
            setFTLTL(ptb.getFTLANDLTL());
        }
    }

    public void setFTLTL(String ftlltl) {
        Log.d("FTLLTL", ftlltl);
        ftltext.setTextColor(getActivity().getResources().getColor(R.color.green));
        if (ftlltl.equals("FTL")) {
            ftltext.setText("FTL");
            FTL.setImageResource(R.drawable.fullgreen);
        } else {
            ftltext.setText("LTL");
            FTL.setImageResource(R.drawable.ltlgreen);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_viewload, container, false);
        }
        initComponent(rootView);
        setData();
        startThread("oncreate");
        return rootView;
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoadFragmentListener) activity;
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
        //b = true;
        if (loader != null)
            loader.setVisibility(View.INVISIBLE);
        String data = enquiry.toString();
        if (submit != null)
            submit.setVisibility(View.VISIBLE);
        try {
            if (enquiry.toString().contains("COMPANYNAME")) {//data received from server
                JSONObject obj = (JSONObject) new JSONArray(enquiry.toString()).get(0);
                data = obj.getString("COMPANYNAME") + ":" + obj.getString("MOBILE") + ":" + obj.getString("HOEMAIL") + ":" + obj.getString("ID");
                this.ptb.setViewed("1");
            } else if (enquiry.toString().contains("nocredit")) {
                data = "nocredit";
            } else if (data.contains("no transaction")) {
                data = "notransaction";
            } else if (data.contains("notviewed")) {
                // customerpanel.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
            } else if (data.contains("notparams")) {
                Toast.makeText(getActivity(), "Something went wrong with system", Toast.LENGTH_SHORT).show();
            }
            if (mListener != null)
                mListener.onLoadFragmentInterction(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface OnLoadFragmentListener {
        // TODO: Update argument type and name
        public void onLoadFragmentInterction(String uri);
    }

}
