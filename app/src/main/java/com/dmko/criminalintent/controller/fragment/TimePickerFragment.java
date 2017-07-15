package com.dmko.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dmko.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.dmko.criminalintent.time";
    public static final String ARG_TIME = "time";
    private TimePicker mTimePicker;
    private Button mOkButton;
    private TextView mTitleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time, null);

        mTitleTextView = (TextView) view.findViewById(R.id.dialog_title_text_view);
        mTitleTextView.setText(R.string.time_picker_title);

        mTimePicker = (TimePicker) view.findViewById(R.id.crime_time_picker);
        mTimePicker.setIs24HourView(true);

        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        mOkButton = (Button) view.findViewById(R.id.dialog_ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                Date time = calendar.getTime();
                sendResult(Activity.RESULT_OK, time);
                dismiss();
            }
        });
        return view;
    }

    public static TimePickerFragment newInstance(Date time) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, time);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode, Date time) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        } else {
            getActivity().setResult(resultCode, intent);
            getActivity().finish();
        }
    }
}
