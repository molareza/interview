package com.example.interview.db.user;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.interview.page.main.model.UserModel;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User order by id2 DESC")
    LiveData<List<UserModel>> getAllUser();

    @Query("select * from User where userId = :id")
    UserModel getUser(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserList(List<UserModel> userModels);

    @Insert
    void insertUser(UserModel user);

    @Delete
    void deleteUser(UserModel user);

    @Update
    void editUser(UserModel user);

}
