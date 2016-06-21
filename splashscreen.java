package logistic.compare.comparelogistic;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import logistic.compare.comparelogistic.Database.DB;


public class splashscreen extends AppCompatActivity implements SplashFragment.OnFragmentInteractionListener, LoginFragment.LoginInteractionLisener {
    DB db;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        db = new DB(this);
        db.open();
        status = db.isUserVerified();
        db.close();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent();
                if (!status.equals("false")) {
                    i.setClass(splashscreen.this, MainActivity.class);

                } else {
                    i.setClass(splashscreen.this, VerificationActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 3000);
    }


    public void startActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splashscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new LoginFragment()).commit();
        ///  Toast.makeText(this,"helo",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInteraction() {
        // getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MainActivity.PlaceholderFragment())
        startActivity(new Intent(this, MainActivity.class));
    }
}
