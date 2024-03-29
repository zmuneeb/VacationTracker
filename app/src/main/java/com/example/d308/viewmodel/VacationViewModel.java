package com.example.d308.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308.entities.Vacation;
import com.example.d308.repository.AppRepository;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Vacation>> allVacations;

    public VacationViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allVacations = repository.getAllVacations();
    }

    public void insert(Vacation vacation) {
        repository.insert(vacation);
    }

    public void update(Vacation vacation) {
        repository.update(vacation);
    }

    public void delete(Vacation vacation) {
        repository.delete(vacation);
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public LiveData<Vacation> getVacationById(long id) {
        return repository.getVacationById(id);
    }
}