package logistic.compare.comparelogistic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import logistic.compare.comparelogistic.verification.OtpScreen;

public class VerificationActivity extends AppCompatActivity implements OTPVerification.OnFragmentInteractionListener, OtpScreen.OTPScreenListener {

    OTPVerification otpVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        otpVerification = new OTPVerification();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, otpVerification).addToBackStack("otp").commit();
    }

    @Override
    public void onOtpFragmentInteraction() {
        OtpScreen otpscreen = new OtpScreen();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, otpscreen).addToBackStack("otpscreen").commit();
    }

    @Override
    public void onOTPScreenFragmentInteraction() {
        this.finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        }
    }
}
