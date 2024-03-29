package com.example.d308.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.d308.dao.ExcursionDao;
import com.example.d308.dao.VacationDao;
import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Vacation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private VacationDao vacationDao;
    private ExcursionDao excursionDao;
    private LiveData<List<Vacation>> allVacations;
    private ExecutorService executorService;

    public AppRepository(Application application) {
        AppDatabase database = Room.databaseBuilder(application, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        vacationDao = database.vacationDao();
        excursionDao = database.excursionDao();
        allVacations = vacationDao.getAllVacations();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Vacation vacation) {
        executorService.execute(() -> vacationDao.insert(vacation));
    }

    public void update(Vacation vacation) {
        executorService.execute(() -> vacationDao.update(vacation));
    }

    public void delete(Vacation vacation) {
        executorService.execute(() -> vacationDao.delete(vacation));
    }
    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }
}
