package logistic.compare.comparelogistic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.User;
import logistic.compare.comparelogistic.Load.PostLoadBean;
import logistic.compare.comparelogistic.verification.OtpBackground;

public class OTPVerification extends Fragment implements CommonUtility, CommonInterface {
    public String mobileNo = "false";
    private OnFragmentInteractionListener mListener;
    OtpBackground async;
    String response;
    ProgressBar pb;
    Button b;
    Handler handler;
    DB db;
    EditText textField;
    MyOtpCheck myOtpCheck;


    // TODO: Rename and change types and number of parameters
    public static OTPVerification newInstance(String param1, String param2) {
        OTPVerification fragment = new OTPVerification();
        // fragment.setArguments(args);
        return fragment;
    }

    public void createHandler() {

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                if (msg.arg1 == 1) {

                    User u = (User) msg.obj;

                    startOTPScreen();

                } else if (msg.arg1 == 2) {
                    // stopLoader();

                    textField.setEnabled(false);
                    //msg.arg2==1 then User is already verified
                } else if (msg.arg2 == 1) {
                    stopLoader();
                    Toast.makeText(getActivity(), "User Already Verified", Toast.LENGTH_SHORT).show();
                    //Enable Submit Button
                    b.setEnabled(true);
                    //msg.arg2 == 2 then User not Exist.
                } else if (msg.arg2 == 2) {
                    stopLoader();
                    Toast.makeText(getActivity(), "User not Exist", Toast.LENGTH_SHORT).show();
                    //Enable Submit Button..
                    b.setEnabled(true);

                }

            }
        };
    }

    public OTPVerification() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        try {
            db = new DB(getActivity());
            createHandler();
            view = inflater.inflate(R.layout.fragment_otpverification, container, false);
            pb = (ProgressBar) view.findViewById(R.id.loader);
            textField = (EditText) view.findViewById(R.id.mobileNumber);
            b = (Button) view.findViewById(R.id.submitOtp);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mobileNo = textField.getText().toString();
                    if (validateTextView(textField)) {
                        startVerification(mobileNo);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view = inflater.inflate(R.layout.fragment_otpverification, container, false);
        }
        return view;
    }

    //Stop Loading Progress Bar
    public void stopLoader() {
        pb.setVisibility(View.INVISIBLE);
    }

    //Start OTP Screen where user enter its otp no.
    private void startOTPScreen() {
        mListener.onOtpFragmentInteraction();
    }

    //This will start verification process from loader to send message.
    public void startVerification(String mobile) {
        try {

            if (pb != null) {
                pb.setVisibility(View.VISIBLE);
                pb.getIndeterminateDrawable().setColorFilter(
                        getResources().getColor(R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN);

                b.setEnabled(false);
            }

            String otpurl = url + "/getTesting" + "/" + mobile;

            if (new ConnectionUtility(getContext()).isConnectingToInternet()) { //Check Internet is enabled or not.
                try {
                    //   async = new OtpBackground(otpurl, mobile);
                    myOtpCheck = new MyOtpCheck(otpurl, mobile);
                    myOtpCheck.setCommonInterface(this);
                    //  myOtpCheck.otpVerificationFragment = this;
                    myOtpCheck.SendMobNoToServer();
                    // createUser(response);
                    Log.d("response", response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUser(String response) {
        Message msg = new Message();
        try {
            b.setEnabled(false);
            JSONObject obj;
            User u = new User();
            if (!response.equals("false") && response.contains("userid") && response.contains("otp")) {
                obj = new JSONObject(response);
                Log.d("data", obj.getString("userid") + obj.getString("otp"));

                User.getInstance().setID(obj.getString("userid"));

                User.getInstance().setOtpNo(obj.getString("otp"));

                User.getInstance().setIsOtpSend("true");

                User.getInstance().setIsOtpVerified("false");

                User.getInstance().setMobileno(mobileNo);

                db.open();

                String status = db.isUserExist();

                db.close();

                if (status.equals("true")) {

                    db.open();

                    db.UpdateUser(User.getInstance());

                    db.close();
                    PostLoadBean plb = new PostLoadBean();
                    Intent i = new Intent();
                    i.putExtra("key", plb);

                } else if (status.equals("false")) {

                    db.open();

                    db.createUser(User.getInstance());

                    db.close();
                }


                msg.obj = u;

                msg.arg1 = 1;

                handler.sendMessage(msg);
            } else if (response.contains("averified") && response.contains("true")) {

                msg.arg2 = 1;

                handler.sendMessage(msg);

            } else if (response.contains("notexist") && response.contains("true")) {

                msg.arg2 = 2;

                handler.sendMessage(msg);

            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public boolean validateTextView(EditText textView) {
        String data = textView.getText().toString();
        if (data.length() == 0 || data.length() != 10) {

            Toast.makeText(getActivity(), "Mobile number should be 10 digit.", Toast.LENGTH_SHORT).show();

            textView.setText(null);

            return false;

        } else if (data.contains("[a-zA-Z]")) {

            Toast.makeText(getActivity(), "Please enter mobile number only", Toast.LENGTH_SHORT).show();

            textView.setText(null);

            return false;
        } else if (!new ConnectionUtility(getActivity()).isConnectingToInternet()) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        } else {
            mobileNo = textView.getText().toString();  //All should be right then save mobile no.
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
        createUser(enquiry.toString());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onOtpFragmentInteraction();
    }

}
