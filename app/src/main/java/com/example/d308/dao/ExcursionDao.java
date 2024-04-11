package com.example.d308.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.d308.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {
    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursion_table WHERE vacationId = :vacationId")
    LiveData<List<Excursion>> getExcursionsForVacation(int vacationId);

    @Query("SELECT * FROM excursion_table WHERE id = :id")
    LiveData<Excursion> getExcursionById(int id);

    @Query("SELECT * FROM excursion_table")
    LiveData<List<Excursion>> getAllExcursions();
    @Query("SELECT * FROM excursion_table WHERE user_id = :userId")
    List<Excursion> getExcursionsByUserId(int userId);

    @Query("SELECT * FROM excursion_table ORDER BY id DESC LIMIT 1")
    LiveData<Excursion> getLastExcursion();

}
