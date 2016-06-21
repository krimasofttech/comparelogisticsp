package logistic.compare.comparelogistic.Profile;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import logistic.compare.comparelogistic.Profile.load.MyLoadPost;
import logistic.compare.comparelogistic.Profile.load.MyViewLoadFragment;
import logistic.compare.comparelogistic.Profile.truck.MyTruckPost;
import logistic.compare.comparelogistic.R;

public class MyPostActivity extends AppCompatActivity implements MyTruckPost.MyTruckPostListener, MyLoadPost.MyLoadPostListener, MyViewLoadFragment.OnFragmentInteractionListener {
    MyTruckPost myTruckPost;
    MyLoadPost myLoadPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myTruckPost = new MyTruckPost();
        myLoadPost = new MyLoadPost();
        if (getIntent().hasExtra("truck")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myTruckPost).addToBackStack("truck").commit();
        } else if (getIntent().hasExtra("load")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myLoadPost).addToBackStack("load").commit();
        }
    }

    @Override
    public void onFragmentInteraction(String uri) {
        getSupportActionBar().setTitle(uri);
        getSupportActionBar().setIcon(R.drawable.cross);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onMyLoadPostInteraction(String uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
