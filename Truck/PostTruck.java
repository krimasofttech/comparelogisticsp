package logistic.compare.comparelogistic.Truck;

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

public class PostTruck extends Fragment implements View.OnClickListener, CommonUtility, CommonInterface {
    private OnFragmentInteractionListener mListener;
    View rootView;
    LinearLayout pickuplayout, deliverylayout, ftllinear, ltllinear;
    TextView pickupcitytext, deliverycitytext;
    CardView onewaycard, roundtrip;
    ImageView oneway, roundtripview, FTL, LTL;
    TextView ftltext, ltltext, datetext, selecttruck, kg, ton, kg1, ton1;
    FrameLayout movingdateframe, truckframe;
    EditText capacity, freight, remark;
    Button submit;
    String trucway = "";
    RelativeLayout root;
    DB db;
    FrameLayout loaderframe;
    PostTruckAsync pta;
    String FTLLTL = "0";
    String dimen = "KG";
    String freightDimen = "KG";
    TruckLoader truckLoader;
    TextView status;

    public PostTruck() {
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
            rootView = inflater.inflate(R.layout.fragment_post_truck, container, false);
        }
        initComponent(rootView);
        return rootView;
    }

    public void initComponent(View rootView) {
        selecttruck = (TextView) rootView.findViewById(R.id.selecttruck);
        root = (RelativeLayout) rootView.findViewById(R.id.root);
        kg = (TextView) rootView.findViewById(R.id.kg);
        kg1 = (TextView) rootView.findViewById(R.id.kg1);
        truckLoader = (TruckLoader) rootView.findViewById(R.id.truckloader);
        status = (TextView) rootView.findViewById(R.id.status);
        ton1 = (TextView) rootView.findViewById(R.id.ton1);
        kg.setBackgroundResource(R.drawable.dimenbackground2);
        kg1.setBackgroundResource(R.drawable.dimenbackground2);
        ton = (TextView) rootView.findViewById(R.id.ton);
        loaderframe = (FrameLayout) rootView.findViewById(R.id.loaderframe);
        truckframe = (FrameLayout) rootView.findViewById(R.id.selecttruckframe);
        pickuplayout = (LinearLayout) rootView.findViewById(R.id.pickuplayout);
        deliverylayout = (LinearLayout) rootView.findViewById(R.id.deliverylayout);
        pickupcitytext = (TextView) rootView.findViewById(R.id.pickupcitytext);
        onewaycard = (CardView) rootView.findViewById(R.id.onewaycard);
        submit = (Button) rootView.findViewById(R.id.submitbutton);
        truckLoader.setDestX(submit.getWidth());
        truckLoader.invalidate();
        roundtrip = (CardView) rootView.findViewById(R.id.roundwaycard);
        deliverycitytext = (TextView) rootView.findViewById(R.id.deliverycity);
        oneway = (ImageView) rootView.findViewById(R.id.oneway);
        roundtripview = (ImageView) rootView.findViewById(R.id.rounttrip);
        FTL = (ImageView) rootView.findViewById(R.id.FTL);
        LTL = (ImageView) rootView.findViewById(R.id.LTL);
        ftllinear = (LinearLayout) rootView.findViewById(R.id.ftllinear);
        ltllinear = (LinearLayout) rootView.findViewById(R.id.ltllinear);
        ftltext = (TextView) rootView.findViewById(R.id.ftltext);
        ltltext = (TextView) rootView.findViewById(R.id.ltltext);
        movingdateframe = (FrameLayout) rootView.findViewById(R.id.movingdateframe);
        datetext = (TextView) rootView.findViewById(R.id.datetext);
        capacity = (EditText) rootView.findViewById(R.id.capacity);
        remark = (EditText) rootView.findViewById(R.id.remark);
        freight = (EditText) rootView.findViewById(R.id.freight);
        clickEvent();
        datetext.setText(getCurrentDate());
        pta = new PostTruckAsync();
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return CalenderPicker.getDate(calendar);
    }


    public void clickEvent() {
        truckframe.setOnClickListener(this);
        ftllinear.setOnClickListener(this);
        ltllinear.setOnClickListener(this);
        kg.setOnClickListener(this);
        ton.setOnClickListener(this);
        submit.setOnClickListener(this);
        onewaycard.setOnClickListener(this);
        kg1.setOnClickListener(this);
        ton1.setOnClickListener(this);
        roundtrip.setOnClickListener(this);
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

    public void changeImage(ImageView iv, int src) {
        iv.setImageResource(src);
    }

    public void startCityActivity(int flag) {
        Intent i = new Intent(getActivity(), CityActivity.class);
        startActivityForResult(i, flag);
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
                    pickupcitytext.setFocusable(false);
                    pickupcitytext.setFocusableInTouchMode(false);
                } else if (requestCode == 1) {
                    deliverycitytext.setText(city);
                    deliverycitytext.setError(null);
                }
            } else if (data.hasExtra("date")) {
                String date = data.getStringExtra("date");
                Log.d("dateReceived", date);
                datetext.setText(date);
                datetext.setError(null);
            } else if (data.hasExtra("truck")) {
                String truck = data.getStringExtra("truck");
                selecttruck.setText("  " + truck);
                selecttruck.setError(null);
            }
        }
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

    public void sendPostTruck() {
        if (validate()) {
            pta = new PostTruckAsync();
            pta.setUrl(posttruckurl);
            pta.setCommonInterface(this);
            pta.setFormEncodingBuilder(getBuilder());
            //loaderframe.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            truckLoader.setVisibility(View.VISIBLE);
            submit.setVisibility(View.INVISIBLE);
//            pta.execute();
        }
    }

    public FormBody.Builder getBuilder() {
        FormBody.Builder FEB = new FormBody.Builder();
        PostTruckBean ptb = getPostTruckBean();
        PostTruckBean.setPtb(ptb);//it means current post truck
        FEB.add(CAPACITY, ptb.getCAPACITY() + " " + freightDimen);
        FEB.add(FROMCITY, ptb.getFROMCITY());
        FEB.add(TOCITY, ptb.getTOCITY());
        FEB.add(PICKUPDATE, ptb.getPICKUPDATE());
        Log.d("MYPICKUPDATE", ptb.getPICKUPDATE());
        FEB.add(TYPEOFVEHICLE, ptb.getTYPEOFVEHICLE());
        Log.d("ftlltl", ptb.getFTLLTL());
        FEB.add("FTLLTL", ptb.getFTLLTL());
        FEB.add(FREIGHT, ptb.getFREIGHT() + " " + dimen);
        FEB.add(REMARK, ptb.getREMARK());
        FEB.add(SPID, User.getInstance().getID());
        FEB.add(STATUS, "0");
        Log.d("WAY", ptb.getWAY());
        FEB.add(WAY, ptb.getWAY());
        return FEB;
    }

    public PostTruckBean getPostTruckBean() {
        PostTruckBean ptb = new PostTruckBean();
        ptb.setTYPEOFVEHICLE(selecttruck.getText().toString());
        ptb.setFROMCITY(pickupcitytext.getText().toString());
        ptb.setTOCITY(deliverycitytext.getText().toString());
        ptb.setWAY(trucway);
        ptb.setFTLLTL(FTLLTL);
        ptb.setPICKUPDATE(datetext.getText().toString());
        ptb.setCAPACITY(capacity.getText().toString());
        ptb.setFREIGHT(freight.getText().toString());
        ptb.setSTATUS("0");
        ptb.setREMARK(remark.getText().toString());
        return ptb;
    }


    public boolean validate() {
        if (TextUtils.isEmpty(selecttruck.getText().toString()) || selecttruck.getText().toString().contains("Select Your Truck")) {
            selecttruck.setError("Select your truck");
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
        } else if (trucway.equals("")) {
            Snackbar.make(root, "Select Either One way or Round Trip", Snackbar.LENGTH_SHORT).show();
            onewaycard.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(datetext.getText().toString())) {
            datetext.setError("Select your moving date");
            datetext.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(capacity.getText().toString())) {
            capacity.setError("Enter Capacity in tons");
            capacity.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(freight.getText().toString())) {
            freight.setError("Enter Freight in tons");
            freight.requestFocus();
            return false;
        }
        capacity.setError(null);
        freight.setError(null);
        return true;
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
                ftltext.setTextColor(getActivity().getResources().getColor(R.color.green));
                ltltext.setTextColor(getActivity().getResources().getColor(R.color.grey));
                FTLLTL = "0";
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
                startActivityForResult(new Intent(getActivity(), TruckActivity.class), 3);
                break;
            case R.id.submitbutton:
                sendPostTruck();
                break;
            case R.id.kg:
                kg.setBackgroundResource(R.drawable.dimenbackground2);
                ton.setBackgroundResource(R.drawable.dimenbackground);
                dimen = kg.getText().toString();
                break;
            case R.id.ton:
                kg.setBackgroundResource(R.drawable.dimenbackground);
                ton.setBackgroundResource(R.drawable.dimenbackground2);
                dimen = ton.getText().toString();
                break;
            case R.id.kg1:
                kg1.setBackgroundResource(R.drawable.dimenbackground2);
                ton1.setBackgroundResource(R.drawable.dimenbackground);
                freightDimen = kg1.getText().toString();
                break;
            case R.id.ton1:
                kg1.setBackgroundResource(R.drawable.dimenbackground);
                ton1.setBackgroundResource(R.drawable.dimenbackground2);
                freightDimen = ton.getText().toString();
                break;
        }
    }

    @Override
    public void RefreshEnquiry(String status, int maxid) {

    }

    @Override
    public void ReceiveEnquiry(Object enquiry) {
        submit.setVisibility(View.VISIBLE);
        truckLoader.setVisibility(View.INVISIBLE);
        status.setVisibility(View.VISIBLE);
        try {
            PostTruckBean.getPtb().setID(new JSONObject(enquiry.toString()).getString("ID"));
            if (insertPostTruck(PostTruckBean.getPtb())) {
                loaderframe.setVisibility(View.INVISIBLE);

                Snackbar.make(root, "Enjoy Service,Your Truck has been Posted", Snackbar.LENGTH_LONG).show();
                submit.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(root, "Something went wrong ,try after some time", Snackbar.LENGTH_SHORT).show();
        }
    }

    public boolean insertPostTruck(PostTruckBean ptb) {
        if (db == null) {
            db = new DB(getActivity());
        }
        db.open();
        long w = db.createPostTruck(ptb);
        db.close();
        if (w > 0) {
            return true;
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
