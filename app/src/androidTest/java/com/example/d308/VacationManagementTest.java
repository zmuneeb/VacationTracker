package com.example.d308;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class VacationManagementTest {

    @Rule
    public ActivityScenarioRule<VacationListActivity> activityRule =
            new ActivityScenarioRule<>(VacationListActivity.class);

    @Test
    public void test1UpdateButton() {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.updateButton))
                .perform(click());
        Espresso.onView(withId(R.id.nameEditText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test2DeleteButton() {
        String firstVacationName = getNameOfFirstVacation();
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.deleteButton))
                .perform(click());
        Espresso.onView(withText(firstVacationName))
                .check(doesNotExist());
    }

    @Test
    public void testViewButton() {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.viewButton))
                .perform(click());

        Espresso.onView(withId(R.id.vacationDetailsListView))
                .check(matches(isDisplayed()));
    }

    private String getNameOfFirstVacation() {
        return "First Vacation Name";
    }
}