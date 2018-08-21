package com.planner.family.therapist.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "supports", foreignKeys = @ForeignKey(
        entity = Child.class,
        parentColumns = "id",
        childColumns = "child_id",
        onDelete = CASCADE))
public class Support {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "support")
    public String support;

    @NonNull
    @ColumnInfo(name = "child_id")
    public final String childId;

    @Ignore
    public Support(@NonNull String support, @NonNull String childId) {
        this.support = support;
        this.childId = childId;
    }

    public Support(@NonNull int id, String support, String childId) {
        this.id = id;
        this.support = support;
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
    public String getSupport() {
        return support;
    }

    public void setSupport(@NonNull String support) {
        this.support = support;
    }

    @NonNull
    public String getChildId() {
        return childId;
    }

    public String toString() {
        return support;
    }
}
