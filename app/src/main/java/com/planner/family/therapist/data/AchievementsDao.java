package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AchievementsDao {

    @Query("SELECT * FROM achievements WHERE child_id = :childId")
    LiveData<List<Achievement>> getAchievementsByChildId(String childId);

    @Query("SELECT * FROM achievements WHERE id LIKE :achievementId")
    LiveData<Achievement> getAchievementById(int achievementId);

    @Insert
    void insert(Achievement achievement);

    @Update
    void update(Achievement achievement);

    @Delete
    void delete(Achievement achievement);

    @Query("DELETE FROM achievements WHERE id = :achievementsId")
    void deleteAchievementById(int achievementsId);

    @Query("UPDATE achievements SET achievement = :achievement WHERE id LIKE :achievementId")
    void updateAchievement(String achievementId, String achievement);
}
