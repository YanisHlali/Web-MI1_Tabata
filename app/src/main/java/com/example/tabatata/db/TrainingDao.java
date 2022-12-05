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

    @Query("SELECT * FROM training WHERE name = :nameParam")
    Training getTrainingByName(String nameParam);

    @Query("SELECT * FROM training WHERE id = :id")
    Training getTrainingById(int id);

    @Query("UPDATE training SET preparation = :preparation," +
            "sequence = :sequence," +
            "cycle = :cycle," +
            "work = :work," +
            "rest = :rest,longRest = :longRest " +
            "WHERE id = :id")
    void updateTraining(int id, int preparation, int sequence, int cycle, int work, int rest, int longRest);

    @Insert
    long insert(Training training);

    @Insert
    long[] insertAll(Training... trainings);

    @Delete
    void delete(Training training);

    @Update
    void update(Training training);
}