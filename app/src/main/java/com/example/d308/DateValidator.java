package com.example.d308;

import java.util.Calendar;

public class DateValidator {
    public boolean isEndDateValid(Calendar startDate, Calendar endDate) {
        return !endDate.before(startDate);
    }
}
