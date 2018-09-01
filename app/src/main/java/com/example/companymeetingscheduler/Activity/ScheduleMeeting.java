package com.example.companymeetingscheduler.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.companymeetingscheduler.R;

/**
 * Screen that lets the user to schedule a meeting
 * Created by Nitin on 01/09/18.
 */
public class ScheduleMeeting extends AppCompatActivity {

    public static final String INTENT_DATA_DATE = "date";

    private EditText date;
    private EditText startTime;
    private EditText endTime;
    private EditText desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        date = findViewById(R.id.date);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        desc = findViewById(R.id.description);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            date.setText(bundle.getString(INTENT_DATA_DATE));
        }

        makeEditTextImutable(date);
        makeEditTextImutable(startTime);
        makeEditTextImutable(endTime);

        date.setClickable(false);


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                break;
        }

        return true;
    }

    private void makeEditTextImutable(EditText editText) {

        editText.setFocusable(false);
        editText.setClickable(true);
    }

}
