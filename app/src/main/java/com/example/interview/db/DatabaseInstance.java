package com.example.interview.db;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.interview.db.user.UserDao;
import com.example.interview.page.main.model.UserModel;

@Database(entities = {UserModel.class} , version = 1)
public abstract class DatabaseInstance extends RoomDatabase {

    private static DatabaseInstance databaseInstance;
    public static final String DB_NAME = "User";


    public static synchronized DatabaseInstance getDatabaseInstance(Context context){
        if (databaseInstance == null){
            databaseInstance = Room.databaseBuilder(context,  DatabaseInstance.class , DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return databaseInstance;
    }


    public abstract UserDao userDao();

}
