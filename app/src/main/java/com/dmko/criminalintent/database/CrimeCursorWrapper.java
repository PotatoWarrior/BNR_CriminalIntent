package com.dmko.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.dmko.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.dmko.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Date date = new Date(getLong(getColumnIndex(CrimeTable.Cols.DATE)));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        int isPoliceRequired = getInt(getColumnIndex(CrimeTable.Cols.POLICE_REQUIRED));
        String suspectName = getString(getColumnIndex(CrimeTable.Cols.SUSPECT_NAME));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(date);
        crime.setSolved(isSolved != 0);
        crime.setPoliceRequired(isPoliceRequired != 0);
        crime.setSuspectName(suspectName);

        return crime;
    }
}
