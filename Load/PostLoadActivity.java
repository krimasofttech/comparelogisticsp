package logistic.compare.comparelogistic.Load;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import logistic.compare.comparelogistic.R;

public class PostLoadActivity extends AppCompatActivity implements PostLoad.OnPostFragmentInteractionListener {
    PostLoad postLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load);
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
        postLoad = new PostLoad();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, postLoad)
                .addToBackStack("posttruck")
                .commit();
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

    @Override
    public void onPostInteraction(String uri) {

    }
}


