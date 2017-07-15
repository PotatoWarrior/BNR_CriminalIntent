package com.dmko.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dmko.criminalintent.controller.fragment.DatePickerFragment;

import java.util.Date;

public class DatePickerActivity extends SingleFragmentActivity {
    private static final String EXTRA_DATE = "com.dmko.criminalintent.crime_date";
    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_DATE);
        return DatePickerFragment.newInstance(date);
    }
    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }
}
