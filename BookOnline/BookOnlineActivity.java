package logistic.compare.comparelogistic.BookOnline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import logistic.compare.comparelogistic.R;

public class BookOnlineActivity extends AppCompatActivity {
    CardView header, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookonline);
        header = (CardView) findViewById(R.id.cardview);
        bottom = (CardView) findViewById(R.id.cardview2);
    }
}
