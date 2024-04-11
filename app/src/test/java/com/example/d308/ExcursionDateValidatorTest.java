package com.example.d308;

import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

public class ExcursionDateValidatorTest {
    private ExcursionDateValidator excursionDateValidator = new ExcursionDateValidator();

    @Test
    public void testExcursionDateWithinVacationDates() {
        Calendar vacationStartDate = Calendar.getInstance();
        vacationStartDate.set(2024, Calendar.JANUARY, 1);

        Calendar vacationEndDate = Calendar.getInstance();
        vacationEndDate.set(2024, Calendar.DECEMBER, 31);

        Calendar excursionDate = Calendar.getInstance();
        excursionDate.set(2024, Calendar.JUNE, 15);

        assertTrue(excursionDateValidator.isExcursionDateValid(vacationStartDate, vacationEndDate, excursionDate));
    }

    @Test
    public void testExcursionDateOutsideVacationDates() {
        Calendar vacationStartDate = Calendar.getInstance();
        vacationStartDate.set(2024, Calendar.JANUARY, 1);

        Calendar vacationEndDate = Calendar.getInstance();
        vacationEndDate.set(2024, Calendar.DECEMBER, 31);

        Calendar excursionDate = Calendar.getInstance();
        excursionDate.set(2025, Calendar.JANUARY, 1);

        assertFalse(excursionDateValidator.isExcursionDateValid(vacationStartDate, vacationEndDate, excursionDate));
    }
}
