package com.example.d308.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursion_table")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int vacationId; // foreign key
    private String name;
    private String date;

    // getters and setters...
    public int getId() {
        return id;
    }
    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    /* public Excursion(String name, String date) {
        this.name = name;
        this.date = date;
    } */
}
