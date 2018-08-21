package com.planner.family.therapist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "stressors", foreignKeys = @ForeignKey(
        entity = Child.class,
        parentColumns = "id",
        childColumns = "child_id",
        onDelete = CASCADE))
public class Stressor {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "stressor")
    public String stressor;

    @NonNull
    @ColumnInfo(name = "child_id")
    public final String childId;

    @Ignore
    public Stressor(@NonNull String stressor, @NonNull String childId) {
        this.stressor = stressor;
        this.childId = childId;
    }

    public Stressor(@NonNull int id, String stressor, String childId) {
        this.id = id;
        this.stressor = stressor;
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
    public String getStressor() {
        return stressor;
    }

    public void setStressor(@NonNull String stressor) {
        this.stressor = stressor;
    }

    @NonNull
    public String getChildId() {
        return childId;
    }

    public String toString() {
        return stressor;
    }
}
