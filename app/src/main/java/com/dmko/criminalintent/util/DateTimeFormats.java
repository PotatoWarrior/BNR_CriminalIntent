package com.dmko.criminalintent.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class DateTimeFormats {
    private DateTimeFormats(){}
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());;
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());;
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm, EEEE, MMM dd, yyyy", Locale.getDefault());
}
