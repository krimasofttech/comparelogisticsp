package logistic.compare.comparelogistic.gcm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import logistic.compare.comparelogistic.AsyncTask.EnquiryQuote;
import logistic.compare.comparelogistic.Database.DB;
import logistic.compare.comparelogistic.Database.EnquiryColumns;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 11/4/2015.
 */
public class dialog {
    ImageButton ackok;//AcknowledgeButton
    Dialog d;
    TextView close;
    EditText rate, transitdays, validity, remark, remarks;
    Button submit;
    ProgressBar loader;
    TextView quoteIcon;
    Context context;
    DatePickerDialog fromDatePickerDialog;
    public EnquiryQuote.CommunicateToEnquiryQuote ef;
    EnquiryColumns ec;
    DB db;

    public dialog(Context context, EnquiryQuote.CommunicateToEnquiryQuote ef, EnquiryColumns ec) {
        this.context = context;
        this.ec = ec;
        this.ef = ef;
        db = new DB(context);
    }

    public dialog() {
        //Empty Constructor
    }

    public Dialog getDialog() {
        if (d == null) {
            d = new android.app.Dialog(new ContextThemeWrapper(context, R.style.DialogSlideAnim));
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialoglayout);
            rate = (EditText) d.findViewById(R.id.rate);
            transitdays = (EditText) d.findViewById(R.id.transitdays);
            close = (TextView) d.findViewById(R.id.close);
            validity = (EditText) d.findViewById(R.id.quotevalidity);
            //   remarks = (EditText) d.findViewById(R.id.remarks);
            submit = (Button) d.findViewById(R.id.submitrate);
            loader = (ProgressBar) d.findViewById(R.id.quoteloader);
            validity.setInputType(InputType.TYPE_NULL);
            remark = (EditText) d.findViewById(R.id.remark);
            quoteIcon = (TextView) d.findViewById(R.id.quoteCorrecticon);
            loader = (ProgressBar) d.findViewById(R.id.quoteloader);
            ackok = (ImageButton) d.findViewById(R.id.ackok);
            ackok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ef.sendAck("close", "2");
                }
            });
            validity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (startValidation(rate, transitdays, validity)) {
                        if (d != null) {
                            d.setCanceledOnTouchOutside(false);
                        }
                        hideButton();
                        sendQuoteToServer();//Below mentioned.
                    }
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
        }
        return d;
    }

    public void sendQuoteToServer() {
        Log.d("ENCID", ec.getID());
        //here we collect all the data
        String rate = null, transitdays = null, validity = null, remark = null;
        try {
            if (this.rate != null) {
                rate = this.rate.getText().toString(); // get & set Rate
                this.ec.setPRICE(rate);
            }
            if (this.transitdays != null) {
                transitdays = this.transitdays.getText().toString(); // get & set Transit Days
                this.ec.setDAYS(transitdays);
            }
            if (this.validity != null) {
                validity = this.validity.getText().toString(); // get & set Validity
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                validity = new SimpleDateFormat("dd/MM/yyyy").format(format.parse(validity));
                this.ec.setVALIDITY(validity);
            }
            if (this.remark != null) {
                remark = this.remark.getText().toString(); // get & set Remark
                Log.d("Remark", remark);
                this.ec.setREMARK(remark);
            }
            this.ec.setSPSTATUS("2");
            //quoted = getQuotedObject(remark, validity, rate, transitdays);
            //SendQuote(quoted, "2", "all");//Send Quote to Server as well as update to database.
            SendQuote(this.ec);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //send this quote to server to update and call Ack() to response back.
    public void SendQuote(EnquiryColumns ec) {
        EnquiryQuote quote = new EnquiryQuote(ef, context, ec);
        quote.SendQuote();//
    }


    public void close() {
        Log.d("Close Fired", "True");
        if (d != null) {
            d.dismiss();
        }
    }

    //Hide Button and Visible loader
    public void hideButton() {
        if (submit != null) {
            submit.setVisibility(View.INVISIBLE);
        }
        if (loader != null) {
            loader.setVisibility(View.VISIBLE);
        }
    }

    public void showGreeting() {
        if (loader != null) {
            loader.setVisibility(View.INVISIBLE);
        }
        if (quoteIcon != null) {
            quoteIcon.setVisibility(View.VISIBLE);
        }
        //   greetMessage.setVisibility(View.VISIBLE);
        if (ackok != null) {
            ackok.setVisibility(View.VISIBLE);
        }
    }

    public void showButton() {
        if (submit != null) {
            submit.setVisibility(View.VISIBLE);
        }
        if (loader != null) {
            loader.setVisibility(View.INVISIBLE);
        }
        hideGreeting();
    }

    public void hideGreeting() {
        if (quoteIcon != null) {
            quoteIcon.setVisibility(View.INVISIBLE);
        }
        if (ackok != null) {
            //  greetMessage.setVisibility(View.INVISIBLE);
            ackok.setVisibility(View.INVISIBLE);
        }
    }

    public void updateEnquiry(String status) {
        this.ec.setSPSTATUS(status);
        //  EnquiryColumns.setEnquiryColumns(this.ec);
        if (db != null) {
            db.open();
            db.updateEnquiry(this.ec);
            db.close();
            if (d != null)
                d.setCanceledOnTouchOutside(true);
        }
    }

    public boolean startValidation(EditText rate, EditText transitdays, EditText validity) {
        String rat = rate.getText().toString();
        String transitDays = transitdays.getText().toString();
        String validit = validity.getText().toString();
        if (nullValidate(rat)) {
            Toast.makeText(this.context, "Enter Your Rate", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nullValidate(transitDays)) {
            Toast.makeText(this.context, "Enter Transit Days", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nullValidate(validit)) {
            Toast.makeText(this.context, "Enter Quote Validity", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean nullValidate(String data) {
        if (TextUtils.isEmpty(data)) {
            return true;
        }
        return false;
    }


    public void showDatePickerDialog() {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this.context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (validity != null) {
                    validity.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
}
