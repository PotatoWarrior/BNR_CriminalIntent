package com.dmko.criminalintent.model;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mPoliceRequired;
    private String mSuspectName;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isPoliceRequired() {
        return mPoliceRequired;
    }

    public void setPoliceRequired(boolean policeRequired) {
        mPoliceRequired = policeRequired;
    }

    public String getSuspectName() {
        return mSuspectName;
    }

    public void setSuspectName(String suspectName) {
        mSuspectName = suspectName;
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + mDate.hashCode();
        result = 31 * result + (mSolved ? 1 : 0);
        result = 31 * result + (mPoliceRequired ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof Crime)) return false;
        UUID crimeId = ((Crime) obj).getId();
        return mId.equals(crimeId);
    }
}
