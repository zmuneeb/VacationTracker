package com.example.d308.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.d308.entities.Vacation;
import java.util.List;

@Dao
public interface VacationDao {
    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacation_table")
    LiveData<List<Vacation>> getAllVacations();
    @Query("SELECT * FROM vacation_table WHERE id = :id")
    LiveData<Vacation> getVacationById(long id);
}
