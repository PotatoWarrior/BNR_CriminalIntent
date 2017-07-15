package com.dmko.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmko.criminalintent.R;
import com.dmko.criminalintent.controller.activity.CrimePagerActivity;
import com.dmko.criminalintent.model.Crime;
import com.dmko.criminalintent.model.CrimeLab;
import com.dmko.criminalintent.util.DateTimeFormats;

import java.text.SimpleDateFormat;
import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private TextView mNoCrimesTextView;
    private Button mAddCrimeButton;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String KEY_SUBTITILE_VISIBILITY = "subtitle_visible";

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView mTitleTextView, mDataTextView;
        protected ImageView mSolvedImageView;
        protected Crime mCrime;
        protected SimpleDateFormat mDateFormat = DateTimeFormats.dateFormat;

        public CrimeHolder(View view) {
            super(view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDataTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDataTextView.setText(mDateFormat.format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            startActivity(CrimePagerActivity.newIntent(getActivity(), mCrime.getId()));
        }
    }

    private class NormalCrimeViewHolder extends CrimeHolder {
        public NormalCrimeViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_item_normal_crime, parent, false));
        }
    }

    private class PoliceRequiredCrimeViewHolder extends CrimeHolder {
        public PoliceRequiredCrimeViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_tem_police_required_crime, parent, false));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        private static final int NORMAL_CRIME_TYPE = 1;
        private static final int REQUIRES_POLICE_CRIME_TYPE = 2;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case NORMAL_CRIME_TYPE:
                    return new NormalCrimeViewHolder(layoutInflater, parent);
                case REQUIRES_POLICE_CRIME_TYPE:
                    return new PoliceRequiredCrimeViewHolder(layoutInflater, parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            boolean isPoliceRequired = mCrimes.get(position).isPoliceRequired();
            return isPoliceRequired ? REQUIRES_POLICE_CRIME_TYPE : NORMAL_CRIME_TYPE;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITILE_VISIBILITY);
        }
        mNoCrimesTextView = (TextView) view.findViewById(R.id.no_crimes_text_view);
        mAddCrimeButton = (Button) view.findViewById(R.id.add_button);
        mAddCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddCrimeActivity();
            }
        });
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITILE_VISIBILITY, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem menuItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                startAddCrimeActivity();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startAddCrimeActivity() {
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (!crimes.isEmpty()) {
            mAddCrimeButton.setVisibility(View.INVISIBLE);
            mNoCrimesTextView.setVisibility(View.INVISIBLE);
            if (mAdapter == null) {
                mAdapter = new CrimeAdapter(crimes);
                mCrimeRecyclerView.setAdapter(mAdapter);
            }
        } else {
            mAddCrimeButton.setVisibility(View.VISIBLE);
            mNoCrimesTextView.setVisibility(View.VISIBLE);
        }
        if (mAdapter != null) {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = mSubtitleVisible ? getResources().getQuantityString(R.plurals.subtitle_plurals, crimeCount, crimeCount) : null;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }
}

