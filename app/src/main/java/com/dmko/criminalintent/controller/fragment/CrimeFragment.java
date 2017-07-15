package com.dmko.criminalintent.controller.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.dmko.criminalintent.R;
import com.dmko.criminalintent.controller.activity.DatePickerActivity;
import com.dmko.criminalintent.controller.activity.TimePickerActivity;
import com.dmko.criminalintent.model.Crime;
import com.dmko.criminalintent.model.CrimeLab;
import com.dmko.criminalintent.util.ScreenSize;

import java.util.Date;
import java.util.UUID;

import static com.dmko.criminalintent.util.DateTimeFormats.dateFormat;
import static com.dmko.criminalintent.util.DateTimeFormats.dateTimeFormat;
import static com.dmko.criminalintent.util.DateTimeFormats.timeFormat;


public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton, mTimeButton, mReportButton, mSuspectNameButton, mCallSuspect;
    private CheckBox mSolvedCheckBox, mIsPoliceRequiredCheckBox;
    private static final String CRIME_ID_KEY = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID_KEY);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ScreenSize.isScreenBig(getActivity())) {
                    DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                    dialog.show(getFragmentManager(), DIALOG_DATE);
                } else {
                    startActivityForResult(DatePickerActivity.newIntent(getActivity(), mCrime.getDate()), REQUEST_DATE);
                }
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mIsPoliceRequiredCheckBox = (CheckBox) view.findViewById(R.id.is_police_required);
        mIsPoliceRequiredCheckBox.setChecked(mCrime.isPoliceRequired());
        mIsPoliceRequiredCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setPoliceRequired(isChecked);
            }
        });

        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ScreenSize.isScreenBig(getActivity())) {
                    TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    dialog.show(getFragmentManager(), DIALOG_TIME);
                } else {
                    startActivityForResult(TimePickerActivity.newIntent(getActivity(), mCrime.getDate()), REQUEST_TIME);
                }
            }
        });
        updateTime();

        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShareCompat.IntentBuilder.
                        from(getActivity()).
                        setType("text/plain").
                        setText(getReport()).
                        setSubject(getString(R.string.crime_report_subject)).
                        setChooserTitle(getString(R.string.send_report)).
                        createChooserIntent();
                startActivity(intent);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectNameButton = (Button) view.findViewById(R.id.crime_suspect);
        if (mCrime.getSuspectName() != null) mSuspectNameButton.setText(mCrime.getSuspectName());
        mSuspectNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectNameButton.setEnabled(false);
        }

        mCallSuspect = (Button) view.findViewById(R.id.call_suspect);
        mCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23 && getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CONTACTS}, 0);
                }
                String suspectName = mCrime.getSuspectName();
                if (suspectName != null) {
                    String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like '%" + suspectName + "%'";
                    String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                    Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null, null);
                    String phoneNumber;
                    try {
                        if (cursor.getCount() == 0) return;
                        cursor.moveToFirst();
                        phoneNumber = cursor.getString(0);
                    } finally {
                        cursor.close();
                    }
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(phoneIntent);
                }
            }
        });
        if (mCrime.getSuspectName() == null) mCallSuspect.setEnabled(false);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(date);
                updateDate();
                break;
            case REQUEST_TIME:
                Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mCrime.setDate(time);
                updateTime();
                break;
            case REQUEST_CONTACT:
                if (data != null) {
                    Uri contactUri = data.getData();
                    String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
                    Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
                    try {
                        if (cursor.getCount() == 0) return;
                        cursor.moveToFirst();
                        String suspect = cursor.getString(0);
                        mCrime.setSuspectName(suspect);
                        mSuspectNameButton.setText(suspect);
                        mCallSuspect.setEnabled(true);
                    } finally {
                        cursor.close();
                    }
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                crimeLab.deleteCrime(mCrime.getId());
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        CrimeFragment crimeFragment = new CrimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CRIME_ID_KEY, crimeId);
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    private void updateDate() {
        mDateButton.setText(dateFormat.format(mCrime.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(timeFormat.format(mCrime.getDate()));
    }

    private String getReport() {
        String solvedString;
        if (mCrime.isSolved()) solvedString = getString(R.string.crime_report_solved);
        else solvedString = getString(R.string.crime_report_unsolved);

        String dateString = dateTimeFormat.format(mCrime.getDate());

        String suspect = mCrime.getSuspectName();
        if (suspect == null) suspect = getString(R.string.crime_report_no_suspect);
        else suspect = getString(R.string.crime_report_suspect, suspect);

        String policeRequired;
        if (mCrime.isPoliceRequired())
            policeRequired = getString(R.string.crime_report_police_required);
        else policeRequired = getString(R.string.crime_report_police_not_required);

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect, policeRequired);
        return report;
    }
}






















