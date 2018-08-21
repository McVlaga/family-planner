package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ChildrenDao {

    @Query("SELECT * FROM children ORDER BY last_name ASC, first_name ASC")
    LiveData<List<Child>> getChildren();

    @Query("SELECT * FROM children WHERE id LIKE :childId")
    LiveData<Child> getChildById(String childId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Child child);

    @Update
    void update(Child child);

    @Delete
    void delete(Child child);
}
