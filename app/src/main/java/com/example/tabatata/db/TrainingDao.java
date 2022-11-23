package com.example.tabatata.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrainingDao {
    /*@Query("SELECT * FROM training")
    LiveData<Training> getAll();*/

    @Query("SELECT * FROM training")
    List<Training> getAll();

    @Query("DELETE FROM training")
    void delete();

   /* @Query("SELECT * FROM training WHERE name = :nameParam")
    Training searchTrainingByName(String nameParam);*/

    @Query("SELECT * FROM training WHERE name =:nameParam")
    Training getTrainingByName(String nameParam);

    @Insert
    long insert(Training training);

    @Insert
    long[] insertAll(Training... trainings);

    @Delete
    void delete(Training training);

    @Update
    void update(Training training);
}