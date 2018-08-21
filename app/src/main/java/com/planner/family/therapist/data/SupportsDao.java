package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SupportsDao {

    @Query("SELECT * FROM supports WHERE child_id = :childId")
    LiveData<List<Support>> getSupportsByChildId(String childId);

    @Query("SELECT * FROM supports WHERE id LIKE :supportId")
    LiveData<Support> getSupportById(int supportId);

    @Insert
    void insert(Support support);

    @Update
    void update(Support support);

    @Delete
    void delete(Support support);

    @Query("DELETE FROM supports WHERE id = :supportId")
    void deleteSupportById(int supportId);

    @Query("UPDATE supports SET support = :support WHERE id LIKE :supportId")
    void updateSupport(String supportId, String support);
}
