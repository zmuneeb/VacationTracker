package com.example.d308.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308.entities.Excursion;
import com.example.d308.repository.AppRepository;

import java.util.List;

public class ExcursionViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Excursion>> allExcursions;

    public ExcursionViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allExcursions = repository.getAllExcursions();
    }

    public void insert(Excursion excursion) {
        repository.insert(excursion);
    }

    public void update(Excursion excursion) {
        repository.update(excursion);
    }

    public void delete(Excursion excursion) {
        repository.delete(excursion);
    }

    public LiveData<List<Excursion>> getAllExcursions() {
        return allExcursions;
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return repository.getExcursionById(id);
    }
    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return repository.getExcursionsForVacation(vacationId);
    }
    public LiveData<Excursion> getLastExcursion() {
        return repository.getLastExcursion();
    }
}
