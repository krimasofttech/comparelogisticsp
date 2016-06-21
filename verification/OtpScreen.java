package logistic.compare.comparelogistic.verification;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import logistic.compare.comparelogistic.CommonUtility;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.R;

public class OtpScreen extends android.support.v4.app.Fragment implements CommonUtility {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OTPScreenListener mListener;
    private EditText otpnumber;
    private Button otpsend;
    private ProgressBar loader;
    private String otpno;
    private OkHttpExecute okhttpexecute;
    private Handler handler;
    private DB db;

    public OtpScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void startHandler() {
        handler = new android.os.Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 1) {
                    //This will register user on to google server.
                    // register.registerApp();
                    // register.saveToServer();
                    stopLoader();

                    loadHomeFragment();

                } else if (msg.arg1 == 2) {

                    stopLoader();

                    Toast.makeText(getActivity(), "OTP is UNAUTHORIZED", Toast.LENGTH_LONG).show();

                } else if (msg.arg1 == 0) {

                    Toast.makeText(getActivity(), "Server Down", Toast.LENGTH_SHORT).show();

                }

            }
        };

    }

    public void loadHomeFragment() {
        mListener.onOTPScreenFragmentInteraction();
    }

    public void stopLoader() {
        loader.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp_screen, container, false);

        db = new DB(getActivity());
        startHandler();
        // register = new GCMRegister(getActivity(), this);
        //EditText for User
        otpnumber = (EditText) view.findViewById(R.id.otpnumber);
        //Submit Button
        otpsend = (Button) view.findViewById(R.id.otpsend);
        //Loader
        loader = (ProgressBar) view.findViewById(R.id.otpscreenloader);

        okhttpexecute = new OkHttpExecute(getActivity());

        otpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateTextView(otpnumber)) {
                    //Here we will compare otp number from user and otp number from server
                    if (checkOTPVerification(otpnumber.getText().toString())) {

                        startLoader();

                        verfiyToServer();
                    } else {
                        Toast.makeText(getActivity(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    public void verfiyToServer() {
        Thread t = new Thread() {

            public void run() {
                JSONObject obj;
                try {

                    if (User.getInstance().getID() != null) {
                        obj = new JSONObject();
                        obj.put("userid", User.getInstance().getID().toString());

                        obj.put("otp", User.getInstance().getOtpNo());

                        obj.put("verified", "true");

                        okhttpexecute.setUrl(url + "/getVerified");

                        okhttpexecute.setJson(obj.toString());

                        String response = okhttpexecute.execute();

                        JSONObject jobj = new JSONObject(response);


                        if (jobj.getString("verified").equals("true")) {

                            User.getInstance().isOtpVerified = "true";

                            User.getInstance().isOtpSend = "true";

                            db.open();

                            db.UpdateUser(User.getInstance());

                            db.close();

                            Message msg = new Message();

                            msg.arg1 = 1;

                            handler.sendMessage(msg);

                        } else {

                            Message msg = new Message();

                            msg.arg2 = 2;

                            handler.sendMessage(msg);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //    return "false";
                }
            }

        };
        t.start();

    }

    public void startLoader() {
        loader.setVisibility(View.VISIBLE);
    }

    public boolean checkOTPVerification(String otpToVerify) {

        boolean b = false;

        if (User.getInstance().getOtpNo().equals(otpToVerify)) {
            b = true;
        }
        return b;
    }

    public void createChannels() {

    }

    public boolean validateTextView(EditText textView) {
        String data = textView.getText().toString();
        if (data.length() == 0 || data.length() != 4) {

            Toast.makeText(getActivity(), "OTP number must be 4 digit .", Toast.LENGTH_SHORT).show();

            textView.setText(null);

            return false;

        } else if (data.contains("[a-zA-Z]*")) {

            Toast.makeText(getActivity(), "Please enter mobile number only", Toast.LENGTH_SHORT).show();

            textView.setText(null);

            return false;

        } else {
            otpno = textView.getText().toString();  //All should be right then save mobile no.
            return true;
        }
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OTPScreenListener) activity;
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


    public interface OTPScreenListener {
        // TODO: Update argument type and name
        public void onOTPScreenFragmentInteraction();
    }

}
