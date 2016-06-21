package logistic.compare.comparelogistic.Truck;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import logistic.compare.comparelogistic.R;

public class PostTruckActivity extends AppCompatActivity implements PostTruck.OnFragmentInteractionListener {

    PostTruck postTruck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_truck);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.cross);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
            }
        });
        postTruck = new PostTruck();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, postTruck)
                .addToBackStack("posttruck")
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
            overridePendingTransition(R.anim.up_from_bottom, R.anim.down_from_top);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

}
