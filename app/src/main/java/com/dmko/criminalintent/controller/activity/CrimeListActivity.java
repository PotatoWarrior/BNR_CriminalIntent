package com.dmko.criminalintent.controller.activity;

import android.support.v4.app.Fragment;

import com.dmko.criminalintent.controller.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
