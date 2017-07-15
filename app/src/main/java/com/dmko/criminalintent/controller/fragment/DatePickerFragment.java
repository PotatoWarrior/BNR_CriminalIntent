package com.dmko.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dmko.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.dmko.criminalintent.date";
    private DatePicker mDatePicker;
    private Button mOkButton;
    private TextView mTitleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date, container, false);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mOkButton = (Button) view.findViewById(R.id.dialog_ok_button);
        mTitleTextView = (TextView) view.findViewById(R.id.dialog_title_text_view);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        final Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, null);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, mDatePicker.getYear());
                calendar.set(Calendar.MONTH, mDatePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
                Date date = calendar.getTime();
                sendResult(Activity.RESULT_OK, date);
                dismiss();
            }
        });

        mTitleTextView.setText(R.string.date_picker_title);
        return view;
    }

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode, Date date) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        } else {
            getActivity().setResult(resultCode, intent);
            getActivity().finish();
        }
    }
}
