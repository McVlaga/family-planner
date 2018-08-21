package com.planner.family.therapist.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "goals", foreignKeys = @ForeignKey(
        entity = Child.class,
        parentColumns = "id",
        childColumns = "child_id",
        onDelete = CASCADE))
public class Goal {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "goal")
    public String goal;

    @NonNull
    @ColumnInfo(name = "child_id")
    public final String childId;

    @Ignore
    public Goal(@NonNull String goal, @NonNull String childId) {
        this.goal = goal;
        this.childId = childId;
    }

    public Goal(@NonNull int id, String goal, String childId) {
        this.id = id;
        this.goal = goal;
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
    public String getGoal() {
        return goal;
    }

    public void setGoal(@NonNull String goal) {
        this.goal = goal;
    }

    @NonNull
    public String getChildId() {
        return childId;
    }

    public String toString() {
        return goal;
    }
}
