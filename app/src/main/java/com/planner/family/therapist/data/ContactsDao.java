package com.planner.family.therapist.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM contacts WHERE child_id = :childId")
    LiveData<List<Contact>> getContactsByChildId(String childId);

    @Query("SELECT * FROM contacts WHERE id LIKE :contactId")
    LiveData<Contact> getContactsById(int contactId);

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contacts WHERE id = :contactId")
    void deleteContactById(int contactId);

    @Query("UPDATE contacts SET name = :name, number = :number WHERE id LIKE :stressorId")
    void updateContact(String stressorId, String name, String number);
}
