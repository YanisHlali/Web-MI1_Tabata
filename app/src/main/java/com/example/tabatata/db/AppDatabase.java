package com.example.tabatata.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Training.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TrainingDao taskDao();

}