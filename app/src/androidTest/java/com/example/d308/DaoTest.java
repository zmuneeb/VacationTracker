package com.example.d308;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.d308.dao.ExcursionDao;
import com.example.d308.dao.VacationDao;
import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DaoTest {
    private ExcursionDao excursionDao;
    private VacationDao vacationDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        excursionDao = db.excursionDao();
        vacationDao = db.vacationDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testVacationAndExcursionCreation() throws InterruptedException {
        Vacation vacation = new Vacation();
        vacation.setName("Test Vacation");

        vacationDao.insert(vacation);

        Vacation lastVacation = LiveDataTestUtil.getValue(vacationDao.getLastVacation());
        assertEquals(vacation.getName(), lastVacation.getName());

        Excursion excursion = new Excursion();
        excursion.setName("Test Excursion");

        excursionDao.insert(excursion);

        Excursion lastExcursion = LiveDataTestUtil.getValue(excursionDao.getLastExcursion());
        assertEquals(excursion.getName(), lastExcursion.getName());
    }
}
