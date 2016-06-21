package logistic.compare.comparelogistic.Profile.truck;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.R;
import logistic.compare.comparelogistic.Truck.PostTruckBean;

public class MyViewTruckFragment extends Fragment {
    private MyViewTruckFragmentListener mListener;
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
    TextView mTruckView;
    String flag;
    PostTruckAsync pta;
    String FTLLTL = "0";
    boolean b = true;

    public MyViewTruckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ptb = (PostTruckBean) getArguments().getSerializable("truck");
            PostTruckBean.setPtb(ptb);
        }
    }

    public void initComponent(View rootView) {
        selecttruck = (TextView) rootView.findViewById(R.id.selecttruck);
        root = (RelativeLayout) rootView.findViewById(R.id.root);
        mTruckView = (TextView) rootView.findViewById(R.id.viewText);
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
        setWay();
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

    public void setData() {
        if (ptb != null) {
            selecttruck.setText(ptb.getTYPEOFVEHICLE());
            pickupcitytext.setText(ptb.getFROMCITY());
            deliverycitytext.setText(ptb.getTOCITY());
            datetext.setText(ptb.getPICKUPDATE());
            capacity.setText(ptb.getCAPACITY());
            freight.setText(ptb.getFREIGHT());
            remark.setText(ptb.getREMARK());
            mTruckView.setText(ptb.getVIEWCOUNT());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_view_truck, container, false);
        }
        initComponent(rootView);
        setData();
        return rootView;
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (MyViewTruckFragmentListener) activity;
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

    public interface MyViewTruckFragmentListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
