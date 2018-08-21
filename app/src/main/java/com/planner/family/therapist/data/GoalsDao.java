package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GoalsDao {

    @Query("SELECT * FROM goals WHERE child_id = :childId")
    LiveData<List<Goal>> getGoalsByChildId(String childId);

    @Query("SELECT * FROM goals WHERE id LIKE :goalId")
    LiveData<Goal> getGoalById(int goalId);

    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("DELETE FROM goals WHERE id = :goalId")
    void deleteGoalById(int goalId);

    @Query("UPDATE goals SET goal = :goal WHERE id LIKE :goalId")
    void updateGoal(String goalId, String goal);
}
