package com.example.d308.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.d308.entities.User;

@Dao
public interface UserDao {
    @Insert
    void registerUser(User user);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    User checkUser(String username, String password);
    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);
}
