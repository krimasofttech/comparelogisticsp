package logistic.compare.comparelogistic.Search;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import logistic.compare.comparelogistic.CalenderPicker;
import logistic.compare.comparelogistic.R;

/**
 * Created by Sony on 5/27/2016.
 */
public class FilterLoadFragment extends BottomSheetDialogFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    TextView truckselect, pickupcity, deliverycity, selectdate;
    CheckBox ftl, ltl;
    Button submit;
    ImageView cancel;
    String truck, pick, dest, date, ftltext, ltltext;
    FilterLoadFragmentInterface myinterface;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    public FilterLoadFragment() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_filter_load, null);
        dialog.setContentView(contentView);
        getParameters();
        initComponent(contentView);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        selectdate.setOnClickListener(this);
        ftl.setOnCheckedChangeListener(this);
        ltl.setOnCheckedChangeListener(this);
        setParametersData();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }

    public void initComponent(View contentView) {
        selectdate = (TextView) contentView.findViewById(R.id.datetext);
        // truckselect = (TextView) contentView.findViewById(R.id.selecttruck);
        //pickupcity = (TextView) contentView.findViewById(R.id.pickupcitytext);
        //deliverycity = (TextView) contentView.findViewById(R.id.deliverycity);
        submit = (Button) contentView.findViewById(R.id.submitbutton);
        cancel = (ImageView) contentView.findViewById(R.id.canceldate);
        ftl = (CheckBox) contentView.findViewById(R.id.ftlcheckbox);
        ltl = (CheckBox) contentView.findViewById(R.id.ltlcheckbox);
    }

    public void sendDataToBack(Object str) {
        this.dismiss();
        if (myinterface != null) {
            myinterface.ReceiveFilterData(str);
        }
    }

    //input data from SearchActivity
    public void setParametersData() {
        FilterLoad f = FilterLoad.getFilterLoad();
        /*truckselect.setText(truck);
        pickupcity.setText(pick);
        deliverycity.setText(dest);*/
        if (f.isFtl()) {
            ftl.setChecked(true);
        } else {
            ftl.setChecked(false);
        }
        if (f.isLtl()) {
            ltl.setChecked(true);
        } else {
            ltl.setChecked(false);
        }
        if (!f.getDate().contains("false") && f.getDate() != null) {
            selectdate.setText(f.getDate());
        }
    }


    public void getParameters() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("truck")) {
                if (!TextUtils.isEmpty(bundle.get("truck").toString())) {
                    truck = bundle.get("truck").toString();
                }
            }
            if (bundle.containsKey("pick")) {
                if (!TextUtils.isEmpty(bundle.get("pick").toString())) {
                    pick = bundle.get("pick").toString();
                }
            }
            if (bundle.containsKey("dest")) {
                if (!TextUtils.isEmpty(bundle.get("dest").toString())) {
                    dest = bundle.get("dest").toString();
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.ftlcheckbox:
                FilterLoad.getFilterLoad().setFtl(isChecked);
                break;
            case R.id.ltlcheckbox:
                FilterLoad.getFilterLoad().setLtl(isChecked);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data.hasExtra("date")) {
                String date = data.getStringExtra("date");
                selectdate.setText(date);
                FilterLoad.getFilterLoad().setDate(date);
            }
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        myinterface = (FilterLoadFragmentInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myinterface = null;
    }

    public interface FilterLoadFragmentInterface {
        public void ReceiveFilterData(Object data);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submitbutton:
                sendDataToBack("false");
                break;
            case R.id.datetext:
                startActivityForResult(new Intent(getActivity(), CalenderPicker.class), 2);
                break;
            case R.id.canceldate:
                selectdate.setText("Tap To Select Moving Date");
                FilterLoad.getFilterLoad().setDate("false");
                break;
        }
    }

}
