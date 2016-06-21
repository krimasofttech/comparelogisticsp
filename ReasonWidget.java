package logistic.compare.comparelogistic;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;
import logistic.compare.comparelogistic.Database.EnquiryColumns;

/**
 * Created by Sony on 3/15/2016.
 */
public class ReasonWidget implements CommonUtility {
    Context context;
    EnquiryColumns ec;
    Dialog d;
    EditText reason;
    public Button submit;
    public ProgressBar loader;
    EnquiryQuote.CommunicateToEnquiryQuote quoteBinder;
    TextView close;
    LinearLayout rootParent;

    public ReasonWidget() {
    }

    public ReasonWidget(Context context, EnquiryColumns ec, EnquiryQuote.CommunicateToEnquiryQuote commoninterface) {
        this.ec = ec;
        this.context = context;
        this.quoteBinder = commoninterface;
    }

    public Dialog open() {
        d = new Dialog(new ContextThemeWrapper(context, R.style.DialogSlideAnim));
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.reasonlayout);
        initComponent(d);
        clickEvent();
        d.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.getWindow().setAttributes(lp);
        return d;
    }

    public void initComponent(Dialog view) {
        reason = (EditText) view.findViewById(R.id.reason);
        submit = (Button) view.findViewById(R.id.submit);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        close = (TextView) view.findViewById(R.id.close);
        rootParent = (LinearLayout) view.findViewById(R.id.dialogparent);
    }

    public void clickEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    ec.setREMARK(reason.getText().toString());
                    sendCancelQuote();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    //this is called from submit  click listener
    private void sendCancelQuote() {
        submit.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.VISIBLE);
        EnquiryQuote eq = new EnquiryQuote(this.quoteBinder, context, this.ec);
        if (new ConnectionUtility(context).isConnectingToInternet()) {
            EnquiryColumns.setEnquiryColumns(this.ec);
            eq.sendCancelQuote();
        } else {
            loader.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);
            Snackbar.make(rootParent, nointerneterror, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void updateEnquiryToServer() {

    }

    public boolean validate() {
        if (TextUtils.isEmpty(reason.getText().toString())) {
            reason.setError("Please mention reason");
            return false;
        }
        return true;
    }
}
