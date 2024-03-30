package com.example.d308.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.d308.dao.ExcursionDao;
import com.example.d308.dao.VacationDao;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();
}
