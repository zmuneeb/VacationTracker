package com.example.d308;

import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

public class DateValidatorTest {
    private DateValidator dateValidator = new DateValidator();

    @Test
    public void testEndDateBeforeStartDate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2024, Calendar.DECEMBER, 31);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2024, Calendar.JANUARY, 1);

        assertFalse(dateValidator.isEndDateValid(startDate, endDate));
    }

    @Test
    public void testEndDateAfterStartDate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2024, Calendar.JANUARY, 1);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2024, Calendar.DECEMBER, 31);

        assertTrue(dateValidator.isEndDateValid(startDate, endDate));
    }
}
