package logistic.compare.comparelogistic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wt.calendarcard.CalendarCard;
import com.wt.calendarcard.CalendarCardPager;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

import java.util.Calendar;

public class CalenderPicker extends AppCompatActivity {
    CalendarCardPager calendarCard;
    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarCard = (CalendarCardPager) findViewById(R.id.calendarCard);
        calendarCard.setOnCellItemClick(new OnCellItemClick() {
            @Override
            public void onCellClick(View v, CardGridItem item) {
                sendDataBack(item.getDate());
            }
        });
    }

    public void sendDataBack(Calendar data) {
        String date = getDate(data);
        Log.d("DateTranslated", date);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("date", date);
        setResult(2, i);
        finish();
    }

    public static String getDate(Calendar data) {
        int day = data.get(data.DAY_OF_MONTH);
        String daySuffix = day + suffixes[day];
        String month = getMonth(data.get(data.MONTH) + 1);
        int year = data.get(data.YEAR);
        return daySuffix + " " + month + " " + year;
    }

    public static String getMonth(int month) {
        String monthstring = "false";
        switch (month) {
            case 1:
                monthstring = "January";
                break;
            case 2:
                monthstring = "February";
                break;
            case 3:
                monthstring = "March";
                break;
            case 4:
                monthstring = "April";
                break;
            case 5:
                monthstring = "May";
                break;
            case 6:
                monthstring = "June";
                break;
            case 7:
                monthstring = "July";
                break;
            case 8:
                monthstring = "August";
                break;
            case 9:
                monthstring = "September";
                break;
            case 10:
                monthstring = "October";
                break;
            case 11:
                monthstring = "November";
                break;
            case 12:
                monthstring = "December";
                break;
        }

        return monthstring;
    }

}
