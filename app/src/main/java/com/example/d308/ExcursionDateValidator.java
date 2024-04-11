package com.example.d308;

import java.util.Calendar;

public class ExcursionDateValidator {
    public boolean isExcursionDateValid(Calendar vacationStartDate, Calendar vacationEndDate, Calendar excursionDate) {
        return !excursionDate.before(vacationStartDate) && !excursionDate.after(vacationEndDate);
    }
}
