package com.dmko.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dmko.criminalintent.R;
import com.dmko.criminalintent.controller.fragment.CrimeFragment;
import com.dmko.criminalintent.model.Crime;
import com.dmko.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Button mFirstButton, mLastButton;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_IC = "com.dmko.criminalintent.crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mFirstButton = (Button) findViewById(R.id.first_button);
        mLastButton = (Button) findViewById(R.id.last_button);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_IC);
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                updateButtons(i);
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size());
            }
        });
    }

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_IC, crimeId);
        return intent;
    }

    private void updateButtons(int position) {
        if (position == 0) mFirstButton.setEnabled(false);
        else mFirstButton.setEnabled(true);

        if (position == mCrimes.size() - 1) mLastButton.setEnabled(false);
        else mLastButton.setEnabled(true);
    }
}
