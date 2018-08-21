package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StressorsDao {

    @Query("SELECT * FROM stressors WHERE child_id = :childId")
    LiveData<List<Stressor>> getStressorsByChildId(String childId);

    @Query("SELECT * FROM stressors WHERE id LIKE :stressorId")
    LiveData<Stressor> getStressorById(int stressorId);

    @Insert
    void insert(Stressor stressor);

    @Update
    void update(Stressor stressor);

    @Delete
    void delete(Stressor stressor);

    @Query("DELETE FROM stressors WHERE id = :stressorId")
    void deleteStressorById(int stressorId);

    @Query("UPDATE stressors SET stressor = :stressor WHERE id LIKE :stressorId")
    void updateStressor(String stressorId, String stressor);
}
