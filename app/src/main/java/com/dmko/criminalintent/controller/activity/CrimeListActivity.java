package com.dmko.criminalintent.controller.activity;

import android.support.v4.app.Fragment;

import com.dmko.criminalintent.R;
import com.dmko.criminalintent.controller.fragment.CrimeFragment;
import com.dmko.criminalintent.controller.fragment.CrimeListFragment;
import com.dmko.criminalintent.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null) {
            startActivity(CrimePagerActivity.newIntent(this, crime.getId()));
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();
        }
    }

    @Override
    public void onCrimeUpdate(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
