package com.dmko.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.dmko.criminalintent.controller.fragment.TimePickerFragment;
import java.util.Date;

public class TimePickerActivity extends SingleFragmentActivity {
    private static final String EXTRA_TIME = "com.dmko.criminalintent.crime_time";
    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_TIME);
        return TimePickerFragment.newInstance(date);
    }
    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, TimePickerActivity.class);
        intent.putExtra(EXTRA_TIME, date);
        return intent;
    }
}
