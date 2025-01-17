package com.example.d308.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.example.d308.dao.ExcursionDao;
import com.example.d308.dao.VacationDao;
import com.example.d308.database.AppDatabase;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private VacationDao vacationDao;
    private ExcursionDao excursionDao;
    private LiveData<List<Vacation>> allVacations;
    private LiveData<List<Excursion>> allExcursions;
    private ExecutorService executorService;

    public AppRepository(Application application) {
        AppDatabase database = Room.databaseBuilder(application, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        vacationDao = database.vacationDao();
        excursionDao = database.excursionDao();
        allVacations = vacationDao.getAllVacations();
        allExcursions = excursionDao.getAllExcursions();
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
    public LiveData<Vacation> getVacationById(long id) {
        return vacationDao.getVacationById(id);
    }
    public void insert(Excursion excursion) {
        executorService.execute(() -> excursionDao.insert(excursion));
    }

    public void update(Excursion excursion) {
        executorService.execute(() -> excursionDao.update(excursion));
    }

    public void delete(Excursion excursion) {
        executorService.execute(() -> excursionDao.delete(excursion));
    }

    public LiveData<List<Excursion>> getAllExcursions() {
        return allExcursions;
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return excursionDao.getExcursionById(id);
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return excursionDao.getExcursionsForVacation(vacationId);
    }

    public LiveData<Vacation> getLastVacation() {
        return vacationDao.getLastVacation();
    }
    public LiveData<Excursion> getLastExcursion() {
        return excursionDao.getLastExcursion();
    }
}
