package logistic.compare.comparelogistic.Load;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import logistic.compare.comparelogistic.Loader.TruckLoader;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.CalenderPicker;
import logistic.compare.comparelogistic.CityActivity;
import logistic.compare.comparelogistic.CommonInterface;
import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;
import logistic.compare.comparelogistic.Truck.TruckActivity;

public class PostLoad extends Fragment implements View.OnClickListener, CommonUtility, CommonInterface {

    View rootView;
    LinearLayout pickuplayout, deliverylayout, ftllinear, ltllinear;
    TextView pickupcitytext, deliverycitytext;
    CardView onewaycard, roundtrip;
    ImageView oneway, roundtripview, FTL, LTL;
    TextView ftltext, ltltext, datetext, selecttruck;
    FrameLayout movingdateframe, truckframe;
    EditText capacity, freight, remark;
    Button submit;
    String trucway = "";
    RelativeLayout root;
    TextView kg, ton;
    DB db;
    FrameLayout loaderframe;
    PostTruckAsync pta;
    String FTLLTL = "FTL";
    String dimen = "KG";
    TruckLoader truckLoader;
    TextView status;


    private OnPostFragmentInteractionListener mListener;


    public PostLoad() {
        // Required empty public constructor
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
            rootView = inflater.inflate(R.layout.fragment_post_load, container, false);
        }
        initComponent(rootView);
        return rootView;
    }

    public void initComponent(View rootView) {
        try {
            selecttruck = (TextView) rootView.findViewById(R.id.selectloadcategory);
            root = (RelativeLayout) rootView.findViewById(R.id.root);
            loaderframe = (FrameLayout) rootView.findViewById(R.id.loaderframe);
            truckframe = (FrameLayout) rootView.findViewById(R.id.selecttruckframe);
            pickuplayout = (LinearLayout) rootView.findViewById(R.id.pickuplayout);
            deliverylayout = (LinearLayout) rootView.findViewById(R.id.deliverylayout);
            pickupcitytext = (TextView) rootView.findViewById(R.id.pickupcitytext);
            truckLoader = (TruckLoader) rootView.findViewById(R.id.truckloader);
            status = (TextView) rootView.findViewById(R.id.status);
            submit = (Button) rootView.findViewById(R.id.submitbutton);
            roundtrip = (CardView) rootView.findViewById(R.id.roundwaycard);
            deliverycitytext = (TextView) rootView.findViewById(R.id.deliverycity);
            kg = (TextView) rootView.findViewById(R.id.kg);
            ton = (TextView) rootView.findViewById(R.id.ton);
            if (kg != null)
                kg.setBackgroundResource(R.drawable.dimenbackground2);
            roundtripview = (ImageView) rootView.findViewById(R.id.rounttrip);
            FTL = (ImageView) rootView.findViewById(R.id.FTL);
            LTL = (ImageView) rootView.findViewById(R.id.LTL);
            ftllinear = (LinearLayout) rootView.findViewById(R.id.ftllinear);
            ltllinear = (LinearLayout) rootView.findViewById(R.id.ltllinear);
            ftltext = (TextView) rootView.findViewById(R.id.ftltext);
            ltltext = (TextView) rootView.findViewById(R.id.ltltext);
            movingdateframe = (FrameLayout) rootView.findViewById(R.id.movingdateframe);
            datetext = (TextView) rootView.findViewById(R.id.datetext);
            capacity = (EditText) rootView.findViewById(R.id.weight);
            remark = (EditText) rootView.findViewById(R.id.remark);
            freight = (EditText) rootView.findViewById(R.id.loaddescription);
            clickEvent();
            datetext.setText(getCurrentDate());
            pta = new PostTruckAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return CalenderPicker.getDate(calendar);
    }

    public void clickEvent() {
        truckframe.setOnClickListener(this);
        ftllinear.setOnClickListener(this);
        ltllinear.setOnClickListener(this);
        submit.setOnClickListener(this);
        kg.setOnClickListener(this);
        ton.setOnClickListener(this);
        pickuplayout.setOnClickListener(this);
        deliverylayout.setOnClickListener(this);
        movingdateframe.setOnClickListener(this);
        capacity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(v.getText().toString())) {
                    v.setError(null);
                    return false;
                }
                return true;
            }
        });
        freight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(v.getText().toString())) {
                    v.setError(null);
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.hasExtra("city")) {
                String city = data.getStringExtra("city");
                if (requestCode == 0) {
                    pickupcitytext.setText(city);
                    pickupcitytext.setError(null);
                } else if (requestCode == 1) {
                    deliverycitytext.setText(city);
                    deliverycitytext.setError(null);
                }
            } else if (data.hasExtra("date")) {
                String date = data.getStringExtra("date");
                Log.d("dateReceived", date);
                //  if (requestCode == 2) {
                datetext.setText(date);
                datetext.setError(null);
                //}
            } else if (data.hasExtra("load")) {
                String truck = data.getStringExtra("load");
                selecttruck.setText("  " + truck);
                selecttruck.setError(null);
            }
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostFragmentInteractionListener) activity;
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

    public boolean insertPostTruck(PostLoadBean ptb) {
        if (db == null) {
            db = new DB(getActivity());
        }
        db.open();
        long w = db.createMyPostLoad(ptb);
        db.close();
        if (w > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        Log.d("ObjectReceived", enquiry.toString());

        try {
            PostLoadBean.getPlb().setID(new JSONObject(enquiry.toString()).getString("ID"));
            if (insertPostTruck(PostLoadBean.getPlb())) {
                loaderframe.setVisibility(View.INVISIBLE);
                status.setVisibility(View.VISIBLE);
                truckLoader.setVisibility(View.VISIBLE);
                Snackbar.make(root, "Enjoy Service,Your Load has been Posted", Snackbar.LENGTH_LONG).show();
                submit.setEnabled(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DataReceived", enquiry.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onewaycard:
                changeImage(roundtripview, R.drawable.arrowleftgray);
                changeImage(oneway, R.drawable.arrowright);
                trucway = "oneway";
                break;
            case R.id.roundwaycard:
                changeImage(oneway, R.drawable.arrowrightlightgray);
                changeImage(roundtripview, R.drawable.arrowleft);
                trucway = "twoway";
                break;
            case R.id.pickuplayout:
                startCityActivity(0);
                break;
            case R.id.deliverylayout:
                startCityActivity(1);
                break;
            case R.id.ftllinear:
                changeImage(FTL, R.drawable.fullgreen);
                changeImage(LTL, R.drawable.ltl);
                ftltext.setTextColor(getActivity().getResources().getColor(R.color.black));
                ltltext.setTextColor(getActivity().getResources().getColor(R.color.grey));
                FTLLTL = "0";
                break;
            case R.id.kg:
                kg.setBackgroundResource(R.drawable.dimenbackground2);
                ton.setBackgroundResource(R.drawable.dimenbackground);
                dimen = "KG";
                break;
            case R.id.ton:
                kg.setBackgroundResource(R.drawable.dimenbackground);
                ton.setBackgroundResource(R.drawable.dimenbackground2);
                dimen = "TON";
                break;
            case R.id.ltllinear:
                changeImage(LTL, R.drawable.ltlgreen);
                changeImage(FTL, R.drawable.full);
                ftltext.setTextColor(getActivity().getResources().getColor(R.color.grey));
                ltltext.setTextColor(getActivity().getResources().getColor(R.color.green));
                FTLLTL = "1";
                break;
            case R.id.movingdateframe:
                startActivityForResult(new Intent(getActivity(), CalenderPicker.class), 2);
                break;
            case R.id.selecttruckframe:
                startActivityForResult(new Intent(getActivity(), LoadActivity.class), 1);
                break;
            case R.id.submitbutton:
                sendPostTruck();
                break;
        }
    }

    public void changeImage(ImageView iv, int src) {
        iv.setImageResource(src);
    }

    public void startCityActivity(int flag) {
        Intent i = new Intent(getActivity(), CityActivity.class);
        startActivityForResult(i, flag);
    }

    public void sendPostTruck() {
        if (validate()) {
            pta = new PostTruckAsync();
            pta.setUrl(insertLoadUrl);
            pta.setCommonInterface(this);
            pta.setFormEncodingBuilder(getBuilder());
            status.setVisibility(View.VISIBLE);
            truckLoader.setVisibility(View.VISIBLE);
            loaderframe.setVisibility(View.VISIBLE);
            pta.execute();
        }
    }

    public FormBody.Builder getBuilder() {
        FormBody.Builder FEB = new FormBody.Builder();
        PostLoadBean ptb = getPostTruckBean();
        PostLoadBean plb = new PostLoadBean();
        PostLoadBean.setPlb(ptb);//it means current post truck
        FEB.add(plb.getLOADCATEGORY(), ptb.getLOADCATEGORY());
        FEB.add(FROMCITY, ptb.getFROMCITY());
        FEB.add(TOCITY, ptb.getTOCITY());
        FEB.add(PICKUPDATE, ptb.getPICKUPDATE());
        FEB.add(plb.getLOADDISCRIPTION(), ptb.getLOADDISCRIPTION());
        Log.d("ftlltl", ptb.getFTLANDLTL());
        FEB.add(plb.getFTLANDLTL(), ptb.getFTLANDLTL());
        FEB.add(plb.getWEIGHT(), ptb.getWEIGHT() + "" + dimen);
        FEB.add(REMARK, ptb.getREMARK());
        FEB.add(SPID, User.getInstance().getID());
        FEB.add(STATUS, "0");
        return FEB;
    }

    public PostLoadBean getPostTruckBean() {
        PostLoadBean ptb = new PostLoadBean();
        ptb.setLOADCATEGORY(selecttruck.getText().toString());
        ptb.setFROMCITY(pickupcitytext.getText().toString());
        ptb.setTOCITY(deliverycitytext.getText().toString());
        ptb.setFTLANDLTL(FTLLTL);
        ptb.setPICKUPDATE(datetext.getText().toString());
        ptb.setWEIGHT(capacity.getText().toString());
        ptb.setLOADDISCRIPTION(freight.getText().toString());
        ptb.setSTATUS("0");
        ptb.setREMARK(remark.getText().toString());
        return ptb;
    }


    public boolean validate() {
        if (TextUtils.isEmpty(selecttruck.getText().toString()) || selecttruck.getText().toString().contains("Select Load Category")) {
            selecttruck.setError("Select Load Category");
            selecttruck.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(pickupcitytext.getText().toString())) {
            pickupcitytext.setError("Select Pick Up City");
            pickupcitytext.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(deliverycitytext.getText().toString())) {
            deliverycitytext.setError("Select delivery City ");
            deliverycitytext.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(datetext.getText().toString())) {
            datetext.setError("Select your moving date");
            datetext.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(capacity.getText().toString())) {
            capacity.setError("Enter Weight in tons");
            capacity.requestFocus();
            return false;
        }
        capacity.setError(null);
        freight.setError(null);
        return true;
    }


    public interface OnPostFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPostInteraction(String uri);
    }

}
