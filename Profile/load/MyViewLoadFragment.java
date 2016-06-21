package logistic.compare.comparelogistic.Profile.load;

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

import logistic.compare.comparelogistic.AsyncTask.PostTruckAsync;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.R;

public class MyViewLoadFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
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
    TextView mTruckView;

    public MyViewLoadFragment() {
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
        mTruckView = (TextView) rootView.findViewById(R.id.viewText);
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
            mTruckView.setText(ptb.getVIEWCOUNT());
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
            rootView = inflater.inflate(R.layout.fragment_my_view_load, container, false);
        }
        initComponent(rootView);
        setData();
        return rootView;
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
