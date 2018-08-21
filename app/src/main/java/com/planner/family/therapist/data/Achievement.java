package com.planner.family.therapist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "achievements", foreignKeys = @ForeignKey(
        entity = Child.class,
        parentColumns = "id",
        childColumns = "child_id",
        onDelete = CASCADE))
public class Achievement {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "achievement")
    public String achievement;

    @NonNull
    @ColumnInfo(name = "child_id")
    public final String childId;

    @Ignore
    public Achievement(@NonNull String achievement, @NonNull String childId) {
        this.achievement = achievement;
        this.childId = childId;
    }

    public Achievement(@NonNull int id, String achievement, String childId) {
        this.id = id;
        this.achievement = achievement;
        this.childId = childId;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }


    @NonNull
    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(@NonNull String achievement) {
        this.achievement = achievement;
    }

    @NonNull
    public String getChildId() {
        return childId;
    }

    public String toString() {
        return achievement;
    }
}
